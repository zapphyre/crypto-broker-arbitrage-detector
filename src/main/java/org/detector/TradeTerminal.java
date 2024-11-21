package org.detector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.detector.broker.Broker;
import org.detector.broker.impl.BitfinexBroker;
import org.detector.broker.impl.KrakenBroker;
import org.detector.command.Command;
import org.detector.command.bitfinex.BitfinexSubscribeTickerCommand;
import org.detector.command.kraken.KrakenSubscribeTickerCommand;
import org.detector.communication.Communication;
import org.detector.connector.WsConnector;
import org.detector.connector.impl.FixTrialConnector;
import org.detector.function.BrokerageIssuer;
import org.detector.function.IssuerBuild;
import org.detector.function.MapperClosure;
import org.detector.interaction.Issuer;
import org.detector.interaction.impl.CommandIssuer;
import org.detector.mapper.ConnectorMapper;
import org.detector.model.bitfinex.BitfinexTick;
import org.detector.model.kraken.KrakenTick;
import org.detector.pojo.Signal;
import org.detector.pojo.Tick;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

@Slf4j
public class TradeTerminal {
    final static ObjectMapper objectMapper = new ObjectMapper();
    final static Broker kraken = new KrakenBroker();
    final static Broker bitfinex = new BitfinexBroker();
    final static ConnectorMapper tickMapper = Mappers.getMapper(ConnectorMapper.class);

    final static MapperClosure issuerCreator = q -> p -> CommandIssuer.builder()
            .sinkSessionPair(p)
            .objectMapper(q)
            .build();

    static Function<ObjectMapper, IssuerBuild> issuerBuildFunction = issuerCreator::mapperUsed;

    static Function<ObjectMapper, Function<Broker, Issuer>> issuerFunction = om -> broker ->
            issuerBuildFunction
                    .andThen(Communication.initSession(broker)::getDoneIssuer)
                    .apply(om);

    public static void main(String[] args) throws InterruptedException {
        final Command krakenTickerSub = KrakenSubscribeTickerCommand.of("BTC", "USDT");
        final Command bitfinexTickerSub = BitfinexSubscribeTickerCommand.of("BTC", "USD");

        Function<Broker, Issuer> brokerIssuer = issuerFunction.apply(objectMapper);

        final Flux<Tick> arbiterFlux = brokerIssuer.apply(kraken)
                .issue(krakenTickerSub)
                .mapNotNull(brokerSpecific(KrakenTick.class))
                .map(KrakenTick::getData)
                .flatMap(Flux::fromIterable)
                .map(tickMapper::map);

        final Flux<Tick> arbiteeFlux = brokerIssuer.apply(bitfinex)
                .issue(bitfinexTickerSub)
                .mapNotNull(brokerSpecific(BitfinexTick.class))
                .map(BitfinexTick::getData)
                .map(tickMapper::map);

        Flux.combineLatest(arbiterFlux, arbiteeFlux, TradeTerminal::signalize)
                .map(TradeTerminal::countPremium)
                .distinctUntilChanged(Signal::getPremiumPercent)
                .doOnDiscard(Signal.class, q -> log.info("discarded signal gain of: {}", q.getPremiumPercent()))
                .subscribe(q -> log.info(
                        "buy on: {} for: {}; sell on: {} for: {} -> gain of: {}%",
                        q.getArbiter().getBrokerName(),
                        q.getArbiter().getAsk(),
                        q.getArbitee().getBrokerName(),
                        q.getArbitee().getBid(),
                        q.getPremiumPercent()));

        Thread.currentThread().join();
    }

    static Signal signalize(final Tick arbiter, final Tick arbitee) {
        return arbiter.getAsk().subtract(arbitee.getBid()).compareTo(BigDecimal.ZERO) < 0 ?
                Signal.builder()
                        .arbiter(arbiter)
                        .arbitee(arbitee)
                        .build() :
                Signal.builder()
                        .arbiter(arbitee)
                        .arbitee(arbiter)
                        .build();
    }

    static Signal countPremium(Signal signal) {
        return signal.withPremiumPercent(toPercentageOf(signal.getArbitee().getBidSize(), signal.getArbiter().getAsk()));
    }

    static BigDecimal toPercentageOf(BigDecimal value, BigDecimal total) {
        return value.divide(total, 5, RoundingMode.HALF_UP).scaleByPowerOfTen(2);
    }

    static <T> Function<String, T> brokerSpecific(Class<T> clazz) {
        return s -> {
            try {
                return objectMapper.readValue(s, clazz);
            } catch (JsonProcessingException e) {
                return null;
            }
        };
    }
}

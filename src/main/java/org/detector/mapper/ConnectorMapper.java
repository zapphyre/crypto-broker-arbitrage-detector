package org.detector.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import org.detector.model.bitfinex.BitfinexWsTickDetail;
import org.detector.model.kraken.KrakenWsTick;
import org.detector.pojo.Tick;

@Mapper
public interface ConnectorMapper {

    @Mapping(target = "brokerName", constant = "Bitfinex")
    Tick map(BitfinexWsTickDetail tick);

    @Mapping(target = "askSize", source = "askQty")
    @Mapping(target = "bidSize", source = "bidQty")
    @Mapping(target = "brokerName", constant = "Kraken")
    Tick map(KrakenWsTick krakenWsTick);
}

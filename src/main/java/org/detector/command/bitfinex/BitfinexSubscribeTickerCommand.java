package org.detector.command.bitfinex;

import lombok.Builder;
import lombok.Value;

import org.detector.command.Command;

@Value
@Builder
public class BitfinexSubscribeTickerCommand implements Command {

    String event = "subscribe";
    String channel = "ticker";
    String symbol;

    public static Command of(String trade, String profit) {
        return BitfinexSubscribeTickerCommand.builder()
                .symbol("t" + trade + profit)
                .build();
    }
}

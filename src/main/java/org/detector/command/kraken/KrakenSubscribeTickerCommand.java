package org.detector.command.kraken;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import org.detector.command.Command;

@Value
@Builder
public class KrakenSubscribeTickerCommand implements Command {

    String method;
    Params params;

    @JsonProperty("req_id")
    Long reqId;

    public static Command of(String trade, String profit) {
        return KrakenSubscribeTickerCommand.builder()
                .method("subscribe")
                .reqId(System.currentTimeMillis())
                .params(Params.builder()
                                .channel("ticker")
                                .snapshot(false)
                                .symbol(trade + "/" + profit)
                                .build())
                .build();
    }

    @Value
    @Builder
    static class Params {
        String channel;
        Boolean snapshot;

        @Singular("symbol")
        List<String> symbol;
    }
}

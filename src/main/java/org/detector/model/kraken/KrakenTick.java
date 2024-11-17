package org.detector.model.kraken;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class KrakenTick {
    String channel;
    String type;
    @NonNull
    List<KrakenWsTick> data;
}

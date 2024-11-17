package org.detector.model.kraken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class KrakenWsTick {
    String symbol;

    BigDecimal bid;
    @JsonProperty("bid_qty")
    BigDecimal bidQty;

    BigDecimal ask;
    @JsonProperty("ask_qty")
    BigDecimal askQty;

    BigDecimal last;
    BigDecimal volume;
    BigDecimal vwap;

    BigDecimal low;
    BigDecimal high;

    BigDecimal change;

    @JsonProperty("change_pct")
    BigDecimal changePct;
}

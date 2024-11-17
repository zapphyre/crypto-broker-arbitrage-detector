package org.detector.model.bitfinex;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class BitfinexWsTickDetail {
    private final float bid; //Price of last highest bid
    private final float bidSize; //Sum of the 25 highest bid sizes
    private final float ask; //Price of last lowest ask
    private final float askSize; //Sum of the 25 lowest ask sizes
    private final float dailyChange; //Amount that the last price has changed since yesterday
    private final float dailyChangeRelative; //Relative price change since yesterday (*100 for percentage change)
    private final float lastPrice; //Price of the last trade.
    private final float volume; //Daily volume
    private final float high; //Daily high
    private final float low; //Daily low
}

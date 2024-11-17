package org.detector.pojo;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class Tick {
    BigDecimal bid;
    BigDecimal bidSize;

    BigDecimal ask;
    BigDecimal askSize;

    String brokerName;
}

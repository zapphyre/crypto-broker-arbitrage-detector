package org.detector.pojo;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@With
@Builder
public class Signal {
    Tick arbiter;
    Tick arbitee;

    BigDecimal premiumPercent;
}

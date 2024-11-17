package org.detector.strategy.impl;

import lombok.Setter;
import org.detector.strategy.Strategy;
import reactor.core.publisher.Sinks;

import org.detector.pojo.Tick;

@Setter
public class ArbitrageStrategy implements Strategy {

    private Tick arbiterState;
    private Tick arbiteeState;

    private final Sinks.Many<String> signalSink = Sinks.many().multicast().onBackpressureBuffer();

}

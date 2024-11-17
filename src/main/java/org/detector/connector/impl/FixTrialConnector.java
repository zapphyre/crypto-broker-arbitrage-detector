package org.detector.connector.impl;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.detector.broker.Broker;
import org.detector.connector.WsConnector;
import org.detector.pojo.SinkSessionPair;
import org.detector.ws.BrokerWS;

import java.util.concurrent.atomic.AtomicInteger;

@Value
@RequiredArgsConstructor
public class FixTrialConnector implements WsConnector {

    int maxTrials;
    AtomicInteger counter = new AtomicInteger();

    public SinkSessionPair connect(Broker broker) {
        try {
            return new BrokerWS().connect(broker);
        } catch (RuntimeException e) {
            if (counter.getAndIncrement() == maxTrials) {
                throw new RuntimeException("Connector has failed");
            } else {
                return connect(broker);
            }
        }
    }
}

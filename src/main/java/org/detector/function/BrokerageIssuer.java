package org.detector.function;

import org.detector.broker.Broker;
import org.detector.interaction.Issuer;

@FunctionalInterface
public interface BrokerageIssuer {

    Issuer issuerOnBroker(Broker broker);
}

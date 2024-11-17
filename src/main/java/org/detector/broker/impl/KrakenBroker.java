package org.detector.broker.impl;

import org.detector.broker.Broker;

public class KrakenBroker implements Broker {

    public final static String URL = "wss://ws.kraken.com/v2";

    @Override
    public String getBrokerUrl() {
        return URL;
    }
}

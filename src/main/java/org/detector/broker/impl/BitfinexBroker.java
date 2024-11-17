package org.detector.broker.impl;


import org.detector.broker.Broker;

public class BitfinexBroker implements Broker {

    public final static String URL = "wss://api.bitfinex.com/ws/2";

    @Override
    public String getBrokerUrl() {
        return URL;
    }
}

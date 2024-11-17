package org.detector.connector;

import org.detector.broker.Broker;
import org.detector.pojo.SinkSessionPair;

public interface WsConnector {

    SinkSessionPair connect(Broker broker);
}

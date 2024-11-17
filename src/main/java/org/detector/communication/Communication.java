package org.detector.communication;

import lombok.experimental.UtilityClass;
import org.detector.broker.Broker;
import org.detector.connector.WsConnector;
import org.detector.connector.impl.FixTrialConnector;
import org.detector.function.IssuerBuild;
import org.detector.function.IssuerDone;
import org.detector.interaction.Issuer;
import org.detector.pojo.SinkSessionPair;

import java.util.function.Function;

@UtilityClass
public class Communication {

    static WsConnector wsConnector = new FixTrialConnector(3);

    static Function<Broker, SinkSessionPair> connectBrokerOn = wsConnector::connect;

    public static IssuerDone initSession(Broker broker) {
        return b -> connectBrokerOn
                .andThen(b::createIssuer)
                .apply(broker);
    }
}

package org.detector.function;

import org.detector.interaction.Issuer;
import org.detector.pojo.SinkSessionPair;

@FunctionalInterface
public interface IssuerBuild {

    Issuer createIssuer(SinkSessionPair sinkSessionPair);
}

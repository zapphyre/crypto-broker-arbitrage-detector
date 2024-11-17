package org.detector.pojo;

import jakarta.websocket.Session;
import lombok.Builder;
import lombok.Value;
import reactor.core.publisher.Flux;

@Value
@Builder
public class SinkSessionPair {
    Session session;
    Flux<String> stringFlux;
}

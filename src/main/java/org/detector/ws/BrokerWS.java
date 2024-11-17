package org.detector.ws;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.detector.broker.Broker;
import reactor.core.publisher.Sinks;

import org.detector.pojo.SinkSessionPair;

@Slf4j
@ClientEndpoint
@RequiredArgsConstructor
public class BrokerWS {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
    private Session session;

    public SinkSessionPair connect(Broker broker) {
        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            session = container.connectToServer(this, URI.create(broker.getBrokerUrl()));
        } catch (DeploymentException | IOException e) {
            log.error("couldn't connect to websocket; {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return SinkSessionPair.builder()
                .session(session)
                .stringFlux(sink.asFlux())
                .build();
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("session opened");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("closing session");
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message) {
        sink.tryEmitNext(message);
    }

}

package org.detector.interaction.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.detector.interaction.Issuer;
import reactor.core.publisher.Flux;

import org.detector.command.Command;
import org.detector.pojo.SinkSessionPair;

@Value
@Builder
public class CommandIssuer implements Issuer {

    @NonNull
    ObjectMapper objectMapper;

    @NonNull
    SinkSessionPair sinkSessionPair;

    @Override
    public Flux<String> issue(final Command command) {
        Optional.ofNullable(command)
                .map(c -> c.toJsonString(objectMapper))
                .ifPresent(q -> Optional.of(sinkSessionPair)
                        .map(SinkSessionPair::getSession)
                        .map(Session::getAsyncRemote)
                        .ifPresent(session -> session.sendText(q)));

        return sinkSessionPair.getStringFlux();
    }
}

package org.detector.interaction;

import reactor.core.publisher.Flux;

import org.detector.command.Command;

public interface Issuer {
    Flux<String> issue(Command command);
}

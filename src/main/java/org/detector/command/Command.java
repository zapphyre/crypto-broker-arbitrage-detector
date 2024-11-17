package org.detector.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public interface Command {

    @SneakyThrows
    default String toJsonString(ObjectMapper objectMapper) {
        return objectMapper.writeValueAsString(this);
    }
}

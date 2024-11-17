package org.detector.function;

import com.fasterxml.jackson.databind.ObjectMapper;

@FunctionalInterface
public interface MapperClosure {

    IssuerBuild mapperUsed(ObjectMapper objectMapper);
}

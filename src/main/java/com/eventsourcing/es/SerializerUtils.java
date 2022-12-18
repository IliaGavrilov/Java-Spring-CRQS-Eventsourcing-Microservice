package com.eventsourcing.es;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

public final class SerializerUtils {

    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    private SerializerUtils() {
    }

    public static byte[] serializeToJsonBytes(final Object object) {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T deserializeFromJsonBytes(final byte[] jsonBytes, final Class<T> valueType) {
        try {
            return objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonBytes, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Event[] deserializeEventsFromJsonBytes(final byte[] jsonBytes) {
        try {
            return objectMapper.readValue(jsonBytes, Event[].class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
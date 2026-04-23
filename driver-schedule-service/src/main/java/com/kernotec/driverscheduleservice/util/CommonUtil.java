package com.kernotec.driverscheduleservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <O> O getObjecOfString(String value, TypeReference<O> typeReference) {
        return getObjecOfString(value, typeReference, objectMapper);
    }

    public static <O> O getObjecOfString(String value, TypeReference<O> typeReference,
        ObjectMapper objectMapper)
    {
        try {
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException ex) {
            log.error(
                "Error while converting string to object [objectMapperCustom]: {}",
                ex.getMessage()
            );
            throw new RuntimeException(ex);
        }
    }

    public static String toUpperCase(String value) {
        if (value == null) {
            return null;
        }

        return value.toUpperCase();
    }

    public static String getSafeString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}

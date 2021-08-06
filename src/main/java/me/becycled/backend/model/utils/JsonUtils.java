package me.becycled.backend.model.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Ivan Muratov
 */
@SuppressWarnings("WhitespaceAround")
public enum JsonUtils {;

    private static final ObjectMapper JSON_MAPPER;
    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        JSON_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JSON_MAPPER.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    /**
     * @author Ivan Muratov
     */
    public static final class Views {

        /**
         * @author Ivan Muratov
         */
        public static final class AttributeColumn {

        }
    }
}

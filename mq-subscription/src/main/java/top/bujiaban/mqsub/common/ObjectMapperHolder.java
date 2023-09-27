package top.bujiaban.mqsub.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .findAndRegisterModules();

    public static JsonNode objectToJsonNode(Object obj) {
        return objectMapper.convertValue(obj, JsonNode.class);
    }

    public static <T> T jsonNodeToObject(JsonNode json, Class<T> clazz) {
        return objectMapper.convertValue(json, clazz);
    }

}

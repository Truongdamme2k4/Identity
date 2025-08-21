package com.identity.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
public class DataUtils {
    private DataUtils() {
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || "".equals(value.trim());
    }

    public static <T> List<T> jsonToListThrow(String json, Class<T> classOutput, String message) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY).registerModule(new JavaTimeModule());
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        try {
            return objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, classOutput));
        } catch (Exception ex) {
            throw new RuntimeException(message + ": " + ex.getMessage(), ex);
        }
    }

    public static String objectToJson(Object data, String defaultValue) {
        if (isNull(data)) {
            return defaultValue;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            return mapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
            return "";
        }
    }

    public static String objectToJson(Object data) {
        return objectToJson(data, "");
    }

    private static <T> T jsonToObjectFronGson(String jsonData, Class<T> classOutput) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonData, classOutput);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T jsonToObject(String jsonData, Class<T> classOutput) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(jsonData, classOutput);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return jsonToObjectFronGson(jsonData, classOutput);
        } catch (Throwable i){
            return jsonToObjectFronGson(jsonData, classOutput);
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }


}

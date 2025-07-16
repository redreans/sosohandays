package com.sosohandays.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapUtil {
    public static Map<String, Object> toMap(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }
}

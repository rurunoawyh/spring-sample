package com.wyh.spring.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Json操作工具类
 *
 * @author 杨小院
 */
@Slf4j
public class JsonUtil {
    private JsonUtil() {
    }

    private final static ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Java对象转换为对应的json字符串
     *
     * @param obj 需要转换的java对象
     * @return java对象的json字符串
     * @throws Exception
     */
    public static String getJsonString(Object obj) throws Exception {
        String jsonStr = null;
        if (obj == null) {
            //do nothing
        } else if (obj instanceof String) {
            jsonStr = (String)obj;
        } else {
            jsonStr = MAPPER.writeValueAsString(obj);
        }
        return jsonStr;
    }

    /**
     * Java对象转换为对应的json字符串
     *
     * @param obj 需要转换的java对象
     * @return java对象的json字符串
     */
    public static String getJsonStringQuietly(Object obj) {
        try {
            return getJsonString(obj);
        } catch (Exception e) {
            log.error("exception when getJsonString", e);
            return null;
        }
    }

    /**
     * 解析得到java对象
     *
     * @param jsonStr
     * @param valueType 需要转换得到的java对象类型
     * @return
     * @throws Exception
     */
    public static <T> T getJavaObject(String jsonStr, Class<T> valueType) throws Exception {
        T object = null;
        if (jsonStr == null) {
            //do nothing
        } else if (valueType.isAssignableFrom(String.class)) {
            object = (T)jsonStr;
        } else {
            object = MAPPER.readValue(jsonStr, valueType);
        }
        return object;
    }

    /**
     * 解析得到java对象
     *
     * @param jsonStr
     * @param valueType 需要转换得到的java对象类型
     * @return
     */
    public static <T> T getJavaObjectQuietly(String jsonStr, Class<T> valueType) {
        try {
            return getJavaObject(jsonStr, valueType);
        } catch (Exception e) {
            log.error("exception when getJavaObject", e);
            return null;
        }
    }

    /**
     * 解析得到java泛型对象
     *
     * @param jsonStr
     * @param valueTypeRef 泛型对象类型
     * @return
     * @throws Exception
     */
    public static <T> T getJavaGeneric(String jsonStr, TypeReference<T> valueTypeRef) throws Exception {
        return (jsonStr == null) ? null : MAPPER.readValue(jsonStr, valueTypeRef);
    }

    public static <T> T getJavaGenericQuietly(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return getJavaGeneric(jsonStr, valueTypeRef);
        } catch (Exception e) {
            log.error("exception when getJavaGeneric", e);
            return null;
        }
    }
}

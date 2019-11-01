package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.uitls.core.ExtendDateFormat;
import com.github.yafeiwang1240.obrien.uitls.core.StringUnicodeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 普通json util 基础上扩展json功能
 */
public class JsonUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static ObjectMapper UNICODE_SERIALIZER_OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {}

    static {
        OBJECT_MAPPER.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        OBJECT_MAPPER.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 扩展时间格式
        OBJECT_MAPPER.getDeserializationConfig().withDateFormat(ExtendDateFormat.instance);
        OBJECT_MAPPER.getJsonFactory().disable(JsonParser.Feature.INTERN_FIELD_NAMES);// 避免触发的String.intern(), 导致内存持续增加

        // unicode
        CustomSerializerFactory serializerFactory = new CustomSerializerFactory();
        serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());
        UNICODE_SERIALIZER_OBJECT_MAPPER.setSerializerFactory(serializerFactory);
        UNICODE_SERIALIZER_OBJECT_MAPPER.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        UNICODE_SERIALIZER_OBJECT_MAPPER.getDeserializationConfig().without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 不抛异常的接口
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (object == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            logger.warn("obj to json fail with " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * to unicode json
     * @param object
     * @return
     */
    public static String toUnicodeJson(Object object) {
        if (object == null) return null;
        try {
            return UNICODE_SERIALIZER_OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            logger.warn("obj to unicode json fail with " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * to object
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) return null;
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            logger.warn("json to object failed with " + e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) return null;
        try {
            if (json.startsWith("[") && json.endsWith("]")) {
                return OBJECT_MAPPER.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, TypeFactory.defaultInstance().constructType(clazz)));
            }
            return Lists.asList(OBJECT_MAPPER.readValue(json, clazz));
        } catch (Exception e) {
            logger.warn("json to array failed with " + e.getMessage(), e);
        }
        return null;
    }
}

package com.github.yafeiwang1240.obrien.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Map生成工具
 */
public class Maps {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

    public static <K, V> Map<K, V>  newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> Map<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> Map<K, V> newHashMap(final int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }

    public static Map<String, Object> newMap(Object... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new IllegalArgumentException("key value Paired appearance");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            if (!(keyValues[i] instanceof String)) {
                throw new IllegalArgumentException("key must instanceof String");
            }
            map.put(keyValues[i].toString(), keyValues[i + 1]);
        }
        return map;
    }

    public static Map<String, String> newStringMap(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("key value Paired appearance");
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return map;
    }

    public static Map<Object, Object> newObjectMap(Object... keyValues) {
        if (keyValues.length % 2 == 1) {
            throw new IllegalArgumentException("key value Paired appearance");
        }
        Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return map;
    }
}

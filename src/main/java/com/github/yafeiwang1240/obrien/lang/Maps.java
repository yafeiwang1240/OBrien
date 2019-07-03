package com.github.yafeiwang1240.obrien.lang;

import java.util.HashMap;
import java.util.Map;

/**
 * Map生成工具
 */
public class Maps {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

    public static <K, V> Map<K, V> newHashMap(final int initialCapacity) {
        return new HashMap<>(initialCapacity);
    }
}

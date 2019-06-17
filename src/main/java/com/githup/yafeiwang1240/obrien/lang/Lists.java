package com.githup.yafeiwang1240.obrien.lang;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List 生成工具
 */
public class Lists {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

    public static ArrayList newArrayList(int initialCapacity) {
        return new ArrayList(initialCapacity);
    }

    public static <T> ArrayList<T> newArrayList(Collection<? extends T> c) {
        return new ArrayList<>(c);
    }
}

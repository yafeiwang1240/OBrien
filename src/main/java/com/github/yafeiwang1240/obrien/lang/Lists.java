package com.github.yafeiwang1240.obrien.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * List 生成工具
 */
public class Lists {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    public static <T> ArrayList<T> newArrayList(int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    public static <T> ArrayList<T> newArrayList(Collection<? extends T> c) {
        return new ArrayList<>(c);
    }
}

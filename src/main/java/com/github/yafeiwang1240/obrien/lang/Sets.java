package com.github.yafeiwang1240.obrien.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Sets {

    public static <T> T create(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    public static <T> Set<T> newHashSet(final int initialCapacity) {
        return new HashSet<>(initialCapacity);
    }

    public static <T> Set<T> newHashSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}

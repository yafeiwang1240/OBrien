package com.githup.yafeiwang1240.obrien.lang;

import java.util.HashSet;
import java.util.Set;

public class Sets {

    public static <T> T create(Supplier<T> supplier) {
        return supplier.get();
    }

    public static <T> Set<T> newHashSet(final int initialCapacity) {
        return new HashSet<>(initialCapacity);
    }
}

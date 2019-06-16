package com.githup.yafeiwang1240.obrien.lang;

public class Maps {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

}

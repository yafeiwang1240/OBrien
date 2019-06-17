package com.githup.yafeiwang1240.obrien.lang;

/**
 * Map生成工具
 */
public class Maps {

    public static <T> T create(final Supplier<T> supplier) {
        return supplier.get();
    }

}

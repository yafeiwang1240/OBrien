package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;

public class ValueInitialized<T> implements Initialized<T> {

    private boolean cover;
    private T value;

    public ValueInitialized(T value) {
        this(value, false);
    }

    public ValueInitialized(T value, boolean cover) {
        this.cover = cover;
        this.value = value;
    }

    @Override
    public T execute(T data) {
        if (cover || data == null || data.toString().trim().length() == 0) {
            return this.value;
        }
        return data;
    }
}

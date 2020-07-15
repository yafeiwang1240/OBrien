package com.github.yafeiwang1240.obrien.initialization;

import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;

import java.lang.reflect.Field;

public interface Initialized<T> {
    T execute(T data) throws InitializedFailedException;
    default void init(Field field) {}
}

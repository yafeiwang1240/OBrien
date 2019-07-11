package com.github.yafeiwang1240.obrien.initialization;

import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;

public interface Initialized<T> {
    T execute(T data) throws InitializedFailedException;
}

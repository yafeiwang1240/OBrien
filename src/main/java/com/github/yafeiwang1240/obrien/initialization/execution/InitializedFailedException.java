package com.github.yafeiwang1240.obrien.initialization.execution;

/**
 * 初始化失败
 */
public class InitializedFailedException extends Exception {
    public InitializedFailedException(String message) {
        super(message);
    }
    public InitializedFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

package com.github.yafeiwang1240.obrien.lang.exception;

/**
 * 反序列化类型异常
 */
public class CastTypeException extends Exception {

    public CastTypeException() {
        super();
    }

    public CastTypeException(String message) {
        super(message);
    }

    public CastTypeException(Throwable throwable) {
        super(throwable);
    }
}

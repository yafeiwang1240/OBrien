package com.github.yafeiwang1240.obrien.uitls.exception;

/**
 * 序列化异常
 */
public class SerializationException extends Exception {
    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable throwable) {
        super(throwable);
    }
}

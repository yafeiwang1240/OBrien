package com.github.yafeiwang1240.obrien.fastreflect.exception;

public class ReflectClassException extends Exception {
    public ReflectClassException(String msg) {
        super(msg);
    }
    public ReflectClassException(String msg, Throwable t) {
        super(msg, t);
    }
}

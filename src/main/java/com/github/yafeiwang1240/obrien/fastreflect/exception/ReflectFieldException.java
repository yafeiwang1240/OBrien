package com.github.yafeiwang1240.obrien.fastreflect.exception;

public class ReflectFieldException extends Exception {
    public ReflectFieldException(String msg) {
        super(msg);
    }
    public ReflectFieldException(String msg, Throwable t) {
        super(msg, t);
    }
}

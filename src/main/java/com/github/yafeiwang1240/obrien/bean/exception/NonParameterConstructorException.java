package com.github.yafeiwang1240.obrien.bean.exception;


public class NonParameterConstructorException extends Exception {

    public NonParameterConstructorException(String msg) {
        super(msg);
    }

    public NonParameterConstructorException(String msg, Throwable t) {
        super(msg, t);
    }
}

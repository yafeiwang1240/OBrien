package com.github.yafeiwang1240.obrien.fsm;

public class UnSupportedEventException extends Exception {
    public UnSupportedEventException() {
        super();
    }
    public UnSupportedEventException(String message) {
        super(message);
    }
}

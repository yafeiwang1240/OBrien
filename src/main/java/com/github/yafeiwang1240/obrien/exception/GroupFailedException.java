package com.github.yafeiwang1240.obrien.exception;

public class GroupFailedException extends Exception {
    public GroupFailedException() {
        super();
    }

    public GroupFailedException(String message) {
        super(message);
    }

    public GroupFailedException(Throwable throwable) {
        super(throwable);
    }
}

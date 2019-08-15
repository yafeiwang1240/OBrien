package com.github.yafeiwang1240.obrien.bean.exception;

public class FieldTransferException extends Exception {
    public FieldTransferException(String msg) {
        super(msg);
    }
    public FieldTransferException(String msg, Throwable t) {
        super(msg, t);
    }
}

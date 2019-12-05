package com.github.yafeiwang1240.obrien.template.exception;

/**
 * 表达式渲染异常
 *
 * @author wangyafei
 */
public class GenerateRuntimeException extends RuntimeException {

    public GenerateRuntimeException(String msg) {
        super(msg);
    }

    public GenerateRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
}

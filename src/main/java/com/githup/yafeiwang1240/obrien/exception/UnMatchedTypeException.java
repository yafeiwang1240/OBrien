package com.githup.yafeiwang1240.obrien.exception;

/**
 * 类型异常信息
 */
public class UnMatchedTypeException extends Exception {
    private String name;
    private Class requiredType;
    private Class actualType;

    public UnMatchedTypeException(String name, Class requiredType, Class actualType) {
        this.actualType = actualType;
        this.requiredType = requiredType;
        this.name = name;
    }

    public UnMatchedTypeException(UnMatchedTypeException clone) {
        this.actualType = clone.getActualType();
        this.requiredType = clone.getRequiredType();
        this.name = clone.getName();
    }

    public String getName() {
        return name;
    }

    public Class getRequiredType() {
        return requiredType;
    }

    public Class getActualType() {
        return actualType;
    }
}
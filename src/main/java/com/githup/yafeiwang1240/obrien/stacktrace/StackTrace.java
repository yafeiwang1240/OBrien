package com.githup.yafeiwang1240.obrien.stacktrace;

/**
 * 堆栈信息
 */
public class StackTrace {

    private String clazzName;
    private String methodName;

    public StackTrace(String clazzName, String methodName) {
        this.clazzName = clazzName;
        this.methodName = methodName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String getMethodName() {
        return methodName;
    }
}

package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.uitls.stacktrace.StackTrace;

import java.lang.reflect.Method;

public class StackTraceUtils {

    public static StackTrace FrontStackTrace(Class clazz, Method method) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        int index = 0;
        for(int i = 0; i < stacks.length; i++) {
            StackTraceElement stack = stacks[i];
            if(stack.getClassName().equals(clazz.getName()) && method.getName().equals(stack.getMethodName())) {
                index = i;
                break;
            }
        }
        if(index <= 0) {
            return null;
        }
        return new StackTrace(stacks[index + 1].getClassName(), stacks[index + 1].getMethodName());
    }
}

package com.github.yafeiwang1240.obrien.uitls.stacktrace;

import com.github.yafeiwang1240.obrien.uitls.stacktrace.annotation.BeanRequest;
import com.github.yafeiwang1240.obrien.uitls.stacktrace.annotation.MethodRequest;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.uitls.StackTraceUtils;

import java.lang.reflect.Method;

/**
 * 校验器
 */
public class VerificationUtils {
    /**
     * 堆栈校验
     * @param from
     * @return
     */
    public static VerificationResult validStack(Class from, Class clazz, Method method) {
        VerificationResult result = new VerificationResult();
        if (from.getDeclaredAnnotation(BeanRequest.class) == null) {
            result.setStatus(VerificationResult.Status.FAILED);
            result.setMessages(Lists.asList("object must with annotation " + BeanRequest.class.getName()));
            return result;
        }
        Method methodFind = null;
        Method[] methods = from.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getDeclaredAnnotation(MethodRequest.class) != null) {
                methodFind = m;
                break;
            }
        }
        if (methodFind != null) {
            StackTrace stackTrace = StackTraceUtils.FrontStackTrace(clazz, method);
            if (methodFind.getName().equals(stackTrace.getMethodName()) && stackTrace.getClazzName().equals(from.getName())) {
                result.setStatus(VerificationResult.Status.SUCCEED);
                result.setMessages(Lists.asList(from.getName(), methodFind.getName()));
                return result;
            } else {
                result.setStatus(VerificationResult.Status.FAILED);
                result.setMessages(Lists.asList("object and method must is Caller"));
                return result;
            }
        }
        result.setStatus(VerificationResult.Status.FAILED);
        result.setMessages(Lists.asList("object methods must with annotation " + MethodRequest.class.getName()));
        return result;
    }
}

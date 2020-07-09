package com.github.yafeiwang1240.obrien.system;

import com.github.yafeiwang1240.obrien.lang.Lists;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简化Process代码结构
 * @author wangyafei
 */
public class CloseProxyFactory {

    public static <T> T getCloseProxy(T proxy, String... closeMethods) throws NoSuchMethodException {
        return (T) Proxy.newProxyInstance(proxy.getClass().getClassLoader(), getInterfacesWithClose(proxy.getClass()), new CloseHandler(proxy, closeMethods));
    }

    private static Class<?>[] getInterfacesWithClose(Class<?> clazz) {
        Class<?>[] classes = clazz.getInterfaces();
        Class<?>[] result;
        if (classes != null && classes.length > 0) {
            result = Arrays.copyOf(classes, classes.length + 1);
            result[classes.length] = Closeable.class;
        } else {
            result = new Class[]{Closeable.class};
        }
        return result;
    }

    public static class CloseHandler implements InvocationHandler {

        private Object proxy;

        private List<Method> closes = new ArrayList<>();

        private static List<String> DEFAULT_CLOSES = Lists.asList("close", "destroy", "stop", "shutdown");

        public CloseHandler(Object proxy, String... closeMethods) throws NoSuchMethodException {
            if (closeMethods != null && closeMethods.length > 0) {
                for (String method : closeMethods) {
                    closes.add(proxy.getClass().getDeclaredMethod(method));
                }
            } else {
                for (String method : DEFAULT_CLOSES) {
                    try {
                        closes.add(proxy.getClass().getDeclaredMethod(method));
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
            this.proxy = proxy;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("close")) {
                for (Method closeMethod : closes) {
                    closeMethod.invoke(proxy);
                }
            }
            return method.invoke(proxy, args);
        }
    }
}

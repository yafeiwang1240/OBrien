package com.github.yafeiwang1240.obrien.lang;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 对java.lang.Math补充
 * {@link java.lang.Math}
 *
 * @author wangyafei
 */
public final class Maths {

    private static Map<Class<?>, Field> valueCache = new HashMap<>();

    /**
     * swap number1 number2
     * @param num1
     * @param num2
     * @param <T>
     * @throws NoValueFieldException
     * @throws ObtainFieldValueException
     * @throws AttachFieldValueException
     */
    public static <T extends Object> void swap(T num1, T num2) throws NoValueFieldException, ObtainFieldValueException, AttachFieldValueException {
        Field field = null;
        if (valueCache.containsKey(num1.getClass())) {
            field = valueCache.get(num1.getClass());
        } else {
            field = getAndCacheValueField(num1.getClass());
        }
        Object v1;
        Object v2;
        try {
            v1 = field.get(num1);
            v2 = field.get(num2);
        } catch (IllegalAccessException e) {
            throw new ObtainFieldValueException(e.getMessage(), e);
        }
        try {
            field.set(num1, v2);
            field.set(num2, v1);
        } catch (IllegalAccessException e) {
            throw new AttachFieldValueException(e.getMessage(), e);
        }
    }

    public static class NoValueFieldException extends Exception {
        public NoValueFieldException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    public static class AttachFieldValueException extends Exception {
        public AttachFieldValueException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    public static class ObtainFieldValueException extends Exception {
        public ObtainFieldValueException(String msg, Throwable t) {
            super(msg, t);
        }
    }

    private synchronized static Field getAndCacheValueField(Class<? extends Object> aClass) throws NoValueFieldException {
        if (valueCache.containsKey(aClass)) return valueCache.get(aClass);
        try {
            Field field = aClass.getDeclaredField("value");
            field.setAccessible(true);
            valueCache.put(aClass, field);
            return field;
        } catch (NoSuchFieldException e) {
            throw new NoValueFieldException(e.getMessage(), e);
        }
    }

    /**
     * 向上取整
     * @param dividend 被除数
     * @param divisor 除数
     * @return
     */
    public static int roundup(int dividend, int divisor) {
        return (dividend + divisor - 1) / divisor;
    }

    /**
     * 向下取整
     * @param dividend
     * @param divisor
     * @return
     */
    public static int floor(int dividend, int divisor) {
        return dividend / divisor;
    }

}

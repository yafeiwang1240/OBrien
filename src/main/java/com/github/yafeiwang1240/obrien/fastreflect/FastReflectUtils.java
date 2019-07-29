package com.github.yafeiwang1240.obrien.fastreflect;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectClassException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectFieldException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectMethodException;
import com.github.yafeiwang1240.obrien.fastreflect.pack.FastReflectPack;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射优化工具
 * @author wangyafei
 * @date 2019-07-29
 */
public class FastReflectUtils {

    private static final Map<Class, FastReflectPack> fastReflectPackCache;

    static {
        fastReflectPackCache = Maps.create(ConcurrentHashMap::new);
    }

    /**
     * 获得方法列表（包含父类方法, 如果覆盖父类方法仅获得子类的方法）
     * @param clazz
     * @return
     * @throws ReflectClassException
     */
    public static List<Method> methods(Class<?> clazz) throws ReflectClassException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getMethods();
    }

    /**
     * 获取方法 （算法复杂度 > O(1) ）
     * @param clazz
     * @param methodName
     * @param paramTypes
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static Method method(Class<?> clazz, String methodName, Class<?>[] paramTypes) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getMethod(methodName, paramTypes);
    }

    /**
     * 获得属性列表（包含父类属性, 如果覆盖父类属性仅获得子类的属性）
     * @param clazz
     * @return
     * @throws ReflectClassException
     */
    public static List<Field> fields(Class<?> clazz) throws ReflectClassException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getFields();
    }

    /**
     * 获取属性
     * @param clazz
     * @param fieldName
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static Field field(Class<?> clazz, String fieldName) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getField(fieldName);
    }

    /**
     * 设置属性值
     * @param o
     * @param fieldName
     * @param value
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static void setFieldValue(Object o, String fieldName, Object value) throws ReflectClassException, ReflectFieldException {
        Class _clazz;
        Object _o = null;
        if (o instanceof Class<?>) {
            _clazz = (Class) o;
        } else {
            _clazz = o.getClass();
            _o = o;
        }
        FastReflectPack pack = getFastReflectPack(_clazz);
        pack.setFiledValue(_o, fieldName, value);
    }

    /**
     * 获得对象属性值
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object o, String fieldName) throws ReflectClassException, ReflectFieldException {
        Object _o = null;
        Class _clazz;
        if (o instanceof Class<?>) {
            _clazz = (Class) o;
        } else {
            _clazz = o.getClass();
            _o = o;
        }
        FastReflectPack pack = getFastReflectPack(_clazz);
        return pack.getFiledValue(_o, fieldName);
    }

    /**
     * 执行无参方法
     * @param o
     * @param methodName
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     * @throws ReflectMethodException
     */
    public static Object methodInvoke(Object o, String methodName) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        Object _o = null;
        Class _clazz;
        if (o instanceof Class<?>) {
            _clazz = (Class) o;
        } else {
            _clazz = o.getClass();
            _o = o;
        }
        FastReflectPack pack = getFastReflectPack(_clazz);
        return pack.methodInvoke(_o, methodName, null, null);
    }

    /**
     * 执行弱匹配方法
     * @param o
     * @param methodName
     * @param args
     * @return
     */
    public static Object methodInvoke(Object o, String methodName, Object[] args) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        Object _o = null;
        Class _clazz;
        if (o instanceof Class<?>) {
            _clazz = (Class) o;
        } else {
            _clazz = o.getClass();
            _o = o;
        }
        FastReflectPack pack = getFastReflectPack(_clazz);
        return pack.methodInvoke(_o, methodName, args, null);
    }

    /**
     * 执行强匹配方法
     * @param o
     * @param methodName
     * @param args
     * @param paramTypes
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     * @throws ReflectMethodException
     */
    public static Object methodInvoke(Object o, String methodName, Object[] args, Class<?>[] paramTypes) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        Object _o = null;
        Class _clazz;
        if (o instanceof Class<?>) {
            _clazz = (Class) o;
        } else {
            _clazz = o.getClass();
            _o = o;
        }
        FastReflectPack pack = getFastReflectPack(_clazz);
        return pack.methodInvoke(_o, methodName, args, paramTypes);
    }

    private static FastReflectPack getFastReflectPack(Class<?> clazz) throws ReflectClassException {
        if (clazz == null) return null;
        FastReflectPack pack;
        if (fastReflectPackCache.containsKey(clazz)) {
            pack = fastReflectPackCache.get(clazz);
        } else {
            pack = getAndCacheFastReflectPack(clazz);
        }
        return pack;
    }

    private synchronized static FastReflectPack getAndCacheFastReflectPack(Class clazz) throws ReflectClassException {
        if (fastReflectPackCache.containsKey(clazz)) {
            return fastReflectPackCache.get(clazz);
        }
        FastReflectPack pack = new FastReflectPack(clazz);
        fastReflectPackCache.put(clazz, pack);
        return pack;
    }

}

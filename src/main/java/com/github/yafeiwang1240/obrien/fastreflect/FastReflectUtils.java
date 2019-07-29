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
     * 设置属性值
     * @param o
     * @param fieldName
     * @param value
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static void setFieldValue(Object o, String fieldName, Object value) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        pack.setFiledValue(o, fieldName, value);
    }

    /**
     * 获得对象属性值
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object o, String fieldName) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.getFiledValue(o, fieldName);
    }

    /**
     * 获取静态属性值
     * @param clazz
     * @param fieldName
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static Object getFieldValue(Class<?> clazz, String fieldName) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getFiledValue(null, fieldName);
    }

    /**
     * 执行静态无参方法
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object methodInvoke(Class<?> clazz, String methodName) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.methodInvoke(null, methodName, null, null);
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
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(o, methodName, null, null);
    }

    /**
     * 执行弱匹配方法
     * @param o
     * @param methodName
     * @param args
     * @return
     */
    public static Object methodInvoke(Object o, String methodName, Object[] args) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(null, methodName, args, null);
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
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(null, methodName, args, paramTypes);
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

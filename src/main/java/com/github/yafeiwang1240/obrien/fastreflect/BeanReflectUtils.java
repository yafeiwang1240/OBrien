package com.github.yafeiwang1240.obrien.fastreflect;

import com.github.yafeiwang1240.obrien.fastreflect.model.FieldAndMethodModel;
import com.github.yafeiwang1240.obrien.fastreflect.pack.BeanReflectPack;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * bean反射工具
 */
public class BeanReflectUtils {

    private static Map<Class<?>, BeanReflectPack> beanReflectPackCache = Maps.newHashMap();

    /**
     *  获取属性及其set、get方法
     * @param fieldName
     * @param clazz
     * @return
     */
    public static FieldAndMethodModel getFieldAndMethod(String fieldName, Class<?> clazz) {
        return getBeanReflectPack(clazz).getFieldAndMethod(fieldName);
    }

    /**
     * 获取所有属性、方法
     * @param clazz
     * @return
     */
    public static Collection<FieldAndMethodModel> getFieldAndMethods(Class<?> clazz) {
        return getBeanReflectPack(clazz).getFieldAndMethods();
    }

    /**
     * 获取类属性及其set、get方法
     * @param clazz
     * @return
     */
    public static BeanReflectPack getBeanReflectPack(Class<?> clazz) {
        if (beanReflectPackCache.containsKey(clazz)) return beanReflectPackCache.get(clazz);
        return getAndCacheBeanReflectPack(clazz);
    }

    private synchronized static BeanReflectPack getAndCacheBeanReflectPack(Class<?> clazz) {
        if (beanReflectPackCache.containsKey(clazz)) return beanReflectPackCache.get(clazz);
        BeanReflectPack pack = new BeanReflectPack(clazz);
        beanReflectPackCache.put(clazz, pack);
        return pack;
    }
}

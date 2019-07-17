package com.github.yafeiwang1240.obrien.template;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 此接口必须在实现类中第一个接口位置出现
 * @param <T>
 */
public interface InterfaceTemplateClass<T> {
    int position = 0;
    // 获取泛型类
    default Type[] tClass() {
        Type[] type = getClass().getGenericInterfaces();
        Type[] types = ((ParameterizedType) type[position]).getActualTypeArguments();
        return types;
    }
}

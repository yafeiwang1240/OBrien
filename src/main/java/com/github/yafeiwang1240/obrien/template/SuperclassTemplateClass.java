package com.github.yafeiwang1240.obrien.template;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取继承当前类的泛型.class
 */
public abstract class SuperclassTemplateClass<T> {

    // 获取泛型类
    public Type[] tClass() {
        Type type = getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        return types;
    }
}

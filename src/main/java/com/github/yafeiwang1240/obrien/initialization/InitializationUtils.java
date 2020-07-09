package com.github.yafeiwang1240.obrien.initialization;

import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedMeans;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedMethod;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.initialization.impl.AbstractInitializedCreate;
import com.github.yafeiwang1240.obrien.initialization.model.InitializePack;
import com.github.yafeiwang1240.obrien.lang.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化操作
 */
public class InitializationUtils {

    private static Logger logger = LoggerFactory.getLogger(InitializationUtils.class);

    private static Map<Class, InitializePack> initializeCache = Maps.create(ConcurrentHashMap::new);

    public static void initialize(Object o) throws InitializedFailedException {
        initialize("", o);
    }

    public static void initialize(String group, Object o) throws InitializedFailedException {
        if (o == null) return;
        Class clazz = o.getClass();
        InitializePack pack;
        if (initializeCache.containsKey(clazz)) {
            pack = initializeCache.get(clazz);
        } else {
            pack = getAndCacheInitializer(clazz);
        }
        pack.initialize(group, o);
    }

    private synchronized static InitializePack getAndCacheInitializer(Class<?> clazz) throws InitializedFailedException {
        if (initializeCache.containsKey(clazz)) return initializeCache.get(clazz);
        InitializePack pack = new InitializePack();
        initializeCache.put(clazz, pack);
        Class<?> _clazz = clazz;
        while (_clazz != null) {
            // 属性初始化
            Field[] fields = _clazz.getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().isAnnotationPresent(InitializedMeans.class)) {
                            InitializedMeans means = annotation.annotationType().getAnnotation(InitializedMeans.class);
                            try {
                                AbstractInitializedCreate create = means.value().getConstructor(annotation.annotationType()).newInstance(annotation);
                                pack.addFieldInitializer(field, create);
                            } catch (Exception e) {
                                throw new InitializedFailedException("初始化逻辑建造器创建失败", e);
                            }
                        }
                    }
                }
            }

            // 初始化方法
            Method[] methods = _clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getDeclaredAnnotation(InitializedMethod.class) != null) {
                    pack.addMethodInitializer(method);
                }
            }

            // 类初始化
            if (_clazz.isAnnotationPresent(Initializer.class)){
                Initializer initializer = _clazz.getDeclaredAnnotation(Initializer.class);
                pack.setInitialized(initializer);
            }
            // 父类
            _clazz = _clazz.getSuperclass();
        }

        return pack;
    }
}

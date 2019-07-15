package com.github.yafeiwang1240.obrien.initialization;

import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedMethod;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.initialization.model.InitializePack;
import com.github.yafeiwang1240.obrien.lang.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
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
        Class _clazz = clazz;
        while (_clazz != null) {
            Field[] fields = _clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getDeclaredAnnotation(Initializer.class) != null) {
                    pack.addFieldInitializer(field);
                }
            }
            Method[] methods = _clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getDeclaredAnnotation(InitializedMethod.class) != null) {
                    pack.addMethodInitializer(method);
                }
            }
            _clazz = _clazz.getSuperclass();
        }

        if (clazz.isAnnotationPresent(Initializer.class)){
            try {
                pack.setInitialized(clazz.getDeclaredAnnotation(Initializer.class).group(), clazz.getDeclaredAnnotation(Initializer.class).initialization().newInstance());
            } catch (InstantiationException e) {
                throw new InitializedFailedException(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new InitializedFailedException(e.getMessage(), e);
            }
        }

        return pack;
    }
}

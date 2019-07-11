package com.github.yafeiwang1240.obrien.initialization;

import com.github.yafeiwang1240.obrien.initialization.annotation.InitializeMethod;
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
        if (o == null) return;
        Class clazz = o.getClass();
        InitializePack pack;
        if (initializeCache.containsKey(clazz)) {
            pack = initializeCache.get(clazz);
        } else {
            pack = getAndCacheInitializer(clazz);
        }
        pack.initialize(o);
    }

    private synchronized static InitializePack getAndCacheInitializer(Class clazz) throws InitializedFailedException {
        if (initializeCache.containsKey(clazz)) return initializeCache.get(clazz);
        Field[] fields = clazz.getDeclaredFields();
        InitializePack pack = new InitializePack();
        for (Field field : fields) {
            if (field.getAnnotation(Initializer.class) != null) {
                pack.addFieldInitializer(field);
            }
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(InitializeMethod.class) != null) {
                pack.addMethodInitializer(method);
            }
        }
        return pack;
    }
}

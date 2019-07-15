package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedMethod;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InitializePack {

    private Map<String, List<FieldInitializedExecutor>> fieldInitializerCache = Maps.create(ConcurrentHashMap::new);

    private Map<String, List<Method>> methodInitializerCache = Maps.create(ConcurrentHashMap::new);

    private Map<String, Initialized> initializedCache = Maps.create(ConcurrentHashMap::new);

    public void addFieldInitializer(Field f) throws InitializedFailedException {
        Initializer annotation = f.getDeclaredAnnotation(Initializer.class);
        String group = annotation.group();
        if(annotation == null) {
            return;
        }
        try {
            List<FieldInitializedExecutor> fieldInitializer;
            if ((fieldInitializer = fieldInitializerCache.get(group)) == null) {
                fieldInitializer = Lists.create(ArrayList::new);
                fieldInitializerCache.put(group, fieldInitializer);
            }
            fieldInitializer.add(new FieldInitializedExecutor(f, annotation.initialization().newInstance()));
        } catch (InstantiationException e) {
            throw new InitializedFailedException("初始化实例过程失败", e);
        } catch (IllegalAccessException e) {
            throw new InitializedFailedException("初始化非法访问", e);
        }
    }

    public void addMethodInitializer(Method m) {
        m.setAccessible(true);
        InitializedMethod annotation = m.getDeclaredAnnotation(InitializedMethod.class);
        String group = annotation.group();
        List<Method> methodInitializer;
        if ((methodInitializer = methodInitializerCache.get(group)) == null) {
            methodInitializer = Lists.create(ArrayList::new);
            methodInitializerCache.put(group, methodInitializer);
        }
        methodInitializer.add(m);
    }

    public void setInitialized(Initialized initialized) {
        setInitialized("", initialized);
    }

    public void setInitialized(String group, Initialized initialized) {
        this.initializedCache.put(group, initialized);
    }

    public void initialize(String group, Object o) throws InitializedFailedException{
        List<FieldInitializedExecutor> fieldInitializer;
        if ((fieldInitializer = fieldInitializerCache.get(group)) != null) {
            for (FieldInitializedExecutor _value : fieldInitializer) {
                _value.process(o);
            }
        }
        List<Method> methodInitializer;
        if ((methodInitializer = methodInitializerCache.get(group)) != null) {
            methodInitializer.forEach(v -> {
                try {
                    v.invoke(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }
        Initialized initialized = initializedCache.get(group);
        if (initialized != null) initialized.execute(o);
    }
}

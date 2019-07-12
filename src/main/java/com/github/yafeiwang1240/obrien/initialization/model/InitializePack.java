package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
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

    private Map<Field, Initialized> fieldInitializer = Maps.create(ConcurrentHashMap::new);

    private List<Method> methodInitializer = Lists.create(ArrayList::new);

    public void addFieldInitializer(Field f) throws InitializedFailedException {
        f.setAccessible(true);
        Initializer annotation = f.getAnnotation(Initializer.class);
        if(annotation == null) {
            return;
        }
        try {
            fieldInitializer.put(f, annotation.initialization().newInstance());
        } catch (InstantiationException e) {
            throw new InitializedFailedException("初始化实例过程失败", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InitializedFailedException("初始化非法访问", e);
        }
    }

    public void addMethodInitializer(Method m) {
        m.setAccessible(true);
        methodInitializer.add(m);
    }

    public void initialize(Object o) throws InitializedFailedException{
        for (Map.Entry<Field, Initialized> entry : fieldInitializer.entrySet()) {
           try {
               Field field = entry.getKey();
               field.set(o, entry.getValue().execute(field.get(o)));
           } catch (IllegalAccessException e) {
               throw new InitializedFailedException("初始化非法访问", e);
           }
        }
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
}

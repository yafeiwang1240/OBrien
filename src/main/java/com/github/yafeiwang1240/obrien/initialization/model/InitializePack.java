package com.github.yafeiwang1240.obrien.initialization.model;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedMethod;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializerGroup;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.initialization.impl.AbstractInitializedCreate;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class InitializePack {

    private Map<String, List<FieldInitializedExecutor>> fieldInitializerCache = Maps.create(ConcurrentHashMap::new);

    private Map<String, List<Method>> methodInitializerCache = Maps.create(ConcurrentHashMap::new);

    private Map<String, List<Initialized>> initializedCache = Maps.create(ConcurrentHashMap::new);

    public void addFieldInitializer(Field f, AbstractInitializedCreate create) throws InitializedFailedException {
        Map<String, List<Initialized>> stringListMap = create.getInitExecutor();
        if (!stringListMap.isEmpty()) {
            for (Map.Entry<String, List<Initialized>> entry : stringListMap.entrySet()) {
                List<FieldInitializedExecutor> executors;
                if ((executors = fieldInitializerCache.get(entry.getKey())) == null) {
                    executors = new ArrayList<>();
                    fieldInitializerCache.put(entry.getKey(), executors);
                }
                for (Initialized initialized : entry.getValue()) {
                    initialized.init(f);
                    executors.add(new FieldInitializedExecutor(f, initialized));
                }
            }
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

    public void setInitialized(Initializer Initializer) throws InitializedFailedException {
        initializedCache.putAll(getInitExecutor(Initializer));
    }

    public void initialize(String group, Object o) throws InitializedFailedException {
        List<FieldInitializedExecutor> fieldInitializer;
        if ((fieldInitializer = fieldInitializerCache.get(group)) != null) {
            for (FieldInitializedExecutor _value : fieldInitializer) {
                _value.process(o);
            }
        }
        List<Method> methodInitializer;
        if ((methodInitializer = methodInitializerCache.get(group)) != null) {
            for (Method method : methodInitializer) {
                try {
                    method.invoke(o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InitializedFailedException(e.getMessage(), e);
                }
            }
        }
        List<Initialized> initializeds = initializedCache.get(group);
        if (initializeds != null && initializeds.size() > 0) {
            for (Initialized value : initializeds) {
                value.execute(o);
            }
        }
    }

    public static Map<String, List<Initialized>> getInitExecutor(Initializer annotation) throws InitializedFailedException {
        Map<String, List<Initialized>> result = new HashMap<>();
        try {
            Class<? extends Initialized>[] initializations = annotation.initializations();
            for (Class<? extends Initialized> clazz : initializations) {
                InitializerGroup initializerGroup = clazz.getDeclaredAnnotation(InitializerGroup.class);
                if (initializerGroup == null) {
                    throw new InitializedFailedException("初始化类添加注解：" + InitializerGroup.class.getName());
                }
                List<Initialized> initializeds;
                if ((initializeds = result.get(initializerGroup.group())) == null) {
                    initializeds = Lists.create(ArrayList::new);
                    result.put(initializerGroup.group(), initializeds);
                }
                initializeds.add(clazz.newInstance());
            }
        } catch (InstantiationException e) {
            throw new InitializedFailedException("初始化实例过程失败", e);
        } catch (IllegalAccessException e) {
            throw new InitializedFailedException("初始化非法访问", e);
        }
        return result;
    }
}

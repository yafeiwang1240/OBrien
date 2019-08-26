package com.github.yafeiwang1240.obrien.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * application bean context
 */
public class ApplicationContext {

    private static ApplicationContext instance;

    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (ApplicationContext.class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

    private Map<Class<?>, Object> beanCache = new HashMap<>();

    public <T> T getBean(Class<T> requiredType) {
        if (!beanCache.containsKey(requiredType)) {
            return null;
        }
        return (T) beanCache.get(requiredType);
    }

    public void setBean(Object bean) {
        if (bean == null) {
            return;
        }
        beanCache.put(bean.getClass(), bean);
    }
}

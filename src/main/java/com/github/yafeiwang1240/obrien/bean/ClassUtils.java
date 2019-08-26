package com.github.yafeiwang1240.obrien.bean;

import com.github.yafeiwang1240.obrien.bean.exception.NoSuchClassException;
import com.github.yafeiwang1240.obrien.bean.exception.NonParameterConstructorException;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * classpath.* utils
 */
public class ClassUtils {

    private static final ApplicationBeanContext beanContext = new ApplicationBeanContext();

    private static final Map<String, Class<?>> classCache = new HashMap<>();

    static {
        URL root = ClassUtils.class.getResource("/");
        final ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        final ClassLoader classLoader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
        File file = new File(root.getFile());
        putClass(file, "", classLoader);
    }

    private static void putClass(File file, String pack, ClassLoader classLoader) {
        File[] files = file.listFiles();
        for (File f : files) {
            String name = f.getName();
            String packageName = "";
            if (pack != null && !pack.equals("")) {
                packageName = pack + ".";
            }
            if (f.isDirectory()) {
                putClass(f, packageName + name, classLoader);
            } else {
                if (name.endsWith(".class")) {
                    Class<?> clazz;
                    packageName += name.substring(0, name.length() - 6);
                    try {
                        clazz = Class.forName(packageName, true, classLoader);
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                    classCache.put(packageName, clazz);
                }
            }
        }
    }

    /**
     * 根据class获取实例
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) throws NonParameterConstructorException {
        T result = beanContext.getBean(requiredType);
        if (result == null) {
            result = getAndCacheBean(requiredType);
        }
        return result;
    }

    /**
     * 根据package.BeanName获取实例
     * @param name
     * @param <T>
     * @return
     * @throws NoSuchClassException
     * @throws NonParameterConstructorException
     */
    public static <T> T getBean(String name) throws NoSuchClassException, NonParameterConstructorException {
        Class<T> clazz = (Class<T>) classCache.get(name);
        if (clazz == null) {
            throw new NoSuchClassException(name);
        }
        return getBean(clazz);
    }

    public static Class<?> getClass(String name) {
        return classCache.get(name);
    }

    private synchronized static <T> T getAndCacheBean(Class<T> requiredType) throws NonParameterConstructorException {
        T result = beanContext.getBean(requiredType);
        if (result != null) return result;
        try {
            result = requiredType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NonParameterConstructorException(e.getMessage(), e);
        }
        beanContext.setBean(result);
        return result;
    }

}

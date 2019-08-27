package com.github.yafeiwang1240.obrien.bean;

import com.github.yafeiwang1240.obrien.bean.exception.NoSuchClassException;
import com.github.yafeiwang1240.obrien.bean.exception.NonParameterConstructorException;
import com.github.yafeiwang1240.obrien.lang.Lists;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * classpath.* utils
 */
public class ClassUtils {

    private static final ApplicationContext beanContext = new ApplicationContext();

    private static final Map<String, Class<?>> classCache = new HashMap<>();

    private static final Map<String, List<Class<?>>> packageClassCache = new HashMap<>();

    private static Map<Class<?>, List<Class<?>>> assignCache = new HashMap<>();

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
    /**
     * 根据package.BeanName获取class
     * @param name
     * @return
     */
    public static Class<?> getClass(String name) {
        return classCache.get(name);
    }

    /**
     * 根据packageName获取包下class
     * @param packageName
     * @return
     */
    public static List<Class<?>> getPackageClasses(String packageName) {
        List<Class<?>> result = null;
        if ((result = packageClassCache.get(packageName)) == null) {
            result = getAndCachePackageClasses(packageName);
        }
        return new ArrayList<>(result);
    }

    /**
     * 获取此类及其继承（实现类），（可能）包含自身
     * @param assignClass
     * @param <T>
     * @return
     */
    public static <T> List<Class<T>> getAssignClasses(Class<T> assignClass) {
        List<Class<?>> assign;
        if ((assign = assignCache.get(assignClass)) == null) {
            assign = getAndCacheAssignClasses(assignClass);
        }
        List<Class<T>> result = Lists.newArrayList(assign.size());
        for (Class<?> clazz : assign) {
            result.add((Class<T>) clazz);
        }
        return result;
    }

    private synchronized static <T> List<Class<?>> getAndCacheAssignClasses(Class<T> assignClass) {
        if (assignCache.containsKey(assignClass)) return assignCache.get(assignClass);
        List<Class<?>> classes = Lists.newArrayList();
        for (Map.Entry<String, Class<?>> entry : classCache.entrySet()) {
            if (assignClass.isAssignableFrom(entry.getValue())) {
                classes.add(entry.getValue());
            }
        }
        return classes;
    }

    private synchronized static List<Class<?>> getAndCachePackageClasses(String packageName) {
        if (packageClassCache.containsKey(packageName)) {
            return packageClassCache.get(packageName);
        }
        List<Class<?>> list = new ArrayList<>();
        packageClassCache.put(packageName, list);
        for (Map.Entry<String, Class<?>> entry : classCache.entrySet()) {
            if (entry.getKey().startsWith(packageName)) {
                list.add(entry.getValue());
            }
        }
        return list;
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

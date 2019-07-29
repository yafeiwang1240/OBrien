# OBrien
### java bean 封装
### 已实现连接池自动分配算法
### 有限状态转换机
### 校验机制

```java
package com.github.yafeiwang1240.obrien.validation;

import com.github.yafeiwang1240.obrien.lang.Maps;
import com.github.yafeiwang1240.obrien.validation.annotation.ValidateRule;
import com.github.yafeiwang1240.obrien.validation.exception.ValidateInitialException;
import com.github.yafeiwang1240.obrien.validation.impl.AbstractValidator;
import com.github.yafeiwang1240.obrien.validation.model.ValidatePack;
import com.github.yafeiwang1240.obrien.validation.model.ValidateResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidationUtils {

    protected static Map<Class, ValidatePack> validatorCache = Maps.create(ConcurrentHashMap::new);

    public static ValidateResult validate(Object o) throws ValidateInitialException {
        return validate("", o);
    }

    public static ValidateResult validate(String group, Object o) throws ValidateInitialException {
        ValidateResult result = new ValidateResult();
        result.setStatus(ValidateResult.Status.SUCCESS);
        if( null == o ) return result;
        ValidatePack validatePack = null;
        if(validatorCache.containsKey(o.getClass())) {
            validatePack = validatorCache.get(o.getClass());
        } else {
            try {
                validatePack = getAndCacheValidator(o.getClass());
            } catch (NoSuchMethodException e) {
                throw new ValidateInitialException("校验类没有提供带参数的构造方法！", e);
            } catch (InstantiationException e) {
                throw new ValidateInitialException("校验类构造方法访问失败！", e);
            } catch (InvocationTargetException e) {
                throw new ValidateInitialException("校验类访问异常！", e);
            } catch (IllegalAccessException e) {
                throw new ValidateInitialException("校验类访问异常！", e);
            }
        }
        return validatePack.validate(group, o);
    }

    private static synchronized ValidatePack getAndCacheValidator(Class<?> clazz) throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
        if(validatorCache.containsKey(clazz)) return validatorCache.get(clazz);
        ValidatePack pack = new ValidatePack();
        Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
        for(Annotation annotation : classAnnotations) {
            if(annotation.annotationType().isAnnotationPresent(ValidateRule.class)) {
                ValidateRule validateRule = annotation.annotationType().getAnnotation(ValidateRule.class);
                AbstractValidator validator = validateRule.value().getConstructor(annotation.annotationType()).newInstance(annotation);
                pack.addClassValidator(getAnnotationGroup(annotation), validator);
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if(annotations != null && annotations.length > 0) {
                for(Annotation annotation : annotations) {
                    if(annotation.annotationType().isAnnotationPresent(ValidateRule.class)) {
                        ValidateRule validateRule = annotation.annotationType().getAnnotation(ValidateRule.class);
                        AbstractValidator validator = validateRule.value().getConstructor(annotation.annotationType()).newInstance(annotation);
                        pack.addFieldValidator(field, getAnnotationGroup(annotation), validator);
                    }
                }
            }
        }
        validatorCache.put(clazz, pack);
        return pack;
    }

    private static String getAnnotationGroup(Annotation annotation) {
        String group = "";
        try {
            Method groupMethod = annotation.annotationType().getDeclaredMethod("group");
            Object groupObject = groupMethod.invoke(annotation);
            if(groupObject != null) {
                group = groupObject.toString();
            }
        } catch (NoSuchMethodException e) {}
          catch (IllegalAccessException
                  | IllegalArgumentException
                  | InvocationTargetException e) {}

        return group;
    }
}

```



### 通用初始化方法

```java
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
        Class<?> _clazz = clazz;
        while (_clazz != null) {
            // 属性初始化
            Field[] fields = _clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getDeclaredAnnotation(Initializer.class) != null) {
                    pack.addFieldInitializer(field);
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

```



### 堆栈追踪校验 &访问追踪

```java
package com.github.yafeiwang1240.obrien.stacktrace;

import com.github.yafeiwang1240.obrien.stacktrace.annotation.BeanRequest;
import com.github.yafeiwang1240.obrien.stacktrace.annotation.MethodRequest;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.uitls.StackTraceUtils;

import java.lang.reflect.Method;

/**
 * 校验器
 */
public class Verification {
    /**
     * 堆栈校验
     * @param from
     * @return
     */
    public static VerificationResult validStack(Class from, Class clazz, Method method) {
        VerificationResult result = new VerificationResult();
        if (from.getDeclaredAnnotation(BeanRequest.class) == null) {
            result.setStatus(VerificationResult.Status.FAILED);
            result.setMessages(Lists.asList("object must with annotation " + BeanRequest.class.getName()));
            return result;
        }
        Method methodFind = null;
        Method[] methods = from.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getDeclaredAnnotation(MethodRequest.class) != null) {
                methodFind = m;
                break;
            }
        }
        if (methodFind != null) {
            StackTrace stackTrace = StackTraceUtils.FrontStackTrace(clazz, method);
            if (methodFind.getName().equals(stackTrace.getMethodName()) && stackTrace.getClazzName().equals(from.getName())) {
                result.setStatus(VerificationResult.Status.SUCCEED);
                result.setMessages(Lists.asList(from.getName(), methodFind.getName()));
                return result;
            } else {
                result.setStatus(VerificationResult.Status.FAILED);
                result.setMessages(Lists.asList("object and method must is Caller"));
                return result;
            }
        }
        result.setStatus(VerificationResult.Status.FAILED);
        result.setMessages(Lists.asList("object methods must with annotation " + MethodRequest.class.getName()));
        return result;
    }
}

```



### java反射优化

```java
package com.github.yafeiwang1240.obrien.fastreflect;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectClassException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectFieldException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectMethodException;
import com.github.yafeiwang1240.obrien.fastreflect.pack.FastReflectPack;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射优化工具
 * @author wangyafei
 * @date 2019-07-29
 */
public class FastReflectUtils {

    private static final Map<Class, FastReflectPack> fastReflectPackCache;

    static {
        fastReflectPackCache = Maps.create(ConcurrentHashMap::new);
    }

    /**
     * 获得方法列表（包含父类方法, 如果覆盖父类方法仅获得子类的方法）
     * @param clazz
     * @return
     * @throws ReflectClassException
     */
    public static List<Method> methods(Class<?> clazz) throws ReflectClassException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getMethods();
    }

    /**
     * 获得属性列表（包含父类属性, 如果覆盖父类属性仅获得子类的属性）
     * @param clazz
     * @return
     * @throws ReflectClassException
     */
    public static List<Field> fields(Class<?> clazz) throws ReflectClassException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getFields();
    }

    /**
     * 设置属性值
     * @param o
     * @param fieldName
     * @param value
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static void setFieldValue(Object o, String fieldName, Object value) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        pack.setFiledValue(o, fieldName, value);
    }

    /**
     * 获得对象属性值
     * @param o
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object o, String fieldName) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.getFiledValue(o, fieldName);
    }

    /**
     * 获取静态属性值
     * @param clazz
     * @param fieldName
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     */
    public static Object getFieldValue(Class<?> clazz, String fieldName) throws ReflectClassException, ReflectFieldException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.getFiledValue(null, fieldName);
    }

    /**
     * 执行静态无参方法
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object methodInvoke(Class<?> clazz, String methodName) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(clazz);
        return pack.methodInvoke(null, methodName, null, null);
    }

    /**
     * 执行无参方法
     * @param o
     * @param methodName
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     * @throws ReflectMethodException
     */
    public static Object methodInvoke(Object o, String methodName) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(o, methodName, null, null);
    }

    /**
     * 执行弱匹配方法
     * @param o
     * @param methodName
     * @param args
     * @return
     */
    public static Object methodInvoke(Object o, String methodName, Object[] args) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(null, methodName, args, null);
    }

    /**
     * 执行强匹配方法
     * @param o
     * @param methodName
     * @param args
     * @param paramTypes
     * @return
     * @throws ReflectClassException
     * @throws ReflectFieldException
     * @throws ReflectMethodException
     */
    public static Object methodInvoke(Object o, String methodName, Object[] args, Class<?>[] paramTypes) throws ReflectClassException, ReflectFieldException, ReflectMethodException {
        FastReflectPack pack = getFastReflectPack(o.getClass());
        return pack.methodInvoke(null, methodName, args, paramTypes);
    }

    private static FastReflectPack getFastReflectPack(Class<?> clazz) throws ReflectClassException {
        if (clazz == null) return null;
        FastReflectPack pack;
        if (fastReflectPackCache.containsKey(clazz)) {
            pack = fastReflectPackCache.get(clazz);
        } else {
            pack = getAndCacheFastReflectPack(clazz);
        }
        return pack;
    }

    private synchronized static FastReflectPack getAndCacheFastReflectPack(Class clazz) throws ReflectClassException {
        if (fastReflectPackCache.containsKey(clazz)) {
            return fastReflectPackCache.get(clazz);
        }
        FastReflectPack pack = new FastReflectPack(clazz);
        fastReflectPackCache.put(clazz, pack);
        return pack;
    }

}
```


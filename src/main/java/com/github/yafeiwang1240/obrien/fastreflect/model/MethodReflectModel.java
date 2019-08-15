package com.github.yafeiwang1240.obrien.fastreflect.model;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectMethodException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class MethodReflectModel {
    private String methodName;
    private Class<?>[] paramTypes;
    private Parameter[] parameters;
    private Class<?> returnType;
    private Class<?>[] exceptionTypes;
    private Annotation[] annotations;
    private Method method;

    public MethodReflectModel(Method method) {
        this.method = method;
        this.method.setAccessible(true);
        this.returnType = method.getReturnType();
        this.paramTypes = method.getParameterTypes();
        this.exceptionTypes = method.getExceptionTypes();
        this.annotations = method.getDeclaredAnnotations();
        this.methodName = method.getName();
        this.parameters = method.getParameters();
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?>[] getExceptionTypes() {
        return exceptionTypes;
    }

    public Annotation[] getDeclaredAnnotations() {
        return annotations;
    }

    public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
        if (annotations == null) return null;
        for (Annotation annotation : annotations) {
            if (annotation.getClass() == annotationClass) {
                return (T) annotation;
            }
        }
        return null;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    /**
     * 非强制参数匹配
     * @param instance
     * @param args
     * @return
     * @throws ReflectMethodException
     */
    public Object invoke(Object instance, Object... args) throws ReflectMethodException {
        try {
            Object[] t;
            int length = args == null ? 0 : args.length;
            if (paramTypes.length < length) {
                t = Arrays.copyOf(args, paramTypes.length);
            } else {
                t = args;
            }
            return method.invoke(instance, t);
        } catch (Exception e) {
            throw new ReflectMethodException(e.getMessage(), e);
        }
    }

    @Override
    public int hashCode() {
        int result = methodName != null ? methodName.hashCode() : 0;
        if (paramTypes != null) {
            for (Class<?> clazz : paramTypes) {
                result = (result * 31) + (clazz == null ? 0 : clazz.hashCode());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return  true;
        }
        if (!(o instanceof MethodReflectModel)) {
            return false;
        }
        MethodReflectModel that = (MethodReflectModel) o;
        if (!methodName.equals(that.getMethodName())) {
            return false;
        }
        if ((paramTypes == null && that.getParamTypes() != null) || (paramTypes != null && that.getParamTypes() == null)) {
            return false;
        } else if (paramTypes != null && that.getParamTypes() != null) {
            if (paramTypes.length != that.getParamTypes().length) {
                return false;
            }
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] != that.getParamTypes()[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method相似匹配算法
     * @param args
     * @param paramTypes
     * @return
     * @throws ReflectMethodException
     */
    public boolean equals(Object[] args, Class<?>[] paramTypes, boolean force) {
        // 无参
        if ((this.paramTypes == null || this.paramTypes.length <= 0)
                &&(args == null || args.length <= 0)
                && (paramTypes == null || paramTypes.length <= 0)) {
            return true;
        }
        // 强校验
        if (force) {
            if (paramTypes != null) {
                return strongVerification(paramTypes);
            } else {
                paramTypes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    paramTypes[i] = args[i].getClass();
                }
                return strongVerification(paramTypes);
            }
        } else {
            // 弱校验
            return weakVerification(args);
        }
    }

    /**
     * 强匹配
     * @param paramTypes
     * @return
     */
    private boolean strongVerification(Class<?>[] paramTypes) {
        if ((this.paramTypes == null && paramTypes != null) || (this.paramTypes != null && paramTypes == null)) {
            return false;
        } else if (this.paramTypes != null && paramTypes != null) {
            if (this.paramTypes.length != paramTypes.length) {
                return false;
            }
            for (int i = 0; i < this.paramTypes.length; i++) {
                if (this.paramTypes[i] != paramTypes[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 弱匹配
     * @param args
     * @return
     */
    private boolean weakVerification(Object[] args) {
        int len1 = args.length;
        int len2 = this.paramTypes != null ? this.paramTypes.length : 0;
        int len = Math.min(len1, len2);
        for (int i = 0; i < len; i++) {
            Class<?> clazz = args[i].getClass();
            if (clazz != this.paramTypes[i]) {

                // 匹配包装类的基本类型
                Field field = null;
                try {
                    field = clazz.getDeclaredField("TYPE");
                } catch (Exception e) {
                    // ignore
                }
                if (field == null) {
                    return false;
                }
                Class<?> _clazz = null;
                try {
                    _clazz = (Class<?>) field.get(null);
                } catch (Exception e) {
                    // ignore
                }
                if (_clazz == null) {
                    return false;
                }
                if (_clazz != this.paramTypes[i]) {
                    return false;
                }
            }
        }
        return true;
    }
}

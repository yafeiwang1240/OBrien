package com.github.yafeiwang1240.obrien.fastreflect.pack;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectClassException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectFieldException;
import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectMethodException;
import com.github.yafeiwang1240.obrien.fastreflect.model.ClassReflectModel;
import com.github.yafeiwang1240.obrien.fastreflect.model.FieldReflectModel;
import com.github.yafeiwang1240.obrien.fastreflect.model.MethodReflectModel;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射优化包
 */
public class FastReflectPack {
    private Map<String, FieldReflectModel> fieldModelCache = Maps.create(ConcurrentHashMap::new);
    private Map<String, List<MethodReflectModel>> methodModelCache = Maps.create(ConcurrentHashMap::new);
    private ClassReflectModel classModelCache;
    private Class<?> clazz;
    private List<Field> fields;
    private List<Method> methods;

    public void setFiledValue(Object o, String fieldName, Object value) throws ReflectFieldException {
        if (!fieldModelCache.containsKey(fieldName)) {
            throw new ReflectFieldException("无此属性");
        }
        fieldModelCache.get(fieldName).set(o, value);
    }

    public Object getFiledValue(Object o, String fieldName) throws ReflectFieldException {
        if (!fieldModelCache.containsKey(fieldName)) {
            throw new ReflectFieldException("无此属性");
        }
        return fieldModelCache.get(fieldName).get(o);
    }

    public Object methodInvoke(Object o, String methodName, Object[] args, Class<?>[] paramTypes) throws ReflectFieldException, ReflectMethodException {
        if (!methodModelCache.containsKey(methodName)) {
            throw new ReflectFieldException("无此属性");
        }
        List<MethodReflectModel> models = methodModelCache.get(methodName);
        if (models.size() <= 0) {
            throw new ReflectFieldException("无此属性");
        }
        if (models.size() == 1) {
            models.get(0).invoke(o, args);
        }
        for (MethodReflectModel model : models) {
            if (model.equals(args, paramTypes)) {
                return model.invoke(o, args);
            }
        }
        throw new ReflectFieldException("无此属性");
    }

    public FastReflectPack(Class<?> clazz) throws ReflectClassException {
        if (clazz == null) throw new ReflectClassException("无效的class");
        this.clazz = clazz;
        init(clazz);
    }

    private void init(Class<?> clazz) throws ReflectClassException {
        this.classModelCache = new ClassReflectModel(clazz);
        List<Class<?>> classes = classModelCache.getClassList();
        for (Class<?> clz : classes) {
            // 属性
            Field[] fields = clz.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    // 只保留子类属性
                    if (fieldModelCache.containsKey(field.getName())) {
                        continue;
                    }
                    fieldModelCache.put(field.getName(), new FieldReflectModel(field));
                    this.fields.add(field);
                }
            }
            // 方法
            Method[] methods = clz.getDeclaredMethods();
            if (methods != null) {
                for (Method method : methods) {
                    List<MethodReflectModel> models;
                    if ((models = methodModelCache.get(method.getName())) == null) {
                        models = Lists.newArrayList();
                        methodModelCache.put(method.getName(), models);
                    }
                    MethodReflectModel model = new MethodReflectModel(method);
                    // @Override
                    if (models.contains(model)) {
                        continue;
                    }
                    models.add(model);
                    this.methods.add(method);
                }
            }
        }
    }


    public Class<?> getClazz() {
        return clazz;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }
}

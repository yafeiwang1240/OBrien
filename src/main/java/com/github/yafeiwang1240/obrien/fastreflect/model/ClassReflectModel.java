package com.github.yafeiwang1240.obrien.fastreflect.model;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectClassException;
import com.github.yafeiwang1240.obrien.lang.Lists;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassReflectModel {

    private List<Class<?>> classList;
    private Object instance;
    private String name;
    private Annotation[] annotations;

    public ClassReflectModel(Class<?> clazz) throws ReflectClassException {
        classList = Lists.newArrayList();
        annotations = clazz.getDeclaredAnnotations();
        Class<?> _clazz = clazz;
        while (_clazz != null) {
            classList.add(_clazz);
            _clazz = _clazz.getSuperclass();
        }
        if (classList.size() <= 0) {
            throw new ReflectClassException("未添加任何class");
        }
        try {
            instance = classList.get(0).newInstance();
        } catch (Exception e) {
            throw new ReflectClassException(e.getMessage(), e);
        }
        name = classList.get(0).getName();
    }

    public List<Class<?>> getClassList() {
        return classList;
    }

    public Object newInstance() {
        return instance;
    }

    public String getName() {
        return name;
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

    @Override
    public int hashCode() {
        return classList.size() == 0 ? 0 : classList.get(0).hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassReflectModel)) {
            return false;
        }
        ClassReflectModel that = (ClassReflectModel) o;
        return classList.get(0).equals(that.getClassList().get(0));
    }
}

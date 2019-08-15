package com.github.yafeiwang1240.obrien.fastreflect.model;

import com.github.yafeiwang1240.obrien.fastreflect.exception.ReflectFieldException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FieldReflectModel {
    private String fieldName;
    private Class<?> type;
    private Field field;
    private Annotation[] annotations;
    private Type[] templates;

    public FieldReflectModel(Field field) {
        this.field = field;
        this.field.setAccessible(true);
        this.fieldName = field.getName();
        this.type = field.getType();
        this.annotations = field.getDeclaredAnnotations();
        Type gType = field.getGenericType();
        this.templates = ((ParameterizedType) gType).getActualTypeArguments();
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getType() {
        return type;
    }

    public Field getField() {
        return field;
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

    public Object get(Object o) throws ReflectFieldException {
        try {
            return field.get(o);
        } catch (Exception e) {
            throw new ReflectFieldException(e.getMessage(), e);
        }
    }

    public void set(Object o, Object value) throws ReflectFieldException {
        try {
            field.set(o, value);
        } catch (Exception e) {
            throw new ReflectFieldException(e.getMessage(), e);
        }
    }

    public Type[] getTemplates() {
        return templates;
    }

    @Override
    public int hashCode() {
        return fieldName == null ? 0 : fieldName.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldReflectModel)) {
            return false;
        }
        FieldReflectModel that = (FieldReflectModel) o;
        return fieldName.equals(that.getFieldName());
    }
}

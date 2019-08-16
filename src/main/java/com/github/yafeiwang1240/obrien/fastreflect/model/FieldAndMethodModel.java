package com.github.yafeiwang1240.obrien.fastreflect.model;

import com.github.yafeiwang1240.obrien.fastreflect.exception.BeanOperatorException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 属性、set、get(is)集合
 */
public class FieldAndMethodModel {
    private Field field;
    private Method set;
    private Method get;
    private Method is;
    private Class<?> type;
    private Type[] templates;
    private String name;

    public FieldAndMethodModel(Field field, Method set, Method get, Method is) {
        this.field = field;
        this.set = set;
        this.get = get;
        this.is = is;
        this.field.setAccessible(true);
        if (this.is != null) is.setAccessible(true);
        if (this.set != null) set.setAccessible(true);
        if (this.get != null) get.setAccessible(true);
        this.type = field.getType();
        Type mType = field.getGenericType();
        try {
            templates = ((ParameterizedType) mType).getActualTypeArguments();
        } catch (Exception e) {
            templates = new Class[]{Object.class};
        }
        this.name = this.field.getName();
    }

    /**
     * 获取field
     * @return
     */
    public Field getField() {
        return field;
    }

    /**
     * 获取set方法
     * @return
     */
    public Method getSet() {
        return set;
    }

    /**
     * 获取get方法
     * @return
     */
    public Method getGet() {
        return get;
    }

    /**
     * 获取is方法
     * @return
     */
    public Method getIs() {
        return is;
    }

    public Class<?> getType() {
        return type;
    }

    public Type[] getTemplates() {
        return templates;
    }

    public String getName() {
        return name;
    }

    /**
     * 设置属性值
     * @param obj
     * @param value
     * @throws BeanOperatorException
     */
    public void set(Object obj, Object value) throws BeanOperatorException {
        if (set != null) {
            try {
                set.invoke(obj, value);
            } catch (Exception e) {
                throw new BeanOperatorException(e.getMessage(), e);
            }
        } else {
            try {
                field.set(obj, value);
            } catch (Exception e) {
                throw new BeanOperatorException(e.getMessage(), e);
            }
        }
    }

    /**
     * 获取属性值方法
     * @param obj
     * @return
     * @throws BeanOperatorException
     */
    public Object get(Object obj) throws BeanOperatorException {
        if (get != null) {
            try {
                return get.invoke(obj);
            } catch (Exception e) {
                throw new BeanOperatorException(e.getMessage(), e);
            }
        } else if (is != null){
            try {
                return is.invoke(obj);
            } catch (Exception e) {
                throw new BeanOperatorException(e.getMessage(), e);
            }
        } else {
            try {
                return field.get(obj);
            } catch (Exception e) {
                throw new BeanOperatorException(e.getMessage(), e);
            }
        }
    }
}

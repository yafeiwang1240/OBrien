package com.github.yafeiwang1240.obrien.bean;

import com.github.yafeiwang1240.obrien.exception.UnMatchedTypeException;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtils {

    private static final Map<Class, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();

    /**
     * 拷贝源对象同名属性
     * @param from 源对象
     * @param to 目标对象
     */
    public static void copy(Object from, Object to) {
        try {
            copy(from, to, true);
        } catch (UnMatchedTypeException e) {
            // 忽略异常
        }
    }

    /**
     * 拷贝源对象同名属性
     * @param from 源对象
     * @param to 目标对象
     * @param ignoreException 是否忽略异常
     */
    public static void copy(Object from, Object to, boolean ignoreException) throws UnMatchedTypeException {
        if(from == null || to == null) return;
        List<Field> fromFields = getAllFields(from.getClass());
        CopyField copyField = new CopyField(from, to);
        fromFields.forEach(copyField::copyField);
        if(!ignoreException && copyField.getUnMatchedTypeException() != null) {
            throw copyField.getUnMatchedTypeException();
        }
    }

    /**
     * 拷贝为空的源对象同名属性
     * @param from
     * @param to
     * @param ignoreException
     * @throws UnMatchedTypeException
     */
    public static void copyNotExit(Object from, Object to, boolean ignoreException) throws UnMatchedTypeException {
        if (to == null || from == null) {
            return;
        }
        CopyField copyField = new CopyField(from, to);
        List<Field> fromFields = getAllFields(from.getClass());
        fromFields.forEach(copyField::copyFieldNotExit);
        if(copyField.getUnMatchedTypeException() != null && !ignoreException) {
            throw copyField.getUnMatchedTypeException();
        }
    }

    /**
     * 获取源对象属性键值对
     * @param object
     * @return
     */
    public static Map<String, Object> toMap(Object object) {
        if(object == null) return null;
        FieldMap fieldMap = new FieldMap(Maps.create(HashMap::new), object);
        Class clazz = object.getClass();
        List<Field> fields = getAllFields(clazz);
        fields.forEach(fieldMap::putValue);
        return fieldMap.getMap();
    }

    /**
     * 获取数值
     * @param o
     * @param fieldName
     * @param fieldType
     * @param <T>
     * @return
     * @throws UnMatchedTypeException
     */
    public static <T> T getValue(Object o, String fieldName, Class<T> fieldType) throws UnMatchedTypeException {
        Field field = getField(o.getClass(), fieldName);
        field.setAccessible(true);
        try {
            Object val = field.get(o);
            if(val.getClass() != fieldType) {
                throw new UnMatchedTypeException(fieldName, fieldType, val.getClass());
            }
            return (T) val;
        } catch (IllegalAccessException e) {
            // 忽略异常
        }
        return null;
    }

    private static Field getField(Class toClass, String name) {
        if (!fieldCache.containsKey(toClass)) {
            cacheFiled(toClass);
        }
        return fieldCache.get(toClass).get(name);
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        if(!fieldCache.containsKey(clazz)) {
            cacheFiled(clazz);
        }
        return Lists.newArrayList(fieldCache.get(clazz).values());
    }

    private static synchronized void cacheFiled(Class<?> clazz) {
        if(!fieldCache.containsKey(clazz)) {
            Class _clazz = clazz;
            FieldMap fieldMap = new FieldMap(Maps.create(HashMap::new));
            fieldCache.put(clazz, fieldMap.getMap());
            while(_clazz != null) {
                Field[] fields = _clazz.getDeclaredFields();
                Arrays.asList(fields).forEach(fieldMap::putField);
                _clazz = _clazz.getSuperclass();
            }
        }
    }

    private static class FieldMap {
        private Map map;
        private Object object;
        private IllegalAccessException illegalAccessException;
        public FieldMap(Map map) {
            this.map = map;
        }
        public FieldMap(Map map, Object object) {
            this.map = map;
            this.object = object;
        }
        public void putField(Field field) {
            map.put(field.getName(), field);
        }
        public void putValue(Field field) {
            try {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                illegalAccessException = e;
            }
        }
        public Map getMap() {
            return map;
        }
        public IllegalAccessException getIllegalAccessException() {
            return illegalAccessException;
        }
    }

    private static class CopyField {
        private Object from;
        private Object to;
        private UnMatchedTypeException unMatchedTypeException;
        private IllegalAccessException illegalAccessException;
        public CopyField(Object from, Object to) {
            this.from = from;
            this.to = to;
        }
        public void copyField(Field field) {
            try {
                Field toField = getField(to.getClass(), field.getName());
                if(toField == null) {
                    return;
                }
                field.setAccessible(true);
                toField.setAccessible(true);
                if(field.getType() != toField.getType()) {
                    throw new UnMatchedTypeException(field.getName(), field.getType(), toField.getType());
                }
                toField.set(to, field.get(from));
            } catch (IllegalAccessException e) {
                illegalAccessException = e;
            } catch (UnMatchedTypeException e) {
                unMatchedTypeException = e;
            }
        }
        public void copyFieldNotExit(Field field) {
            try {
                Field toField = getField(to.getClass(), field.getName());
                if(toField != null) {
                    field.setAccessible(true);
                    toField.setAccessible(true);
                    if (field.getType() != toField.getType()) {
                        throw new UnMatchedTypeException(field.getName(), field.getType(), toField.getType());
                    }
                    Object val = toField.get(to);
                    if (val == null) {
                        toField.set(to, field.get(from));
                    }
                }
            } catch (IllegalAccessException e) {
                illegalAccessException = e;
            } catch (UnMatchedTypeException e) {
                unMatchedTypeException = e;
            }
        }
        public UnMatchedTypeException getUnMatchedTypeException() {
            return unMatchedTypeException;
        }
        public IllegalAccessException getIllegalAccessException() {
            return illegalAccessException;
        }
    }
}

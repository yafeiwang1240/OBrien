package com.github.yafeiwang1240.obrien.fastreflect.pack;

import com.github.yafeiwang1240.obrien.fastreflect.model.FieldAndMethodModel;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class BeanReflectPack {

    private Map<String, FieldAndMethodModel> fieldAndMethodModelMap = Maps.newHashMap();

    public BeanReflectPack(Class<?> clazz) {
        Class<?> _clazz = clazz;
        while (_clazz != null) {
            Field[] fields = _clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                Method set = null;
                Method get = null;
                Method is = null;
                try {
                    String setName = "set" + upperAcronym(name);
                    set = _clazz.getDeclaredMethod(setName, field.getType());
                } catch (Exception e) {
                    // ignore
                }
                try {
                    String getName = "get" + upperAcronym(name);
                    get = _clazz.getDeclaredMethod(getName);
                } catch (Exception e) {
                    // ignore
                }
                try {
                    String isName = "is" + upperAcronym(name);
                    is = _clazz.getDeclaredMethod(isName);
                } catch (Exception e) {
                    // ignore
                }
                FieldAndMethodModel model = new FieldAndMethodModel(field, set, get, is);
                fieldAndMethodModelMap.put(field.getName(), model);
            }
            _clazz = _clazz.getSuperclass();
        }
    }

    /**
     * 获取属性及其set、get方法
     * @param fieldName
     * @return
     */
    public FieldAndMethodModel getFieldAndMethod(String fieldName) {
        return fieldAndMethodModelMap.get(fieldName);
    }

    /**
     * 获取所有属性、方法
     * @return
     */
    public Collection<FieldAndMethodModel> getFieldAndMethods() {
        return fieldAndMethodModelMap.values();
    }

    private static String upperAcronym(String name) {
        char first = name.charAt(0);
        if (first >= 'a' && first <= 'z') {
            first = (char) (first - 32);
            char[] chars = name.toCharArray();
            chars[0] = first;
            return String.valueOf(chars);
        }
        return name;
    }
}

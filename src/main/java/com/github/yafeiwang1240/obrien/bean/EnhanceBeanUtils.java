package com.github.yafeiwang1240.obrien.bean;

import com.github.yafeiwang1240.obrien.bean.enums.EnumFieldTransfer;
import com.github.yafeiwang1240.obrien.bean.exception.FieldTransferException;
import com.github.yafeiwang1240.obrien.fastreflect.BeanReflectUtils;
import com.github.yafeiwang1240.obrien.fastreflect.model.FieldAndMethodModel;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.util.Collection;
import java.util.Map;

/**
 * 增强版的bean处理工具
 */
public class EnhanceBeanUtils {

    /**
     * bean 转 Map
     * @param o
     * @return
     */
    public static Map<String, Object> toMapNotNull(Object o) throws FieldTransferException {
        if (o == null) return null;
        Map<String, Object> map = Maps.newHashMap();
        try {
            Collection<FieldAndMethodModel> fieldAndMethodModels = BeanReflectUtils.getFieldAndMethods(o.getClass());
            for (FieldAndMethodModel model : fieldAndMethodModels) {
                String key;
                Object value;
                Object val = model.get(o);
                if (val == null) continue;
                key = model.getField().getName();
                Class<?> mType = val.getClass();
                value = EnumFieldTransfer.getInstance(val.getClass()).toMap(val);
                if (value != null) map.put(key, value);
            }
        } catch (Exception e) {
            throw new FieldTransferException(e.getMessage(), e);
        }
        return map;
    }

    /**
     * map 转 bean
     * @param map
     * @param type
     * @return
     */
    public static <T> T toObject(Map<String, Object> map, Class<T> type) throws FieldTransferException {
        if (type == null || type == Object.class || map == null) return (T) map;
        T o = null;
        try {
            o = type.newInstance();
            Collection<FieldAndMethodModel> fieldAndMethodModels = BeanReflectUtils.getFieldAndMethods(type);
            for (FieldAndMethodModel model : fieldAndMethodModels) {
                String key;
                Object value;
                Class<?> mType = model.getType();
                key = model.getField().getName();
                if (!map.containsKey(key)) continue;
                Object val = map.get(key);
                if (val instanceof Collection) {
                    mType = (Class<?>) model.getTemplates()[0];
                }
                value = EnumFieldTransfer.getInstance(val.getClass()).toObject(val, mType);
                if (value != null) model.set(o, value);
            }
        } catch (Exception e) {
            throw new FieldTransferException(e.getMessage(), e);
        }
        return o;
    }

    /**
     * 拷贝同名属性
     * @param from
     * @param to
     * @throws FieldTransferException
     */
    public static void copy(Object from, Object to) throws FieldTransferException {
        try {
            Collection<FieldAndMethodModel> fieldAndMethodModels = BeanReflectUtils.getFieldAndMethods(to.getClass());
            Class<?> f = from.getClass();
            for (FieldAndMethodModel model : fieldAndMethodModels) {
                FieldAndMethodModel fromModel = BeanReflectUtils.getFieldAndMethod(
                        model.getName(), f);
                if (fromModel != null) {
                    model.set(to, fromModel.get(from));
                }
            }
        } catch (Exception e) {
            throw new FieldTransferException(e.getMessage(), e);
        }
    }
}

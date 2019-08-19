package com.github.yafeiwang1240.obrien.bean.enums;

import com.github.yafeiwang1240.obrien.bean.EnhanceBeanUtils;
import com.github.yafeiwang1240.obrien.bean.exception.FieldTransferException;
import com.github.yafeiwang1240.obrien.lang.Lists;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 属性类型
 */
public enum EnumFieldTransfer {

    ENUM(Enum.class) {
        @Override
        public Object toMap(Object o) {
            return o == null ? null : o.toString();
        }

        @Override
        public Object toObject(Object o, Class type) throws FieldTransferException {
            if (type == null || (type != Enum.class)) {
                if (type == Object.class) return o == null ? null : o.toString();
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Enum) {
                return (Enum) o;
            }
            Object r = null;
            try {
                r = Enum.valueOf(type, o.toString());
            } catch (Exception e) {
                // ignore
            }
            if (r == null) {
                try {
                    r = Enum.valueOf(type, o.toString().toUpperCase());
                } catch (Exception e) {
                    // ignore
                }
            }
            if (r == null) {
                try {
                    r = Enum.valueOf(type, o.toString().toLowerCase());
                } catch (Exception e) {
                    // ignore
                }
            }
            return r;
        }
    },

    BOOLEAN(Boolean.class){
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Boolean.class.getClass() && type != boolean.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
            if (o instanceof Number) {
                return ((Number) o).doubleValue() > 0.000000000;
            }
            return Boolean.valueOf(o.toString());
        }
    },
    BYTE(Byte.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Byte.class.getClass() && type != byte.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Byte) {
                return (Byte) o;
            }
            if (o instanceof Number) {
                return ((Number) o).byteValue();
            }
            return Byte.valueOf(o.toString());
        }
    },
    CHARACTER(Character.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Character.class && type != char.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Character) {
                return (Character) o;
            }
            String str;
            return (str = o.toString()).length() > 1 ? null : str.charAt(0);
        }
    },
    SHORT(Short.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Short.class && type != short.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Short) {
                return (Short) o;
            }
            if (o instanceof Number) {
                return ((Number) o).shortValue();
            }
            return Short.valueOf(o.toString());
        }
    },
    INTEGER(Integer.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Integer.class && type != int.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Integer) {
                return (Integer) o;
            }
            if (o instanceof Number) {
                return ((Number) o).intValue();
            }
            return Integer.valueOf(o.toString());
        }
    },
    LONG(Long.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Long.class && type != long.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Long) {
                return (Long) o;
            }
            if (o instanceof Number) {
                return ((Number) o).longValue();
            }
            return Long.valueOf(o.toString());
        }
    },
    FLOAT(Float.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Float.class && type != float.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Float) {
                return (Float) o;
            }
            if (o instanceof Number) {
                return ((Number) o).floatValue();
            }
            return Float.valueOf(o.toString());
        }
    },
    DOUBLE(Double.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || (type != Double.class && type != double.class)) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Double) {
                return (Double) o;
            }
            if (o instanceof Number) {
                return ((Number) o).doubleValue();
            }
            return Double.valueOf(o.toString());
        }
    },
    STRING(String.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || type != String.class) {
                if (type == Object.class) return o;
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof String) {
                return (String) o;
            }
            return String.valueOf(o);
        }
    },
    DATE(Date.class) {
        @Override
        public Object toMap(Object o) {
            return o;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (type == null || type != Date.class) {
                throw new FieldTransferException("无效的type: " + type);
            }
            if (o == null) return null;
            if (o instanceof Date) {
                return (Date) o;
            }
            if (o instanceof Long) {
                return new Date((Long) o);
            }
            SimpleDateFormat sdf = null;
            String str = o.toString();
            final String FORMAT_TO_SECONDS = "yyyy-MM-dd HH:mm:ss";
            final String FORMAT_TO_DAY = "yyyy-MM-dd";
            final String FORMAT_SHORT = "yyyyMMdd";

            if (str.length() == FORMAT_TO_DAY.length()) {
                sdf = new SimpleDateFormat(FORMAT_TO_DAY);
            }
            if (str.length() == FORMAT_SHORT.length()) {
                sdf = new SimpleDateFormat(FORMAT_SHORT);
            }
            if (str.length() == FORMAT_TO_SECONDS.length()) {
                sdf = new SimpleDateFormat(FORMAT_TO_SECONDS);
            }
            if (sdf == null) {
                return null;
            }
            try {
                return sdf.parse(str);
            } catch (ParseException e) {
                throw new FieldTransferException("日期解析失败: " + o);
            }
        }
    },
    COLLECTION(Collection.class) {
        @Override
        public Object toMap(Object o) throws FieldTransferException {
            if (o == null) return null;
            if (!(o instanceof Collection)) {
                return null;
            }
            List<Object> list = Lists.newArrayList();
            Collection _o = (Collection) o;
            for (Object obj : _o) {
                Object val = getInstance(obj.getClass()).toMap(obj);
                if (val != null) {
                    list.add(val);
                }
            }
            return list;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (o == null) return null;
            if (!(o instanceof Collection)) {
                return null;
            }
            Collection _o = (Collection) o;
            List<Object> list = Lists.newArrayList();
            for (Object obj : _o) {
                Object val = getInstance(obj.getClass()).toObject(obj, type);
                if (val != null) {
                    list.add(val);
                }
            }
            return list;
        }
    },
    MAP(Map.class) {
        @Override
        public Object toMap(Object o) throws FieldTransferException {
            if (o == null) return null;
            if (!(o instanceof Map)) {
                return null;
            }
            Map<String, Object> map = Maps.newHashMap();
            Map<String, Object> _o = (Map) o;
            for (Map.Entry<String, Object> entry : _o.entrySet()) {
                Object val = getInstance(entry.getValue().getClass()).toMap(entry.getValue());
                if (val != null) {
                    map.put(entry.getKey(), val);
                }
            }
            return map;
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (o == null) return null;
            if (type == Object.class) return o;
            if (!(o instanceof Map)) {
                return null;
            }
            Map<String, Object> _o = (Map) o;
            return EnhanceBeanUtils.toObject(_o, type);
        }
    },
    OBJECT(Object.class) {
        @Override
        public Object toMap(Object o) throws FieldTransferException {
            if (o == null) return null;
            if (o.getClass() == Object.class) {
                return o;
            }
            return EnhanceBeanUtils.toMapNotNull(o);
        }

        @Override
        public Object toObject(Object o, Class<?> type) throws FieldTransferException {
            if (o == null) return null;
            if (type == Object.class) return o;
            return getInstance(o.getClass()).toObject(o, type);
        }
    };

    private static Map<Class<?>, EnumFieldTransfer> fieldTransferMap = Maps.newHashMap();
    static {
        EnumFieldTransfer[] transfers = EnumFieldTransfer.values();
        for (EnumFieldTransfer transfer : transfers) {
            fieldTransferMap.put(transfer.type, transfer);
        }
    }
    private Class<?> type;

    EnumFieldTransfer(Class<?> type) {
        this.type = type;
    }

    public abstract Object toMap(Object o) throws FieldTransferException;

    public abstract Object toObject(Object o, Class<?> type) throws FieldTransferException;

    public static EnumFieldTransfer getInstance(Class<?> type) {
        if (type == null) return null;
        if (fieldTransferMap.containsKey(type)) {
            return fieldTransferMap.get(type);
        }
        Class<?>[] classes = type.getInterfaces();
        if (classes != null && classes.length > 0) {
            for (Class<?> clazz : classes) {
                EnumFieldTransfer transfer = getInstance(clazz);
                if (transfer != null) {
                    return transfer;
                }
            }
        }
        Class<?> clazz = type.getSuperclass();
        return getInstance(clazz);
    }
}

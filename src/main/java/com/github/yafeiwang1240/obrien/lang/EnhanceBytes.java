package com.github.yafeiwang1240.obrien.lang;

import com.github.yafeiwang1240.obrien.exception.SerializationException;
import com.github.yafeiwang1240.obrien.exception.CastTypeException;
import com.github.yafeiwang1240.obrien.exception.ToBytesException;
import com.github.yafeiwang1240.obrien.exception.ToTemplateException;
import com.github.yafeiwang1240.obrien.uitls.SerializableUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于Bytes增强版的字节处理工具
 */
public class EnhanceBytes {

    // toBytes
    public static byte[] toBytes(Object o) throws ToBytesException {
        if(o == null) {
            return null;
        }

        if(o instanceof Boolean) {
            boolean _o = (boolean) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Byte) {
            byte _o = (byte) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Character) {
            char _o = (char) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Short) {
            short _o = (short) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Integer) {
            int _o = (int) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Long) {
            long _o = (long) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Float) {
            float _o = (float) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof Double) {
            double _o = (double) o;
            return Bytes.toBytes(_o);
        }

        if(o instanceof String) {
            String _o = (String) o;
            return Bytes.toBytes(_o);
        }

        try {
            return SerializableUtils.serialization(o);
        } catch (SerializationException e) {
            throw new ToBytesException(e);
        }
    }

    // toTemplate
    public static <T> T toObject(byte[] bytes, Class<T> clazz) throws ToTemplateException {
        // boolean, byte, char, short, int, long, float, double and String

        try {
            if (Boolean.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "boolean"))) {
                return cast(Bytes.toBoolean(bytes));
            }
            if (Byte.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "byte"))) {
                return cast((byte) Bytes.toShort(bytes));
            }
            if (Short.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "short"))) {
                return cast(Bytes.toShort(bytes));
            }
            if (Integer.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "int"))) {
                return cast(Bytes.toInt(bytes));
            }
            if (Long.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "long"))) {
                return cast(Bytes.toLong(bytes));
            }
            if (Float.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "float"))) {
                return cast(Bytes.toFloat(bytes));
            }
            if (Double.class.isAssignableFrom(clazz) || (clazz.isPrimitive() && StringUtils.equals(clazz.getName(), "double"))) {
                return cast(Bytes.toDouble(bytes));
            }
            if (String.class.isAssignableFrom(clazz)) {
                return cast(Bytes.toString(bytes));
            }
        } catch (CastTypeException e) {
            throw new ToTemplateException(e);
        }

        try {
            return cast(SerializableUtils.deserialization(bytes));
        } catch (SerializationException e) {
            throw new ToTemplateException(e);
        } catch (CastTypeException e) {
            throw new ToTemplateException(e);
        }
    }

    private static <T> T cast(Object obj) throws CastTypeException {
        return (T) obj;
    }
}

package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.uitls.exception.SerializationException;

import java.io.*;

/**
 * 序列化工具
 */
public class SerializableUtils {

    /**
     * 序列化
     * @param o
     * @return
     */
    public static byte[] serialization(Object o) throws SerializationException {
        if( o == null ) {
            return null;
        }
        if(!(o instanceof Serializable)) {
            throw new IllegalArgumentException("the object must implement Serializable");
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        } finally {
            IOUtils.closeQuietly(byteArrayOutputStream);
            IOUtils.closeQuietly(objectOutputStream);
        }
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws SerializationException
     */
    public static Object deserialization(byte[] bytes) throws SerializationException {
        if(bytes == null) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new SerializationException(e);
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
            IOUtils.closeQuietly(objectInputStream);
        }
    }
}

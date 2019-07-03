package com.github.yafeiwang1240.obrien.uitls;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class HashKeyTools {

    public static byte[] toHashKey(String origin) {
        byte[] originBytes = origin.getBytes(Charsets.UTF_8);
        byte[] val = new byte[originBytes.length + 4];
        byte[] hashBytes = Hashing.murmur3_32().hashBytes(originBytes).asBytes();

        int i;
        for(i = 0; i < 4; ++i) {
            val[i] = hashBytes[i];
        }

        for(i = 0; i < originBytes.length; ++i) {
            val[i + 4] = originBytes[i];
        }

        return val;
    }

    public static String decode(byte[] bytes) {
        if (bytes == null) {
            return "";
        } else {
            byte[] originBytes = new byte[bytes.length - 4];

            for(int i = 0; i < originBytes.length; ++i) {
                originBytes[i] = bytes[i + 4];
            }

            return new String(originBytes, Charsets.UTF_8);
        }
    }
}

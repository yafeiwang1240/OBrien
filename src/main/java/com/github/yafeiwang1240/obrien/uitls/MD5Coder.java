package com.github.yafeiwang1240.obrien.uitls;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 加盐md5算法
 * @author wangyafei
 * @date 2021-02-04
 */
public class MD5Coder {

    private static Random random = new Random();

    private static String salt = "abcdefghigklmnopqrstuvwxyz1234567890";

    /**
     * 加密算法
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(password.getBytes());
            return hex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("加密失败", e);
        }
    }

    private static String hex(byte[] digest) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    /**
     * 简单加盐算法
     * @param password
     * @return
     */
    public static String saltEncrypt(String password) {
        return encrypt(password, salt);
    }

    /**
     * 随机加盐算法
     * @param password
     * @return
     */
    public static String randSaltEncrypt(String password) {
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        // 生成最终的加密盐
        String salt = sBuilder.toString();
        return encrypt(password, salt);
    }

    private static String encrypt(String password, String salt) {
        password = encrypt(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }

    public static boolean verifySaltMD5(String password, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String Salt = new String(cs2);
        return encrypt(password + Salt).equals(String.valueOf(cs1));
    }

    public static void main(String[] args) {
        String password = "1253";
        String salt = randSaltEncrypt(password);
        System.out.println(verifySaltMD5(password, salt));
    }
}

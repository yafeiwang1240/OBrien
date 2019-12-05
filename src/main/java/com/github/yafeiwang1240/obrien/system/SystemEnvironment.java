package com.github.yafeiwang1240.obrien.system;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

/**
 * 封装获取系统信息
 *
 * @author wangyafei
 */
public class SystemEnvironment {

    private static Properties properties;
    private static InetAddress address;
    private static Map<String, String> envMap;
    private static OS os;
    private static String userhome;
    private static String userdir;
    private static String username;
    private static String classLoadPath;

    public enum OS {
        LINUX,
        WINDOWS,
    }

    static {
        properties = System.getProperties();
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        envMap = System.getenv();
        os = osname().toLowerCase().contains("windows") ? OS.WINDOWS : OS.LINUX;
        userhome = properties.getProperty("user.home");
        userdir = properties.getProperty("user.dir");
        username = properties.getProperty("user.name");
        classLoadPath = SystemEnvironment.class.getResource("SystemEnvironment.class").getPath().replace("/system/SystemEnvironment.class", "");
    }

    /**
     * 获取系统ip
     * @return
     */
    public static String ip() {
        return address.getHostAddress();
    }

    /**
     * 获取当前用户名
     * @return
     */
    public static String username() {
//        return envMap.get("USERNAME");
        return username;
    }

    /**
     * 系统名称
     * @return
     */
    public static String osname() {
        return properties.getProperty("os.name");
    }

    /**
     * 系统架构
     * @return
     */
    public static String osarch() {
        return properties.getProperty("os.arch");
    }

    /**
     * 系统版本
     * @return
     */
    public static String osversion() {
        return properties.getProperty("os.version");
    }

    /**
     * 当前目录
     * @return
     */
    public static String userdir() {
        return userdir;
    }

    public static String userhome() {
        return userhome;
    }

    /**
     *  系统类型
     * @return
     */
    public static OS os() {
        return os;
    }

    public static String getClassLoadPath() {
        return classLoadPath;
    }
}

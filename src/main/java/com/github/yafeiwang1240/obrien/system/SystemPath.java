package com.github.yafeiwang1240.obrien.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统操作
 *
 * @author wangyafei
 */
public class SystemPath {

    // 根据操作系统进行区分分割符
    private static final String split;

    static {
        split = SystemEnvironment.os() == SystemEnvironment.OS.WINDOWS ? "\\" : "/";
    }

    /**
     * 系统路径拼接
     * @param values
     * @return
     */
    public static String join(String... values) {
        if (values == null || values.length <= 0) {
            return split;
        }
        int length = values.length;
        StringBuilder builder = new StringBuilder(values[0]);
        for (int i = 1; i < length; i++) {
            String value = values[i];
            String _value = value;
            if (_value == null || _value.equals("")) {
                _value = split;
            } else if (!_value.startsWith(split)) {
                _value = split + _value;
            } else {
                builder.replace(0, builder.length(), "");
            }
            builder.append(_value);
        }
        return builder.toString();
    }

    /**
     * 数组拼接
     * @param split
     * @param value
     * @return
     */
    public static String join(String split, List<String> value) {
        if (value == null) return null;
        int size = value.size();
        if (size <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            builder.append(value.get(i));
            builder.append(split);
        }
        builder.append(value.get(size - 1));
        return builder.toString();
    }

    /**
     * 切割成数组
     * @param value
     * @param split
     * @return
     */
    public static List<String> split(String value, String split) {
        String[] values = value.split(split);
        List<String> result = new ArrayList<>();
        for (String val : values) {
            if (!val.trim().endsWith("")) {
                result.add(val);
            }
        }
        return result;
    }

    /**
     * 文件是否存在
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * mkdir
     * @param path
     * @return
     */
    public static File mkdir(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        if (file.mkdir()) {
            return file;
        }
        return null;
    }

    /**
     * mkdirs
     * @param path
     * @return
     */
    public static File mkdirs(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        }
        if (file.mkdirs()) {
            return file;
        }
        return null;
    }

    /**
     * create if not exist
     * @param path
     * @return
     * @throws IOException
     */
    public static File createNotExist(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}

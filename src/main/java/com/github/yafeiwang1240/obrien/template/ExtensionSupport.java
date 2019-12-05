package com.github.yafeiwang1240.obrien.template;

import com.github.yafeiwang1240.obrien.uitls.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 扩展的辅助方法
 * @author wangyafei
 */
public abstract class ExtensionSupport {

    public static String intercept(String find, String result) {
        // 截取
        if (find.contains("[") && find.contains("]")) {
            String expression = String.valueOf(Arrays.copyOfRange(find.toCharArray(), find.indexOf("[") + 1, find.indexOf("]")));
            int start = 0;
            int end = result.length();
            if (expression.startsWith(":")) {
                String sub = expression.replace(":", "").trim();
                int cut = Integer.parseInt(sub);
                end = Math.min(cut, end);
            } else if (expression.endsWith(":")) {
                String sub = expression.replace(":", "").trim();
                int cut = Integer.parseInt(sub);
                start = Math.min(cut, end);
            } else {
                String[] subs = expression.split(":");
                if (subs != null && subs.length == 2) {
                    start = Math.min(end, Integer.parseInt(subs[0]));
                    end = Math.min(end, Integer.parseInt(subs[1]));
                }
            }
            result = result.substring(start, end);
        }
        if (find.contains("+")) {
            String add = find.split("\\+")[1].trim()
                    .replace("'", "")
                    .replace("\"", "");
            result += add;
        }
        return result;
    }

    public static String spiltValue(String exp) {
        if (StringUtils.isBlank(exp)) return "";
        String[] keyValue = exp.split("=");
        return keyValue[keyValue.length - 1].trim();
    }

    public static <T> T turn(String value, Class<T> clazz) {
        return JsonUtils.parseObject(value, clazz);
    }

    private static Set<String> ignoreMethods = new HashSet<>();
    static {
        ignoreMethods.add("registerNatives");
        ignoreMethods.add("getClass");
        ignoreMethods.add("hashCode");
        ignoreMethods.add("equals");
        ignoreMethods.add("clone");
        ignoreMethods.add("toString");
        ignoreMethods.add("notify");
        ignoreMethods.add("notifyAll");
        ignoreMethods.add("wait");
        ignoreMethods.add("finalize");
    }

    public static boolean ignore(Method method) {
        return ignoreMethods.contains(method.getName());
    }
}

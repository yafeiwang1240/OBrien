package com.github.yafeiwang1240.obrien.uitls;

public class Tools {

    public static String toString(Object data) {
        return toString(data, "");
    }

    public static String toString(Object data, String defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof String) {
            return (String) data;
        }
        return data.toString();
    }

    public static int toInt(Object data) {
        return toInt(data, 0);
    }

    public static int toInt(Object data, int defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof Integer) {
            return (int) data;
        }
        return Integer.parseInt(data.toString());
    }

    public static boolean toBoolean(Object data) {
        return toBoolean(data, false);
    }

    public static boolean toBoolean(Object data, boolean defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof Boolean) {
            return (boolean) data;
        }
        return Boolean.parseBoolean(data.toString());
    }

    public static char toCharacter(Object data) {
        return toCharacter(data, '\0');
    }

    public static char toCharacter(Object data, char defaultValue) {
        if(data == null || data.toString().trim().length() <= 0 || data.toString().trim().length() > 1) {
            return defaultValue;
        }
        if(data instanceof Character) {
            return (char) data;
        }

        return data.toString().charAt(0);
    }

    public static short toShort(Object data) {
        return toShort(data, (short) 0);
    }

    public static short toShort(Object data, short defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof Short) {
            return (short) data;
        }
        return Short.parseShort(data.toString());
    }

    public static long toLong(Object data) {
        return toLong(data, 0L);
    }

    public static long toLong(Object data, long defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof Long) {
            return (long) data;
        }
        return Long.parseLong(data.toString());
    }

    public static double toDouble(Object data) {
        return toDouble(data, 0L);
    }

    public static double toDouble(Object data, double defaultValue) {
        if(data == null) {
            return defaultValue;
        }
        if(data instanceof Double) {
            return (double) data;
        }
        return Double.parseDouble(data.toString());
    }
}

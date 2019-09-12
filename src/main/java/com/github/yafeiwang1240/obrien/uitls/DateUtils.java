package com.github.yafeiwang1240.obrien.uitls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * date utils
 *
 * @author wangyafei
 */
public class DateUtils {

    public static final String FORMAT_DATA_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_SLAT = "MM/dd/yyyy/HH:mm:ss";
    public static final String FORMAT_DATA_TIME_COMPACT = "yyyyMMddHHmmss";
    public static final String FORMAT_DATA_TIME_COMPACT_MS = "yyyyMMddHHmmss.SSS";
    public static final String FORMAT_DATA = "yyyy-MM-dd";
    public static final String FORMAT_DATA_COMPACT = "yyyyMMdd";
    public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss,SSS";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(FORMAT_DATA_TIME);

    private static final Map<String, String> MonthMap = new HashMap<>();

    static {
        MonthMap.put("Jan","01");
        MonthMap.put("Feb","02");
        MonthMap.put("Mar","03");
        MonthMap.put("Apr","04");
        MonthMap.put("May","05");
        MonthMap.put("Jun","06");
        MonthMap.put("Jul","07");
        MonthMap.put("Aug","08");
        MonthMap.put("Sep","09");
        MonthMap.put("Oct","10");
        MonthMap.put("Nov","11");
        MonthMap.put("Dec","12");
    }
    /**
     * now {@link Date}
     * @return
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     *  now time String {@value FORMAT_DATA_TIME}
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String nowTimestamp() {
        return FORMAT.format(now());
    }

    /**
     *  now time String {@value FORMAT_DATA_TIME}
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String toString(Date date) {
        return FORMAT.format(date);
    }

    /**
     * date to String as format
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * parse date
     * @param val
     * @return
     * @throws ParseException
     */
    public static Date parseDate(Object val) throws ParseException {
        if (val == null) {
            return null;
        }
        if (val instanceof Date) {
            return (Date) val;
        }
        if (val instanceof Long) {
            return new Date((Long) val);
        }
        if (val instanceof String) {
            String format = (String) val;
            if (format.length() == FORMAT_DATA_TIME.length()) {
                return FORMAT.parse(format);
            }
            if (format.length() == FORMAT_DATE_SLAT.length()) {
                return new SimpleDateFormat(FORMAT_DATE_SLAT).parse(format);
            }
            if (format.length() == FORMAT_DATA_TIME_COMPACT.length()) {
                return new SimpleDateFormat(FORMAT_DATA_TIME_COMPACT).parse(format);
            }
            if (format.length() == FORMAT_DATA_TIME_COMPACT_MS.length()) {
                return new SimpleDateFormat(FORMAT_DATA_TIME_COMPACT_MS).parse(format);
            }
            if (format.length() == FORMAT_DATA.length()) {
                return new SimpleDateFormat(FORMAT_DATA).parse(format);
            }
            if (format.length() == FORMAT_DATA_COMPACT.length()) {
                return new SimpleDateFormat(FORMAT_DATA_COMPACT).parse(format);
            }
            if (format.length() == FORMAT_TIMESTAMP.length()) {
                return new SimpleDateFormat(FORMAT_TIMESTAMP).parse(format);
            }
            return parseGMT(format);
        }
        throw new IllegalArgumentException("error date str format");
    }

    /**
     * parse date as format
     * @param strDate
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String strDate, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(strDate);
    }

    /**
     * 格林威治时间（GMT） 字符串转Date
     * {@link Date#toString()}
     *
     * @param strDate EEE MMM dd HH:mm:ss zzz yyyy
     */
    public static Date parseGMT(String strDate) throws ParseException {
        if (strDate != null && strDate.trim().length() > 0) {
            String format = strDate.substring(24, 28) + "-"
                    + MonthMap.get(strDate.substring(4, 7)) + "-"
                    + strDate.substring(8, 20);
            return FORMAT.parse(format);
        }
        return null;
    }

}

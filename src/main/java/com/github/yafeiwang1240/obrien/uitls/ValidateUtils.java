package com.github.yafeiwang1240.obrien.uitls;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验类
 *
 * @date 2019-08-12
 * @author wangyafei
 */
public class ValidateUtils {

    public static final String SPECIAL_SYMBOL = "`~!#%^&*=+\\\\|{};:'\\\",<>/?○●★☆☉♀♂※¤╬の〆";

    public static final String SPECIAL_SYMBOL_WITH_SPACE = " `~!#%^&*=+\\\\|{};:'\\\",<>/?○●★☆☉♀♂※¤╬の〆";

    public static final String PHONE_EXPRESSION = "((^(13|15|18|17|19)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^[3-9]{1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-?\\d{7,8}-(\\d{1,4})$))";

    public static final String EMAIL_EXPRESSION= "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    public static final String CHINESE_EXPRESSION = "[\u4e00-\u9fa5]";

    /**
     * 手机号有效性验证
     * @param phoneNumber
     * @return
     */
    public static boolean validPhoneNumber(String phoneNumber) {

        boolean isValid = false;

        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(PHONE_EXPRESSION);

        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }

    /**
     *邮箱有效性验证
     * @param email
     * @return
     */
    public static boolean validEmail(String email) {

        boolean isValid = false;

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(EMAIL_EXPRESSION);

        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()){
            isValid = true;
        }

        return isValid;
    }

    /**
     * 包含中文校验
     * @param chars
     * @return
     */
    public static boolean isContainChinese(String chars) {
        Pattern pattern = Pattern.compile(CHINESE_EXPRESSION);
        Matcher matcher = pattern.matcher(chars);
        if(matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 特殊字符校验
     * @param chars
     * @return
     */
    public static boolean isContainSpecialSymbol(String chars) {
        boolean isContain = false;
        L1: for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < SPECIAL_SYMBOL.length(); j++) {
                if (chars.charAt(i) == SPECIAL_SYMBOL.charAt(j)) {
                    isContain = true;
                    break L1;
                }
            }
        }
        return isContain;
    }

    /**
     * 特殊字符校验（包含空格）
     * @param chars
     * @return
     */
    public static boolean isContainSpecialSymbolWithSpace(String chars) {
        boolean isContain = false;
        L1: for (int i = 0; i < chars.length(); i++) {
            for (int j = 0; j < SPECIAL_SYMBOL_WITH_SPACE.length(); j++) {
                if (chars.charAt(i) == SPECIAL_SYMBOL_WITH_SPACE.charAt(j)) {
                    isContain = true;
                    break L1;
                }
            }
        }
        return isContain;
    }
}

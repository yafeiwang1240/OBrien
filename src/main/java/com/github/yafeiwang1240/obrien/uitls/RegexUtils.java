package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.lang.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    public static String getValue(String content, String regex) {
        if(content == null || regex.isEmpty()) {
            return "";
        }
        Matcher matcher = getMatcher(content, regex);
        if(matcher.find()) {
            return matcher.group(Pattern.UNIX_LINES);
        }
        return "";
    }

    public static boolean matched(String content, String regex) {
        Matcher matcher = getMatcher(content, regex);
        return matcher.find();
    }

    public static List<String> getValues(String context, String regex) {
        Matcher matcher = getMatcher(context, regex);
        List<String> result = Lists.create(ArrayList::new);
        while (matcher.find()) {
            result.add(matcher.group(Pattern.UNIX_LINES));
        }
        return result;
    }

    public static List<String> getOneGroups(String context, String regex) {
        List<String> result = Lists.create(ArrayList::new);
        Matcher matcher = getMatcher(context, regex);
        if(matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }

    public static List<List<String>> getGroups(String context, String regex) {
        List<List<String>> result = Lists.create(ArrayList::new);
        Matcher matcher = getMatcher(context, regex);
        while (matcher.find()) {
            List<String> list = Lists.create(ArrayList::new);
            for(int i = 1; i <= matcher.groupCount(); i++) {
                list.add(matcher.group(i));
            }
            result.add(list);
        }
        return result;
    }

    public static Matcher getMatcher(String content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        return pattern.matcher(content);
    }
}

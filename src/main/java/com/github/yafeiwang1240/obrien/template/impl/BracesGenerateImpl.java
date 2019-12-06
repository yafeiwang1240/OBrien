package com.github.yafeiwang1240.obrien.template.impl;

import com.github.yafeiwang1240.obrien.template.ExtensionSupport;
import com.github.yafeiwang1240.obrien.template.Generate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {}
 *
 * @author wangyafei
 */
public class BracesGenerateImpl extends Generate {

    private static BracesGenerateImpl instance = new BracesGenerateImpl();

    private BracesGenerateImpl() {

    }

    public static BracesGenerateImpl getInstance() {
        return instance;
    }

    @Override
    public String generate(String expression, Map<String, String> parameters) {
        String _expression = expression;
        for(Map.Entry<String, String> entry : parameters.entrySet()) {
            Pattern pattern = Pattern.compile("\\{\\{\\s*" + entry.getKey() + "([^}]*)\\s*}}");
            Matcher matcher = pattern.matcher(_expression);
            while (matcher.find()) {
                String find = matcher.group(0);
                String result = entry.getValue();
                // {{date[:4]}}
                if (matcher.groupCount() > 0) {
                    result = ExtensionSupport.intercept(matcher.group(1), result);
                }
                _expression = _expression.replace(find, result);
            }
        }
        return _expression;
    }
}

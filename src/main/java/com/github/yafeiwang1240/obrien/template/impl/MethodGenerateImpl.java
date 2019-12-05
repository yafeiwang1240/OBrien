package com.github.yafeiwang1240.obrien.template.impl;

import com.github.yafeiwang1240.obrien.template.ExtensionSupport;
import com.github.yafeiwang1240.obrien.template.Generate;
import com.github.yafeiwang1240.obrien.template.ITemplateFunction;
import com.github.yafeiwang1240.obrien.template.exception.GenerateRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MethodGenerateImpl extends Generate {

    private Method method;
    private String name;
    private Class<?>[] parameterTypes;
    private Pattern pattern;


    MethodGenerateImpl(Method method) {
        this.method = method;
        this.name = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.pattern = Pattern.compile("\\{\\{\\s*" + name + "\\(([^)]*)\\)([^}]*)}}",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        this.method.setAccessible(true);
    }

    @Override
    public String generate(String expression, Map<String, String> parameters, ITemplateFunction function) {
        String _expression = expression;
        Matcher matcher = pattern.matcher(_expression);
        while (matcher.find()) {
            String find = matcher.group(0);
            String value = matcher.group(1);
            String result;
            Object[] args = null;
            if (StringUtils.isNotBlank(value)) {
                String[] values = value.split(",");
                args = new Object[parameterTypes.length];
                for (int i = 0; i < values.length; i++) {
                    String v = ExtensionSupport.spiltValue(values[i].trim());
                    if (parameters.containsKey(v)) {
                        v = parameters.get(v);
                    }
                    args[i] = ExtensionSupport.turn(v, parameterTypes[i]);
                }
            }
            try {
                result = (String) method.invoke(function, args);
            } catch (Exception e) {
                throw new GenerateRuntimeException("渲染异常: " + e.getMessage(), e);
            }
            if (matcher.groupCount() > 1) {
                result = ExtensionSupport.intercept(matcher.group(2), result);
            }
            _expression = _expression.replace(find, result);
        }
        return _expression;
    }
}

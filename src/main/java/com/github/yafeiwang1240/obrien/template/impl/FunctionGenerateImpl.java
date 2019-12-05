package com.github.yafeiwang1240.obrien.template.impl;

import com.github.yafeiwang1240.obrien.template.ExtensionSupport;
import com.github.yafeiwang1240.obrien.template.Generate;
import com.github.yafeiwang1240.obrien.template.ITemplateFunction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class FunctionGenerateImpl extends Generate {

    private List<Generate> GenerateList;

    public FunctionGenerateImpl(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        GenerateList = new ArrayList<>(methods.length);
        for (Method method : methods) {
            if (ExtensionSupport.ignore(method)) continue;
            GenerateList.add(new MethodGenerateImpl(method));
        }
    }

    @Override
    public String generate(String expression, Map<String, String> parameters, ITemplateFunction function) {
        String _expression = expression;
        for (Generate generate : GenerateList) {
            _expression = generate.generate(_expression, parameters, function);
        }
        return _expression;
    }
}

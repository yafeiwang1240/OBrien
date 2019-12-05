package com.github.yafeiwang1240.obrien.template.impl;

import com.github.yafeiwang1240.obrien.template.Generate;

import java.util.Map;

/**
 * %
 * @author wangyafei
 */
public class PercentSignGenerateImpl extends Generate {

    @Override
    public String generate(String expression, Map<String, String> parameters) {
        String _expression = expression;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            _expression = _expression.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return _expression;
    }
}

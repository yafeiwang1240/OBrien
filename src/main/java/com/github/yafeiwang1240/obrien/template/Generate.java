package com.github.yafeiwang1240.obrien.template;

import java.util.Map;

public abstract class Generate {

    public String generate(String expression, Map<String, String> parameters) {
        return expression;
    }

    public String generate(String expression, Map<String, String> parameters, ITemplateFunction function) {
        return expression;
    }
}

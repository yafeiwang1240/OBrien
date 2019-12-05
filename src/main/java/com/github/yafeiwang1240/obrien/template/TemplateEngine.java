package com.github.yafeiwang1240.obrien.template;

import com.github.yafeiwang1240.obrien.template.impl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板渲染引擎
 *
 * @date 2019-12-05
 * @author wangyafei
 */
public class TemplateEngine {

    private static final List<Generate> baseGenerate;

    static {
        baseGenerate = new ArrayList<>(4);
        baseGenerate.add(new DoubleDollarGenerateImpl());
        baseGenerate.add(new DollarGenerateImpl());
        baseGenerate.add(new PercentSignGenerateImpl());
        baseGenerate.add(new BracesGenerateImpl());
    }

    private static Map<Class<?>, Generate> functionGenerateCache = new HashMap<>();

    /**
     * 生成函数
     * @param expression
     * @param parameters
     * @param functions
     * @return
     */
    public static String generate(String expression, Map<String, String> parameters, ITemplateFunction... functions) {
        String _expression = expression;
        for (Generate generate : baseGenerate) {
            _expression = generate.generate(expression, parameters);
        }
        if (functions != null && functions.length > 0)  {
            for (ITemplateFunction function : functions) {
                Generate generate = getGenerate(function.getClass());
                _expression = generate.generate(_expression, parameters, function);
            }
        }
        return _expression;
    }

    private static Generate getGenerate(Class<?> clazz) {
        Generate generate = functionGenerateCache.get(clazz);
        if (generate != null) return generate;
        return getAndCacheFunctionGenerate(clazz);
    }

    private static synchronized Generate getAndCacheFunctionGenerate(Class<?> clazz) {
        if (functionGenerateCache.containsKey(clazz)) return functionGenerateCache.get(clazz);
        Generate generate = new FunctionGenerateImpl(clazz);
        functionGenerateCache.put(clazz, generate);
        return generate;
    }
}

package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedInt;
import com.github.yafeiwang1240.obrien.initialization.model.ValueInitialized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntInitializedCreate extends AbstractInitializedCreate<InitializedInt> {

    public IntInitializedCreate(InitializedInt annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() {
        return getInitExecutor(annotation.group(), annotation.value(), annotation.cover());
    }

    public static <T> Map<String, List<Initialized>> getInitExecutor(String group, T value, boolean cover) {
        Map<String, List<Initialized>> result = new HashMap<>();
        List<Initialized> initializeds = new ArrayList<>();
        result.put(group, initializeds);
        initializeds.add(new ValueInitialized<>(value, cover));
        return result;
    }
}

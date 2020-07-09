package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedFloat;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedShort;

import java.util.List;
import java.util.Map;

public class FloatInitializedCreate extends AbstractInitializedCreate<InitializedFloat> {

    public FloatInitializedCreate(InitializedFloat annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() {
        return IntInitializedCreate.getInitExecutor(annotation.group(), annotation.value(), annotation.cover());
    }
}

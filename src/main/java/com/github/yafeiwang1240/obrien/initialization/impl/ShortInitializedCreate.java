package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedShort;

import java.util.List;
import java.util.Map;

public class ShortInitializedCreate extends AbstractInitializedCreate<InitializedShort> {

    public ShortInitializedCreate(InitializedShort annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() {
        return IntInitializedCreate.getInitExecutor(annotation.group(), annotation.value(), annotation.cover());
    }
}

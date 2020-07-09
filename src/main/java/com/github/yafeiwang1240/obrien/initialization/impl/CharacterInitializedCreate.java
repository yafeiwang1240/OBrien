package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedChar;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;

import java.util.List;
import java.util.Map;

public class CharacterInitializedCreate extends AbstractInitializedCreate<InitializedChar> {
    public CharacterInitializedCreate(InitializedChar annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() throws InitializedFailedException {
        return IntInitializedCreate.getInitExecutor(annotation.group(), annotation.value(), annotation.cover());
    }
}

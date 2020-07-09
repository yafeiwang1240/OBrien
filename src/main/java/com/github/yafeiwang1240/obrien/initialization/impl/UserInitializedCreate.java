package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.Initializer;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializerGroup;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.initialization.model.InitializePack;
import com.github.yafeiwang1240.obrien.lang.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInitializedCreate extends AbstractInitializedCreate<Initializer> {

    public UserInitializedCreate(Initializer annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() throws InitializedFailedException {
        return InitializePack.getInitExecutor(annotation);
    }

}

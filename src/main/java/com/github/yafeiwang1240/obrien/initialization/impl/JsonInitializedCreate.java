package com.github.yafeiwang1240.obrien.initialization.impl;

import com.github.yafeiwang1240.obrien.initialization.Initialized;
import com.github.yafeiwang1240.obrien.initialization.annotation.InitializedJson;
import com.github.yafeiwang1240.obrien.initialization.execution.InitializedFailedException;
import com.github.yafeiwang1240.obrien.initialization.model.JsonInitialized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonInitializedCreate extends AbstractInitializedCreate<InitializedJson> {
    public JsonInitializedCreate(InitializedJson annotation) {
        super(annotation);
    }

    @Override
    public Map<String, List<Initialized>> getInitExecutor() throws InitializedFailedException {
        Map<String, List<Initialized>> result = new HashMap<>();
        List<Initialized> initializeds = new ArrayList<>();
        result.put(annotation.group(), initializeds);
        initializeds.add(new JsonInitialized<>(annotation.json(), annotation.cover()));
        return result;
    }
}

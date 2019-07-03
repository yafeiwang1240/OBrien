package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.exception.GroupFailedException;

public interface IGroupFunction<K, T> {
    K getGroup(T o) throws GroupFailedException;
}

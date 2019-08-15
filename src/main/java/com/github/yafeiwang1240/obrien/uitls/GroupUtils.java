package com.github.yafeiwang1240.obrien.uitls;

import com.github.yafeiwang1240.obrien.bean.BeanUtils;
import com.github.yafeiwang1240.obrien.exception.GroupFailedException;
import com.github.yafeiwang1240.obrien.exception.UnMatchedTypeException;
import com.github.yafeiwang1240.obrien.lang.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupUtils {

    public static <K, T> Map<K, List<T>> groupBy(List<T> list, IGroupFunction<K, T> function) throws GroupFailedException {
        GroupMap groupMap = new GroupMap(Maps.create(HashMap::new), function);
        list.forEach(groupMap::group);
        if(groupMap.exception != null) {
            throw groupMap.exception;
        }
        return groupMap.map;
    }

    public static <K, T> Map<K, List<T>> groupBy(List<T> list, String keyField, Class<K> keyType) throws GroupFailedException {
        return groupBy(list, o ->{
            try {
                K _o = BeanUtils.getValue(o, keyField, keyType);
                return _o == null ? null : _o;
            } catch (UnMatchedTypeException e) {
                throw new GroupFailedException(e);
            }
        });
    }

    private static class GroupMap<K, T> {
        protected Map<K, List<T>> map;
        protected IGroupFunction<K, T> function;
        protected GroupFailedException exception;
        public GroupMap(Map<K, List<T>> map, IGroupFunction<K, T> function) {
            this.map = map;
            this.function = function;
        }
        public void group(T o) {
            try {
                K group = function.getGroup(o);
                if (map.containsKey(group)) {
                    map.get(group).add(o);
                } else {
                    List<T> _list = new ArrayList<>();
                    _list.add(o);
                    map.put(group, _list);
                }
            } catch (GroupFailedException e) {
                exception = e;
            }
        }
    }
}

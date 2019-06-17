package com.githup.yafeiwang1240.obrien.fsm;

import com.githup.yafeiwang1240.obrien.exception.UnSupportedEventException;
import com.githup.yafeiwang1240.obrien.lang.Lists;
import com.githup.yafeiwang1240.obrien.lang.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 状态转换中心
 * @param <S>
 * @param <E>
 * @param <C>
 */
public class StatelessMachine<S, E, C> {
    private Map<S, Map<E, MachineElement<S, E, C>>> statusFlows = Maps.create(HashMap::new);
    private boolean failedNotMatch = false;

    protected StatelessMachine(Builder<S, E, C> builder) {
        builder.list.forEach(this::buildMap);
        this.failedNotMatch = builder.failedNotMatch;
    }

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder<>();
    }

    public S emit(S fromStatus, E event, C context) throws Exception{
        if(!statusFlows.containsKey(fromStatus)) {
            if(failedNotMatch) {
                throw new UnSupportedEventException("当前状态下不支持此操作，状态：" + fromStatus + "，操作：" + event);
            } else {
                return fromStatus;
            }
        }
        return statusFlows.get(fromStatus).get(event).handle(context);
    }

    public static class Builder<S, E, C> {
        protected List<MachineElement<S, E, C>> list = Lists.create(ArrayList::new);
        protected boolean failedNotMatch = false;

        public Builder<S, E, C> addFlow(S fromStatus, S toStatus, E event, IEventHandler handler) {
            MachineElement<S, E, C> machineElement = new MachineElement<>(fromStatus, toStatus, event, handler);
            list.add(machineElement);
            return this;
        }

        public Builder<S, E, C> addFlows(List<S> fromStatuses, S toStatus, E event, IEventHandler handler) {
            for(S fromStatus : fromStatuses) {
                addFlow(fromStatus, toStatus, event, handler);
            }
            return this;
        }

        public Builder<S, E, C> setFlows(List<MachineElement<S, E, C>> list) {
            this.list = list;
            return this;
        }

        public Builder<S, E, C> setFailedNotMatch(boolean failedNotMatch) {
            this.failedNotMatch = failedNotMatch;
            return this;
        }

        public StatelessMachine<S, E, C> build() {
            return new StatelessMachine<>(this);
        }
    }

    private void buildMap(MachineElement<S, E, C> element) {
        Map<E, MachineElement<S, E, C>> elementMap;
        if(statusFlows.containsKey(element.getFromStatus())) {
            elementMap = statusFlows.get(element.getFromStatus());
        } else {
            elementMap = Maps.create(HashMap::new);
            statusFlows.put(element.getFromStatus(), elementMap);
        }
        elementMap.put(element.getOnEvent(), element);
    }
}

package com.github.yafeiwang1240.obrien.fsm;

import com.github.yafeiwang1240.obrien.lang.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * 有限状态机
 * @param <S>
 * @param <E>
 * @param <C>
 */
public class FSM<S, E, C> {

    private S currentStatus;
    private C context;
    private StatelessMachine<S, E, C> statelessMachine;

    private FSM(Builder<S, E, C> builder) {
        this.currentStatus = builder.startStatus;
        statelessMachine = new StatelessMachine.Builder<S, E, C>().setFlows(builder.list).build();
    }

    public static <S, E, C> Builder<S, E, C> builder() {
        return new Builder<>();
    }

    public void emit(E event, C context) throws Exception {
        currentStatus = statelessMachine.emit(currentStatus, event, context);
        this.context = context;
    }

    public S status() {
        return currentStatus;
    }

    public C getContext() {
        return context;
    }

    public static class Builder<S, E, C> {
        private S startStatus;
        private boolean failedNotMatch = false;
        private List<MachineElement<S, E, C>> list = Lists.create(ArrayList::new);

        public Builder<S, E, C> setStartStatus(S startStatus) {
            this.startStatus = startStatus;
            return this;
        }

        public Builder<S, E, C> addFlow(S fromStatus, S toStatus, E event, IEventHandler<S, E, C> handler) {
            MachineElement<S, E, C> element = new MachineElement<>(fromStatus, toStatus, event, handler);
            list.add(element);
            return this;
        }

        public Builder<S, E, C> addFlows(List<S> fromStatuses, S to, E event, IEventHandler<S, E, C> handler) {
            for (S from : fromStatuses) {
                addFlow(from, to, event, handler);
            }
            return this;
        }

        public Builder<S, E, C> addAction(S status, E event, IEventHandler<S, E, C> handler) {
            addFlow(status, status, event, handler);
            return this;
        }

        public Builder<S, E, C> addActions(List<S> statuses, E event, IEventHandler<S, E, C> handler) {
            for (S status : statuses) {
                addFlow(status, status, event, handler);
            }
            return this;
        }

        public Builder<S, E, C> setFailedNotMatch(boolean failedNotMatch) {
            this.failedNotMatch = failedNotMatch;
            return this;
        }

        public FSM<S, E, C> build(){
            return new FSM<>(this);
        }
    }
}

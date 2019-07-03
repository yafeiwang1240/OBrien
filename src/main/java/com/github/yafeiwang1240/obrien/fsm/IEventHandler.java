package com.github.yafeiwang1240.obrien.fsm;

public interface IEventHandler<S, E, C> {
    void handle(S fromStatus, S toStatus, E event, C context) throws Exception;
}

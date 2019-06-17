package com.githup.yafeiwang1240.obrien.fsm;

/**
 * 状态机元素
 * @param <S>
 * @param <E>
 * @param <C>
 */
public class MachineElement<S, E, C> {
    private S fromStatus;
    private S toStatus;
    private E onEvent;
    private IEventHandler<S, E, C> handler;

    public MachineElement(S fromStatus, S toStatus, E onEvent, IEventHandler<S, E, C> handler) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.onEvent = onEvent;
        this.handler = handler;
    }

    public boolean match(S currentStatus, E event) {
        if(currentStatus.equals(fromStatus) && event.equals(onEvent)) {
            return true;
        }
        return false;
    }

    public S handle(C content) throws Exception {
        handler.handle(fromStatus, toStatus, onEvent, content);
        return toStatus;
    }

    public S getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(S fromStatus) {
        this.fromStatus = fromStatus;
    }

    public S getToStatus() {
        return toStatus;
    }

    public void setToStatus(S toStatus) {
        this.toStatus = toStatus;
    }

    public E getOnEvent() {
        return onEvent;
    }

    public void setOnEvent(E onEvent) {
        this.onEvent = onEvent;
    }

    public IEventHandler<S, E, C> getHandler() {
        return handler;
    }

    public void setHandler(IEventHandler<S, E, C> handler) {
        this.handler = handler;
    }
}

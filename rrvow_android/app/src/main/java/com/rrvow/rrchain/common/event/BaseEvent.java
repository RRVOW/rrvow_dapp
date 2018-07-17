package com.rrvow.rrchain.common.event;

import java.io.Serializable;

/**
 * @author lpc on 16/7/20.
 */
public class BaseEvent implements Serializable {
    private int eventType;

    public BaseEvent() {
        super();
    }

    public BaseEvent(int eventType) {
        super();
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }
}

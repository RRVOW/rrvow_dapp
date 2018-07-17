package com.rrvow.rrchain.common.event;

/**
 * yanweiqiang
 * 2018/2/6.
 */

public class SpecEvent extends BaseEvent {
    public static final int SPEC_CHANGED = 0;

    public SpecEvent(int eventType) {
        super(eventType);
    }
}

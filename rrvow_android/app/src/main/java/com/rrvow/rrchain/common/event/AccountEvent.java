package com.rrvow.rrchain.common.event;

/**
 * yanweiqiang
 * 2018/3/2.
 */

public class AccountEvent extends BaseEvent {

    public static final int REFRESHED = 0;

    public AccountEvent(int eventType) {
        super(eventType);
    }
}

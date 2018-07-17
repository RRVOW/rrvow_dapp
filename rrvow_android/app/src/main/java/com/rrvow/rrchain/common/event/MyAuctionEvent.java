package com.rrvow.rrchain.common.event;

/**
 * yanweiqiang
 * 2018/1/29.
 */

public class MyAuctionEvent extends BaseEvent {

    public static final int depositChanged = 0;

    public MyAuctionEvent(int eventType) {
        super(eventType);
    }
}

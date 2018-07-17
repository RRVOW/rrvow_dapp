package com.rrvow.rrchain.common.event;

/**
 * yanweiqiang
 * 2018/2/6.
 */

public class SearchGoodsEvent extends BaseEvent {
    public static final int DO_SEARCH = 0;

    public SearchGoodsEvent(int eventType) {
        super(eventType);
    }

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}

package com.rrvow.rrchain.common.event;

/**
 * @author by lpc on 2018/1/29.
 */
public class JumpMainEvent extends BaseEvent {

    int page;

    public JumpMainEvent(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

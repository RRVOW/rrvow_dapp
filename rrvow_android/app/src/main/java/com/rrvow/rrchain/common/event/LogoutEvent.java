package com.rrvow.rrchain.common.event;

/**
 * Created by shy on 2017/9/27 0027.
 */

public class LogoutEvent extends BaseEvent {
    private String message;

    public LogoutEvent(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

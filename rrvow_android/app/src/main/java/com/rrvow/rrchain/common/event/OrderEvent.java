package com.rrvow.rrchain.common.event;

/**
 * @author by lpc on 2017/11/20.
 */
public class OrderEvent extends BaseEvent {

    private int currentItem;

    public OrderEvent(int currentItem) {
        this.currentItem = currentItem;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
}

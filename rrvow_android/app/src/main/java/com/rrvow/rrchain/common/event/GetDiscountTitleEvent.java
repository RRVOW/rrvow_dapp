package com.rrvow.rrchain.common.event;

/**
 * @author by lpc on 2018/2/28.
 */
public class GetDiscountTitleEvent extends BaseEvent {

    private String title;
    private String discountId;

    public GetDiscountTitleEvent(String title, String discountId) {
        this.title = title;
        this.discountId = discountId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }
}

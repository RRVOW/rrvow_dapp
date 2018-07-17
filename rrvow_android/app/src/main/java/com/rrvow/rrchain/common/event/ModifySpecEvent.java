package com.rrvow.rrchain.common.event;

/**
 * @author by lpc on 2018/2/7.
 */
public class ModifySpecEvent extends BaseEvent {

    private String goods_id;
    private String product_id;
    private String quantity;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

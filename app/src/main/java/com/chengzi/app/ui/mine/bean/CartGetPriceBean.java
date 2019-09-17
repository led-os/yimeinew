package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class CartGetPriceBean {

    /**
     * total_amount : 71134
     * discount_amount : 80
     * coupon_id : 5
     */

    private String total_amount = "0.00";        //合计金额
    private String discount_amount = "0.00";     //优惠金额
    private String coupon_id = "0.00";           //优惠券id

    public CartGetPriceBean(String total_amount, String discount_amount) {
        this.total_amount = total_amount;
        this.discount_amount = discount_amount;
    }

    public String getTotal_amount() {

        return !TextUtils.isEmpty(total_amount) ? total_amount : "0.00";
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDiscount_amount() {
        return !TextUtils.isEmpty(discount_amount) ? discount_amount : "0.00";
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }
}

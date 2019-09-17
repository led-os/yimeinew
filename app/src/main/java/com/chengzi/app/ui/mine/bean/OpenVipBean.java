package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class OpenVipBean {

    /**
     * id : 2
     * product_name : 体验一天
     * price : 0.98
     * product_type : 1
     */

    private String id;
    private String product_name;
    private String price;
    private String product_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return !TextUtils.isEmpty(product_name) ? product_name : "";
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPrice() {
        return !TextUtils.isEmpty(price) ? price : "0";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
}

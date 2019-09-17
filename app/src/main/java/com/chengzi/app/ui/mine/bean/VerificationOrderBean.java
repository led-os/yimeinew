package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class VerificationOrderBean implements Serializable {

    /**
     * type : 2
     * order_id : 2
     * order_number_id : CZ_MRC425905532708545
     * goods_id : null
     * goods_number : 1
     * goods_image :
     * goods_name : 面部轮廓美人筹名称5
     * amount_pay : 300.00
     * amount_spreads : 0
     * amount_total : 300.00
     */

    private String type;            // 订单类型，1-普通，拼购，秒杀 2-美人筹
    private String order_id;        // 订单id
    private String order_number_id; // 订单编号
    private String goods_id;        // 商品id，美人筹订单，该字段为null
    private String goods_number;    // 购买数量，目前都为1
    private String goods_image;     // 商品封面，美人筹订单，该字段为空，使用给的默认图
    private String goods_name;      // 商品名称
    private String amount_pay;      // 订单价格
    private String amount_spreads;  // 补差价
    private String amount_total;    // 支付总价（订单价 + 补差价）

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number_id() {
        return !TextUtils.isEmpty(order_number_id) ? order_number_id : "";
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_number() {
        return !TextUtils.isEmpty(goods_number) ? goods_number : "0";
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_name() {
        return !TextUtils.isEmpty(goods_name) ? goods_name : "";
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getAmount_pay() {
        return !TextUtils.isEmpty(amount_pay) ? amount_pay : "0.00";
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
    }

    public String getAmount_spreads() {
        return !TextUtils.isEmpty(amount_spreads) ? amount_spreads : "0.00";
    }

    public void setAmount_spreads(String amount_spreads) {
        this.amount_spreads = amount_spreads;
    }

    public String getAmount_total() {
        return !TextUtils.isEmpty(amount_total) ? amount_total : "0.00";
    }

    public void setAmount_total(String amount_total) {
        this.amount_total = amount_total;
    }
}

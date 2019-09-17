package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpreadsOrderBean implements Serializable {

    /**
     * prepaylog_id : 130
     * spreads_number : CZ_BC601549354881258
     * user_id : 26
     * order_id : 179
     * spreads : 1
     * payment_type : 1
     * create_time : 1559354935
     */

    private String prepaylog_id;   // 发起支付id
    private String spreads_number; // 补差价订单编号
    private String user_id;        // 用户id
    private String order_id;       // 原订单id
    @SerializedName(value = "spreads", alternate = {"price"})
    private String spreads;        // 补差价金额（单位元）
    private String payment_type;   // 支付方式 1-微信  2-支付宝
    private String create_time;    // 发起补差价时间戳（秒）

    public String getPrepaylog_id() {
        return prepaylog_id;
    }

    public void setPrepaylog_id(String prepaylog_id) {
        this.prepaylog_id = prepaylog_id;
    }

    public String getSpreads_number() {
        return !TextUtils.isEmpty(spreads_number) ? spreads_number : "";
    }

    public void setSpreads_number(String spreads_number) {
        this.spreads_number = spreads_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSpreads() {
        return !TextUtils.isEmpty(spreads) ? spreads : "0.00";
    }

    public void setSpreads(String spreads) {
        this.spreads = spreads;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}

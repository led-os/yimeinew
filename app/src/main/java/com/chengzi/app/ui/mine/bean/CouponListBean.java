package com.chengzi.app.ui.mine.bean;

public class CouponListBean {
    /**
     * id : 1
     * amount : 5
     * full_amount : 100
     * end_time : 2019-04-30
     */

    private String id;                // 优惠券id
    private double amount;         // 减多少
    private double full_amount;    // 满多少
    private String end_time;       // 有效期截止时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFull_amount() {
        return full_amount;
    }

    public void setFull_amount(double full_amount) {
        this.full_amount = full_amount;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

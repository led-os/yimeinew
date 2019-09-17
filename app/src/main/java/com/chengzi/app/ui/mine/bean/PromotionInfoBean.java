package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class PromotionInfoBean {

    /**
     * balance : 489.50
     * promotion_price : 1.50
     * total_recharge : null
     */

    private String balance = "0.00";         // 当前余额
    private String promotion_price = "0.00"; // 推广价格
    private String total_recharge = "0.00";  // 已充值金额

    public String getBalance() {
        return !TextUtils.isEmpty(balance) ? balance : "0.00";
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPromotion_price() {
        return !TextUtils.isEmpty(promotion_price) ? promotion_price : "0.00";
    }

    public void setPromotion_price(String promotion_price) {
        this.promotion_price = promotion_price;
    }

    public String getTotal_recharge() {
        return !TextUtils.isEmpty(total_recharge) ? total_recharge : "0.00";
    }

    public void setTotal_recharge(String total_recharge) {
        this.total_recharge = total_recharge;
    }
}

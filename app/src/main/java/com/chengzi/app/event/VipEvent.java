package com.chengzi.app.event;

public class VipEvent {

    private String vipCategoryId;

    public VipEvent(String vipCategoryId) {
        this.vipCategoryId = vipCategoryId;
    }

    public String getVipCategoryId() {
        return vipCategoryId;
    }
}

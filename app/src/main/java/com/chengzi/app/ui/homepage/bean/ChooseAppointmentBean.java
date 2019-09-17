package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

public class ChooseAppointmentBean {
    private String is_update;   //数据有没有更新        1-成功 0-失败
    private String is_exist;    //当前预约的时间是否存在 1-成功 0-失败

    public String getIs_update() {
        return !TextUtils.isEmpty(is_update) ? is_update : "0";
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }

    public String getIs_exist() {
        return !TextUtils.isEmpty(is_exist) ? is_exist : "0";
    }

    public void setIs_exist(String is_exist) {
        this.is_exist = is_exist;
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class IsSignBean {

    /**
     * is_sign : 0
     */

    private String is_sign;     // 是否已签到，0-未签到 1-已签到

    public String getIs_sign() {
        return !TextUtils.isEmpty(is_sign) ? is_sign : "0";
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }
}

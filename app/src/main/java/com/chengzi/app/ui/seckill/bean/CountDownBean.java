package com.chengzi.app.ui.seckill.bean;

import android.text.TextUtils;

public class CountDownBean {

    /**
     * time : 1559210400
     */

    private String time;  //剩余时间倒计时(秒为单位)

    public String getTime() {
        return !TextUtils.isEmpty(time) ? time : "0";
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.chengzi.app.ui.seckill.bean;

import android.text.TextUtils;

import com.handong.framework.base.PageBean;

public class KillListBean {
    private PageBean<KillIndexBean> data;
    private String start_time;    //status =0 距开始
    private String end_time;      //status =1 距结束

    public PageBean<KillIndexBean> getData() {
        return data;
    }

    public void setData(PageBean<KillIndexBean> data) {
        this.data = data;
    }

    public String getStart_time() {
        return !TextUtils.isEmpty(start_time) ? start_time : "0";
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return !TextUtils.isEmpty(end_time) ? end_time : "0";
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

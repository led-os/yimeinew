package com.chengzi.app.ui.goods.bean;

import java.io.Serializable;

public class SpellBean implements Serializable {

    private static final long serialVersionUID = 868345258112189L;

    private String order_id;       // 拼购订单id
    private String user_id;        // 发起拼购者id
    private String user_name;   // 发起拼购者姓名
    private String user_logo;   // 发起拼购者头像
    private long remain_time;     // 拼购剩余时间（剩余秒数）

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public long getRemain_time() {
        return remain_time;
    }

    public void setRemain_time(long remain_time) {
        this.remain_time = remain_time;
    }
}

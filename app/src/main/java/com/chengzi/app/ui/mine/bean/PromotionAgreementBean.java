package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.chengzi.app.constants.Sys;

public class PromotionAgreementBean {
    /**
     * content :
     */

    private String content;

    //用户使用协议
    private String title;
    private String user_type;

    public String getTitle() {
        if (!TextUtils.isEmpty(title)) {
            return title;
        } else {
            int type = getUser_type_int();
            if (type == Sys.TYPE_USER) {    //用户
                return "用户使用协议";
            } else if (type == Sys.TYPE_DOCTOR) {    //医生
                return "医生使用协议";
            } else if (type == Sys.TYPE_COUNSELOR) {    //咨询师
                return "咨询师使用协议";
            } else if (type == Sys.TYPE_HOSPITAL) {    //医院
                return "医院使用协议";
            }
        }
        return "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_type() {
        return !TextUtils.isEmpty(user_type) ? user_type : "0";
    }

    public int getUser_type_int() {
        return Integer.parseInt(getUser_type());
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class ExtensionRankBean {

    /**
     * id : 16
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * user_name : 咨询师一号
     * num : 472
     */

    private String id;        // 用户id
    private String image;     // 用户头像
    private String user_name; // 用户名称
    private String num;       // 推广金额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNum() {
        return !TextUtils.isEmpty(num) ? num : "0";
    }

    public void setNum(String num) {
        this.num = num;
    }
}

package com.chengzi.app.ui.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PromoteRoutingBean implements Serializable {

    /**
     * id : 14
     * type : 2
     * name : 笑笑二号
     * mobile : 13269961792
     * binding_name : 北京三甲医院
     * binding_id : 8
     * binding_img : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * occupation_name : 主治医师
     */

    private String id;      // 用户id
    private String type;    // 用户类型
    private String name;    // 用户名称
    private String mobile;  // 推荐手机号
    @SerializedName(value = "image")
    private String user_image;  // 用户头像
    private String binding_name;    // 绑定机构名称
    private String binding_id;      // 绑定机构id
    private String binding_img;     // 绑定机构logo
    private String occupation_name; // 咨询师等级

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getBinding_name() {
        return binding_name;
    }

    public void setBinding_name(String binding_name) {
        this.binding_name = binding_name;
    }

    public String getBinding_id() {
        return binding_id;
    }

    public void setBinding_id(String binding_id) {
        this.binding_id = binding_id;
    }

    public String getBinding_img() {
        return binding_img;
    }

    public void setBinding_img(String binding_img) {
        this.binding_img = binding_img;
    }

    public String getOccupation_name() {
        return occupation_name;
    }

    public void setOccupation_name(String occupation_name) {
        this.occupation_name = occupation_name;
    }
}

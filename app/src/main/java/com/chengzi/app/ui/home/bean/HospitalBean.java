package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.utils.ScoreUtils;

public class HospitalBean {

    @SerializedName(value = "id", alternate = {"user_id"})
    private String id;

    @SerializedName(value = "name", alternate = {"user_name"})
    private String name;    //  医院名称

    private String image;   //  医院头像
    private String grade;   // 机构评分
    private String address; // 机构地址
    private String city_name;  //  所在城市
    private String mobile;

    private String begoodat;  // 擅长id

    @SerializedName(value = "begoodatString", alternate = {"major_text"})
    private String begoodatString;  // 擅长名称
    @SerializedName(value = "orderNum", alternate = {"order_quantity"})
    private String orderNum;   // 下单数量
    private float distance;   //  距离

    @SerializedName(value = "is_Vip", alternate = {"is_VIP"})
    private int is_Vip;           // 是否是vip   0 否 1是
    //    private int is_extension;       // 是否推广   是否推广 1-是 2-否
    private String promotion_id;    //   推广id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGrade() {
        return ScoreUtils.processScore(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getIs_Vip() {
        return is_Vip;
    }

    public void setIs_Vip(int is_Vip) {
        this.is_Vip = is_Vip;
    }

    public int getIs_extension() {
        if (TextUtils.isEmpty(promotion_id)) {
            return 0;
        }
        long id = Long.valueOf(promotion_id);
        return id > 0 ? 1 : 0;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBegoodat() {
        return begoodat;
    }

    public void setBegoodat(String begoodat) {
        this.begoodat = begoodat;
    }

    public String getBegoodatString() {
        return begoodatString == null ? "" : begoodatString;
    }

    public void setBegoodatString(String begoodatString) {
        this.begoodatString = begoodatString;
    }

    public String getOrderNum() {
        return orderNum == null ? "0" : orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }
}

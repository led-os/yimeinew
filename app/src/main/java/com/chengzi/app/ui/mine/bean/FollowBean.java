package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class FollowBean implements Serializable {

    private static final long serialVersionUID = 8683452581124389L;

    private String user_id;     // 用户id
    private String name;        // 用户名称
    private String image;       // 用户头像
    private int is_VIP;      // 是否是vip 0-不是 1-是
    private String grade;       // 用户评分
    private String level_name;  // 用户职称
    private String city_id;     // 用户所属城市id
    private String gender;      // 用户性别 0-女 1-男
    private String birthday;    // 用户生日，字符串
    private int is_followed; // 0-未关注 1-已关注 2-自己
    private int user_type;  // 1-普通用户 2-医生 3-咨询师 4-机构

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    // 0-未关注 1-已关注 2-自己
    public int getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(int is_followed) {
        this.is_followed = is_followed;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
}

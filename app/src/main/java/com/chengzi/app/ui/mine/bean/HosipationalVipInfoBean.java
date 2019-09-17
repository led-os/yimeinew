package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class HosipationalVipInfoBean {


    /**
     * user_id : 3
     * user_name : 阿姐
     * image : ["http://www.vue_admin.com/public/uploads/2019-04-12/5cb00bbd0bf3b.jpg"]
     * is_VIP : 0
     * grade : 8.0
     * bind_vip : 1
     * rank : 普通咨询师
     * occupation : 主治医师
     */

    private String user_id;     // 用户id
    private String user_name;   // 名称
    private String is_VIP;      // 本身是否是vip 0 否 1是
    private String grade;       // 评分
    private String bind_vip;    // 绑定机构vip状态-->0绑定机构未开通VIP 1绑定机构开通VIP
    private String rank;        // 咨询师等级
    private String occupation;  // 医生职称
    private  String  image;       // 头像/LOGO
    private  String  type;       // 头像/LOGO
    private  String  mobile;       // 头像/LOGO


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

    public String getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(String is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBind_vip() {    // 绑定机构vip状态-->0绑定机构未开通VIP 1绑定机构开通VIP
        return !TextUtils.isEmpty(bind_vip) ? bind_vip.trim() : "0";
    }

    public void setBind_vip(String bind_vip) {
        this.bind_vip = bind_vip;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }


}

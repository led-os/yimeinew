package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class MyAppointmentBean {

    /**
     * id : 10
     * type : 2
     * user_id : 1
     * to_uid : 8
     * appointed_time : 2019.04.20 15:00
     * appointment_name : 美女一号
     * appointment_phone : 18810882488
     * content : 想做八眼皮
     * uu_image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * uu_name : 北京三甲医院
     * uu_gender : null
     * uu_grade : 6.4
     */

    private String id;            // 预约id
    private String type;          // 预约类型 1-用户 2-医生 3-咨询师 4-机构'
    private String user_id;       // 预约者用户id
    private String to_uid;        // 被预约者用户id
    private String appointed_time;      // 预约时间
    private String appointment_name;    // 预约者姓名
    private String appointment_phone;   // 预约者联系电话
    private String content;    // 预约内容
    private String uu_image;   // 我的预约-显示被预约者头像，预约我的-显示预约者头像
    private String uu_name;    // 我的预约-显示被预约者姓名，预约我的-显示预约者姓名
    private String uu_gender;  //我的预约-显示被预约者性别，预约我的-显示预约者性别 0女 1男
    private String uu_grade;   //我的预约-显示被预约者评分，预约我的-显示预约者评分
    private String uu_level;   //职称

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getAppointed_time() {
        return appointed_time;
    }

    public void setAppointed_time(String appointed_time) {
        this.appointed_time = appointed_time;
    }

    public String getAppointment_name() {
        return appointment_name;
    }

    public void setAppointment_name(String appointment_name) {
        this.appointment_name = appointment_name;
    }

    public String getAppointment_phone() {
        return appointment_phone;
    }

    public void setAppointment_phone(String appointment_phone) {
        this.appointment_phone = appointment_phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUu_image() {
        return uu_image;
    }

    public void setUu_image(String uu_image) {
        this.uu_image = uu_image;
    }

    public String getUu_name() {
        return uu_name;
    }

    public void setUu_name(String uu_name) {
        this.uu_name = uu_name;
    }

    public String getUu_gender() {
        return !TextUtils.isEmpty(uu_gender) ? uu_gender : "0";
    }

    public void setUu_gender(String uu_gender) {
        this.uu_gender = uu_gender;
    }

    public String getUu_grade() {
        return uu_grade;
    }

    public void setUu_grade(String uu_grade) {
        this.uu_grade = uu_grade;
    }

    public String getUu_level() {
        return uu_level;
    }

    public void setUu_level(String uu_level) {
        this.uu_level = uu_level;
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class SheetListBean implements Serializable {

    private String id;        // id
    private String name;      // 昵称
    private String grade;     // 评分
    private String gender;    // 性别 0女 1男
    private String image;     // 头像
    private String is_VIP;    // VIP （ 0 否 1是）
    private String cate_name; // 分类名称
    private String age;       // 年龄

    private String user_id;     // 用户id',
    private String yunxin_accid;    //  云信id
    private String cate_id;     // 分类id（一级分类）
    private String sheet_type;  // 咨询类型 （1-私享咨询 2-在线面诊）
    private String start_time;  // 发起时间
    private String end_time;    // 过期时间
    private String is_grab;     // 是否有人抢单 (1-是 2-否)
    private String is_chose;    // 是否选择咨询对象 (1-是 2-否)
    private String is_cancel;   // 是否手动取消 (1-是 2-否)
    private String answer_id;   // 被选id
    private String update_time; // 更新时间


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

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "0";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(String is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getCate_name() {
        return !TextUtils.isEmpty(cate_name) ? cate_name : "";
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getSheet_type() {
        return sheet_type;
    }

    public void setSheet_type(String sheet_type) {
        this.sheet_type = sheet_type;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIs_grab() {
        return is_grab;
    }

    public void setIs_grab(String is_grab) {
        this.is_grab = is_grab;
    }

    public String getIs_chose() {
        return is_chose;
    }

    public void setIs_chose(String is_chose) {
        this.is_chose = is_chose;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }
}

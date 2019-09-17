package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.ScoreUtils;

public class HospitalInfoBean {

    private String id;               // 医院id
    private String name;             // 医院名称
    private String headimg;
    private String cover;   //封面图
    private String mobile;          // 手机
    private String telephone;       // 固定电话
    private String address;         // 医院地址
    private String grade;           // 专业分
    private String skill_grade;      // 技术分/服务分
    private int type;               // 用户类型 1-用户 2-医生 3-咨询师 4-机构
    private String occupation_name;
    private String consultant_name;
    private boolean is_frozen;      // 是否禁用
    private boolean is_vip;         // 是否VIP
    private boolean is_auth;        // 是否通过审核
    private boolean is_auth_push;   // 是否提交审核
    private boolean is_auth_dis;    // 是否被拒绝审核
    private String synopsis;       //医院介绍

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNameAndId() {
        return name + "(ID:" + id + ")";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address == null ? (address = "") : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrade() {
        return ScoreUtils.processScore(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSkill_grade() {
        return ScoreUtils.processScore(skill_grade);
    }

    public void setSkill_grade(String skill_grade) {
        this.skill_grade = skill_grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOccupation_name() {
        return occupation_name;
    }

    public void setOccupation_name(String occupation_name) {
        this.occupation_name = occupation_name;
    }

    public String getConsultant_name() {
        return consultant_name;
    }

    public void setConsultant_name(String consultant_name) {
        this.consultant_name = consultant_name;
    }

    public boolean isIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(boolean is_frozen) {
        this.is_frozen = is_frozen;
    }

    public boolean isIs_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public boolean isIs_auth() {
        return is_auth;
    }

    public void setIs_auth(boolean is_auth) {
        this.is_auth = is_auth;
    }

    public boolean isIs_auth_push() {
        return is_auth_push;
    }

    public void setIs_auth_push(boolean is_auth_push) {
        this.is_auth_push = is_auth_push;
    }

    public boolean isIs_auth_dis() {
        return is_auth_dis;
    }

    public void setIs_auth_dis(boolean is_auth_dis) {
        this.is_auth_dis = is_auth_dis;
    }

    public String getSynopsis() {
        return !TextUtils.isEmpty(synopsis) ? synopsis : "";
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
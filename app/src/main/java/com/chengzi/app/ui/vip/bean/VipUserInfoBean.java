package com.chengzi.app.ui.vip.bean;

import android.text.TextUtils;

public class VipUserInfoBean {

    private String id;
    private String name;
    private String headimg;
    private String mobile;
    private String telephone;
    private String address;
    private String grade;
    private String skill_grade;
    private int type;
    private String occupation_name;
    private String consultant_name;
    private String level;   //  职级名称
    private String hospitalName;    //  绑定机构的名称
    private String gender;
    private String age;
    private String vip_endtime;
    private boolean is_frozen;
    private boolean is_vip;
    private boolean is_auth;
    private boolean is_auth_push;
    private boolean is_auth_dis;

    private VipUserInfoBean hospital;

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

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
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
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSkill_grade() {
        return skill_grade;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVip_endtime() {
        return !TextUtils.isEmpty(vip_endtime) ? vip_endtime : "";
    }

    public void setVip_endtime(String vip_endtime) {
        this.vip_endtime = vip_endtime;
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

    public VipUserInfoBean getHospital() {
        return hospital;
    }

    public void setHospital(VipUserInfoBean hospital) {
        this.hospital = hospital;
    }

    public String getLevel() {
        return type == 2 ? occupation_name : consultant_name;
    }

    public String getHospitalName() {
        return hospital != null ? hospital.getName() : "";
    }
}

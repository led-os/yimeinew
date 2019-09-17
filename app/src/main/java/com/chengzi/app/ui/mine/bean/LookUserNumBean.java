package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class LookUserNumBean {

    /**
     * id : 1
     * name : 阿布
     * headimg : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=1.jpg
     * mobile : 15801629092
     * telephone : null
     * address : null
     * grade : 5.2
     * skill_grade : 10.0
     * type : 1
     * occupation_name :
     * consultant_name : VIP咨询师
     * is_frozen : true
     * is_vip : true
     * is_auth : true
     * is_auth_push : false
     * is_auth_dis : false
     */

    private String id;
    private String name;
    private String headimg;
    private String mobile;
    private String telephone;
    private String address;
    private String grade;
    private String skill_grade;
    private String type;
    private String occupation_name;
    private String consultant_name;
    private boolean is_frozen;
    private boolean is_vip;
    private boolean is_auth;
    private boolean is_auth_push;
    private boolean is_auth_dis;


    private String age;
    private String gender;

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "0";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


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
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSkill_grade() {
        return !TextUtils.isEmpty(skill_grade) ? skill_grade : "0.0";
    }

    public void setSkill_grade(String skill_grade) {
        this.skill_grade = skill_grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

}

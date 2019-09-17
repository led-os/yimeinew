package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.utils.ScoreUtils;

import java.io.Serializable;
import java.util.List;

public class DoctorHomeInfoBean implements Serializable {

    private String id;
    private String username;
    private String usericon;
    private int type;          //  用户角色类型  2 医生   3 咨询师
    private String mobile;      //  医生/咨询师 电话
    private String occupation;
    private String work_year;
    private String school;           //     学校
    private String qualnumber;       //     资质编号
    private String grade;             // 评分/医生咨询师专业分
    private String skill_grade;      //     医生技术分/咨询师服务分
    private String major_text;

    private int follow;     //      是否关注  1 是  0 否

    @SerializedName(value = "service_organization_info")
    private BelongOrgBean belongOrg;        //  所属机构
    private String order_num;           //  下单量
    private String order_transaction_amounth;   //  成交总额
    private String appointment_num; //  预约量
    private String find_num;        //  发表量
    private String fans_num;        //  粉丝量
    private String follor_num;      //  关注量
    private String answer_num;      //  回答量
    private String sheet_num;     //  咨询量
    private AppointmentManageBean appointment_manage;
    private List<MajorsBean> majors;

    private String cover;   //  主页封面
    private String yunxin_accid;    //  云信id

    private String synopsis;       //医生/咨询师介绍
    private String online_state;    //  在线状态 1-在线 2-离线 3-忙碌

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return !TextUtils.isEmpty(username) ? username : "";
    }

    public String getUserNameAndId() {
        return getUsername() + "(ID:" + id + ")";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWork_year() {
        return work_year == null ? "" : work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getSchool() {
        return school == null ? "" : school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getQualnumber() {
        return qualnumber == null ? (qualnumber = "") : qualnumber;
    }

    public void setQualnumber(String qualnumber) {
        this.qualnumber = qualnumber;
    }

    public String getSkill_grade() {
        return ScoreUtils.processScore(skill_grade);
    }

    public void setSkill_grade(String skill_grade) {
        this.skill_grade = skill_grade;
    }

    public String getMajor_text() {
        return major_text == null ? "" : major_text;
    }

    public void setMajor_text(String major_text) {
        this.major_text = major_text;
    }

    public BelongOrgBean getBelongOrg() {
        return belongOrg;
    }

    public void setBelongOrg(BelongOrgBean belongOrg) {
        this.belongOrg = belongOrg;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_transaction_amounth() {
        return order_transaction_amounth;
    }

    public void setOrder_transaction_amounth(String order_transaction_amounth) {
        this.order_transaction_amounth = order_transaction_amounth;
    }

    public String getAppointment_num() {
        return !TextUtils.isEmpty(appointment_num) ? appointment_num : "0";
    }

    public void setAppointment_num(String appointment_num) {
        this.appointment_num = appointment_num;
    }

    public String getFind_num() {
        return !TextUtils.isEmpty(find_num) ? find_num : "0";
    }

    public void setFind_num(String find_num) {
        this.find_num = find_num;
    }

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getFollor_num() {
        return follor_num;
    }

    public void setFollor_num(String follor_num) {
        this.follor_num = follor_num;
    }

    public String getAnswer_num() {
        return answer_num;
    }

    public void setAnswer_num(String answer_num) {
        this.answer_num = answer_num;
    }

    public AppointmentManageBean getAppointment_manage() {
        return appointment_manage;
    }

    public void setAppointment_manage(AppointmentManageBean appointment_manage) {
        this.appointment_manage = appointment_manage;
    }

    public List<MajorsBean> getMajors() {
        return majors;
    }

    public void setMajors(List<MajorsBean> majors) {
        this.majors = majors;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGrade() {
        return ScoreUtils.processScore(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSheet_num() {
        return sheet_num;
    }

    public void setSheet_num(String sheet_num) {
        this.sheet_num = sheet_num;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public static class MajorsBean implements Serializable {

        private String id;
        private String name;

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
    }

    public String getSynopsis() {
        return !TextUtils.isEmpty(synopsis) ? synopsis : "";
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getOnline_state() {
        return online_state;
    }

    public void setOnline_state(String online_state) {
        this.online_state = online_state;
    }
}
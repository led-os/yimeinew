package com.chengzi.app.ui.homepage.bean;

import com.chengzi.app.utils.ScoreUtils;

import java.util.List;

public class EvaluateBean {

    private String id;
    private String username;
    private String usericon;
    private String doctor_id;           //  被评价医生id
    private String consultant_id;       //  被评价咨询师id
    private String doctor_skill;       //  医生技术分
    private String doctor_major;       //  医生专业分
    private String content;
    private String create_time;
    private String update_time;
    private String consultant_service; //  咨询师服务分
    private String consultant_major;   //  咨询师专业分
    private String user_grade;         //  用户评分
    private List<String> image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
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

    public String getDoctor_skill() {
        return ScoreUtils.processScore(doctor_skill);
    }

    public void setDoctor_skill(String doctor_skill) {
        this.doctor_skill = doctor_skill;
    }

    public String getDoctor_major() {
        return ScoreUtils.processScore(doctor_major);
    }

    public void setDoctor_major(String doctor_major) {
        this.doctor_major = doctor_major;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getConsultant_service() {
        return ScoreUtils.processScore(consultant_service);
    }

    public void setConsultant_service(String consultant_service) {
        this.consultant_service = consultant_service;
    }

    public String getConsultant_major() {
        return ScoreUtils.processScore(consultant_major);
    }

    public void setConsultant_major(String consultant_major) {
        this.consultant_major = consultant_major;
    }

    public String getUser_grade() {
        return user_grade;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public void setConsultant_id(String consultant_id) {
        this.consultant_id = consultant_id;
    }

    public void setUser_grade(String user_grade) {
        this.user_grade = user_grade;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}

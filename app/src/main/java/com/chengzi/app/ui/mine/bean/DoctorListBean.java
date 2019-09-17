package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

/**
 * @Desc:
 * @ClassName:DoctorBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/28 0028
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class DoctorListBean {
    /**
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * true_name : 2121
     * name : 主治医师
     * status : 2
     * type : null
     * user_id : 10
     * grade : 6.3
     * skill_grade : 4.0
     */

    private String image;           // 医生/咨询师头像
    private String true_name;       // 昵称
    private String name;            // 职称
    private String status;          // 状态 0-待审核 1-已通过 2-已拒绝 ',
    private String type;            // '绑定状态 1-已绑定 2-已解绑',
    private String user_id;         // 医生/咨询师id
    private String grade;           // 专业评分
    private String skill_grade;     // 服务分
    private String occupation_name; // 医生职称
    private String consultant_name; // 咨询师职称
    private String yunxin_accid;
    private String yunxin_token;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTrue_name() {
        return !TextUtils.isEmpty(true_name) ? true_name : "";
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getName() {
        return !TextUtils.isEmpty(name) ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {     //'状态 0-待审核 1-已通过 2-已拒绝 ',
        return !TextUtils.isEmpty(status) ? status : "0";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {       //绑定状态 1-已绑定 2-已解绑',
        return !TextUtils.isEmpty(type) ? type : "2";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return !TextUtils.isEmpty(user_id) ? user_id : "";
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getOccupation_name() {
        return !TextUtils.isEmpty(occupation_name) ? occupation_name : "";
    }

    public void setOccupation_name(String occupation_name) {
        this.occupation_name = occupation_name;
    }

    public String getConsultant_name() {
        return !TextUtils.isEmpty(consultant_name) ? consultant_name : "";
    }

    public void setConsultant_name(String consultant_name) {
        this.consultant_name = consultant_name;
    }

    public String getYunxin_accid() {
        return yunxin_accid;
    }

    public void setYunxin_accid(String yunxin_accid) {
        this.yunxin_accid = yunxin_accid;
    }

    public String getYunxin_token() {
        return yunxin_token;
    }

    public void setYunxin_token(String yunxin_token) {
        this.yunxin_token = yunxin_token;
    }
}
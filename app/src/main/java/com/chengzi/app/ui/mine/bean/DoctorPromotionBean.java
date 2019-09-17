package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.handongkeji.utils.FormatUtil;

public class DoctorPromotionBean {

    /**
     * id : 17
     * name : 咨询师二号
     * img : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * grade : 3.0
     * skill_grade : 2.0
     * Consultant : 普通咨询师
     * userOrderCount : 9
     * is_extension : false
     * preset_amount : 0
     */

    private String id;          // 用户id
    private String name;        // 医生/咨询师名称
    private String img;         // 头像
    private String grade;       // 专业分
    private String skill_grade; // 技术（服务）分
    @SerializedName(value = "operation_name")
    private String Consultant;  // 职称
    private String userOrderCount;
    private boolean is_extension; // 当前医生是否加入搜索推广
    private String preset_amount; // 消费限额

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return !TextUtils.isEmpty(grade) ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getConsultant() {
        return Consultant;
    }

    public void setConsultant(String Consultant) {
        this.Consultant = Consultant;
    }

    public String getUserOrderCount() {
        return userOrderCount;
    }

    public void setUserOrderCount(String userOrderCount) {
        this.userOrderCount = userOrderCount;
    }

    public boolean isIs_extension() {
        return is_extension;
    }

    public void setIs_extension(boolean is_extension) {
        this.is_extension = is_extension;
    }

    public String getPreset_amount() {
//        return !TextUtils.isEmpty(preset_amount) ? preset_amount : "0";
        return !TextUtils.isEmpty(preset_amount) ? FormatUtil.format2Decimal(Double.parseDouble(preset_amount)) : "0.00";
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }
}

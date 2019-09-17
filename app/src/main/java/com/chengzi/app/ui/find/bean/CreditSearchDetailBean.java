package com.chengzi.app.ui.find.bean;

import android.text.TextUtils;

public class CreditSearchDetailBean {

    /**
     * id : 47
     * name : 彭彭咨询师
     * image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/155857911259047
     * grade : 8.1
     * orderNum : 0
     * complaintRate : 0
     * refundRate : 0
     */

    private String id;            // 用户id
    private String name;          // 用户名称
    private String image;         // 用户头像
    private String grade;         // 用户评分
    private String orderNum;      // 下单量
    private String complaintRate; // 投诉率
    private String refundRate;    // 退款率

    private String age;     // 年龄
    private String gender;  // 性别
    private String type;  // 1-普通用户 2-医生 3-咨询师 4-机构

    public String getNickName() {
        return getName() + "(ID:" + getId() + ")";
    }

    public String getId() {
        return !TextUtils.isEmpty(id) ? id : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return !TextUtils.isEmpty(name) ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getOrderNum() {
        return !TextUtils.isEmpty(orderNum) ? orderNum : "0";
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getComplaintRate() {
        return !TextUtils.isEmpty(complaintRate) ? complaintRate + "%" : "0%";
    }

    public void setComplaintRate(String complaintRate) {
        this.complaintRate = complaintRate;
    }

    public String getRefundRate() {
        return !TextUtils.isEmpty(refundRate) ? refundRate + "%" : "0%";
    }

    public void setRefundRate(String refundRate) {
        this.refundRate = refundRate;
    }

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

    public String getType() {
        return type;
    }

    public int getTypes() {
        return !TextUtils.isEmpty(type) ? Integer.parseInt(type) : 0;
    }

    public void setType(String type) {
        this.type = type;
    }
}
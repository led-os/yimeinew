package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.List;

public class ComplaintListBean {

    /**
     * id : 24
     * user_id : 6
     * order_id : 6
     * complaint_id : 26
     * content : 投诉测试
     * complanint_img : null
     * is_count : 1
     * create_time : 1555985919
     * update_time : 1555985920
     * gender : null
     * name : 北京美容解放医院
     * image : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * grade : 8.4
     * orange_create : 800
     */

    private String id;          // 主键id
    private String user_id;     // 用户id
    private String order_id;    // 订单id
    private String complaint_id;// 被投诉人id
    private String content;     // 投诉地址
    //    private List<String> complanint_img;
    private String is_count;
    private String create_time; // 添加时间
    private String update_time;
    private String gender;      // 性别
    private String name;        // 姓名
    private String image;       // 头像
    private String grade;       // 评分
    private String age;       // 年龄
    private String orange_create;
    private List<String> complanint_img;    //投诉图

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getComplanint_img() {
        return complanint_img;
    }

    public void setComplanint_img(List<String> complanint_img) {
        this.complanint_img = complanint_img;
    }

    public String getIs_count() {
        return is_count;
    }

    public void setIs_count(String is_count) {
        this.is_count = is_count;
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

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "0";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
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
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getOrange_create() {
        return orange_create;
    }

    public void setOrange_create(String orange_create) {
        this.orange_create = orange_create;
    }
}

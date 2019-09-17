package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class UserMineInfoBean {

    /**
     * name : 呆萌和画师、
     * image : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * gender : 0
     * autograph : null
     * grade : 0.0
     * orange_create : null
     * likes_num : 0
     * collection_num : 0
     * fans_num : 0
     * is_collection : 0
     * is_sign : 0
     * orang_info :
     */

    private String name;            // 姓名
    private String image;           // 头像
    private String cover;           // 封面
    private String gender;          // 性别 0女 1男
    private String autograph;       // 签名
    private String grade;           // 评分
    private String orange_create;   // 橙子信用分
    private String likes_num;       // 点赞数量
    private String collection_num;  // 关注数量
    private String fans_num;        // 粉丝数量
    private String is_collection;   // 1 已关注 0未关注
    private String is_sign;         // 今天是否已签到，false未签到，true已签到
    private String orang_info;      // 橙子信用描述 一般 普通 中等 优秀 极好 最佳
    private String age;             // 年龄

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAutograph() {
        return !TextUtils.isEmpty(autograph) ? autograph : "";
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? "评分 " + grade + "分" : "评分 0.0分";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getOrange_create() {
        return !TextUtils.isEmpty(orange_create) ? orange_create : "0";
    }

    public void setOrange_create(String orange_create) {
        this.orange_create = orange_create;
    }

    public String getLikes_num() {
        return !TextUtils.isEmpty(likes_num) ? likes_num : "0";
    }

    public void setLikes_num(String likes_num) {
        this.likes_num = likes_num;
    }

    public String getCollection_num() {
        return !TextUtils.isEmpty(collection_num) ? collection_num : "0";
    }

    public void setCollection_num(String collection_num) {
        this.collection_num = collection_num;
    }

    public String getFans_num() {
        return !TextUtils.isEmpty(fans_num) ? fans_num : "0";
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getIs_collection() {
        return !TextUtils.isEmpty(is_collection) ? is_collection : "0";
    }

    public void setIs_collection(String is_collection) {
        this.is_collection = is_collection;
    }

    public String getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(String is_sign) {
        this.is_sign = is_sign;
    }

    public String getOrang_info() {
        return orang_info;
    }

    public void setOrang_info(String orang_info) {
        this.orang_info = orang_info;
    }

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

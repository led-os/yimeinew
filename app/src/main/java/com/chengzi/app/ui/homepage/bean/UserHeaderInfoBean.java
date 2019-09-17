package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

public class UserHeaderInfoBean {


    private String id;
    private String name;    //  名字
    private String image;   //  头像
    private String gender;  //  性别
    private String autograph;   //  签名
    private String grade;       //  评分
    private String orange_create;  //  橙子信用分
    private String likes_num;      //  点赞数量
    private String collection_num;     //  关注数量
    private String publish_count;      // 发表数
    private String collect_count;      // 收藏数
    private String ask_count;          // 提问数
    private String response_count;     // 回答数
    private String fans_num;       //  粉丝数量
    private int is_collection;  //   1 已关注 0未关注
    private String is_sign;     //  ，false未签到，true已签到
    private String orang_info;  //  响应信息, 橙子信用描述 一般 普通 中等 优秀 极好 最佳

    private String age;         // 年龄
    private String cover;       // 封面图
    private String yunxin_accid;    //  云信id

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

    public String getName() {
        return name;
    }

    public String getNameAndId() {
        return name + "(ID:" + id + ")";
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

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "0";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getGrade() {
        return grade == null ? (grade = "") : grade;
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

    public String getLikes_num() {
        return likes_num;
    }

    public void setLikes_num(String likes_num) {
        this.likes_num = likes_num;
    }

    public String getCollection_num() {
        return collection_num;
    }

    public void setCollection_num(String collection_num) {
        this.collection_num = collection_num;
    }

    public String getPublish_count() {
        return !TextUtils.isEmpty(publish_count) ? publish_count : "0";
    }

    public void setPublish_count(String publish_count) {
        this.publish_count = publish_count;
    }

    public String getCollect_count() {
        return !TextUtils.isEmpty(collect_count) ? collect_count : "0";
    }

    public void setCollect_count(String collect_count) {
        this.collect_count = collect_count;
    }

    public String getAsk_count() {
        return !TextUtils.isEmpty(ask_count) ? ask_count : "0";
    }

    public void setAsk_count(String ask_count) {
        this.ask_count = ask_count;
    }

    public String getResponse_count() {
        return !TextUtils.isEmpty(response_count) ? response_count : "0";
    }

    public void setResponse_count(String response_count) {
        this.response_count = response_count;
    }

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public int getIs_collection() {  //1已关注 0未关注
        return is_collection;
    }

    public void setIs_collection(int is_collection) {
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

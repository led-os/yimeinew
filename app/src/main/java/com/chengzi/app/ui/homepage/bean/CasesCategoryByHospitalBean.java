package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

public class CasesCategoryByHospitalBean {

    private String id;
    private String name;        // 一级分类名称
    private String is_operation;// 是否为手术分类 1-是 0-否
    private String user_id;     // 查看的医院id
    private String cate_id1;    // 一级分类id
    private String cate_id2;    // 二级分类id
    private String cate_id3;    // 三级分类id

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

    public String getIs_operation() {
        return is_operation;
    }

    public void setIs_operation(String is_operation) {
        this.is_operation = is_operation;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCate_id1() {
        return cate_id1;
    }

    public int getCate_id() {
        return !TextUtils.isEmpty(getCate_id1()) ? Integer.parseInt(getCate_id1()) : 0;
    }

    public void setCate_id1(String cate_id1) {
        this.cate_id1 = cate_id1;
    }

    public String getCate_id2() {
        return cate_id2;
    }

    public void setCate_id2(String cate_id2) {
        this.cate_id2 = cate_id2;
    }

    public String getCate_id3() {
        return cate_id3;
    }

    public void setCate_id3(String cate_id3) {
        this.cate_id3 = cate_id3;
    }
}

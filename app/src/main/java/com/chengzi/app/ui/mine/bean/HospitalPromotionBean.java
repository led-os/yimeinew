package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class HospitalPromotionBean {

    /**
     * id : 8
     * name : 北京三甲医院
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * grade : 9.0
     * is_extension : true
     * preset_amount : 100.00
     */

    private String id;              // 医院id
    private String name;            // 医院名称
    private String image;           // 医院logo
    private String grade;           // 评分
    private boolean is_extension;   // 是否开启搜索推广
    private String preset_amount;   // 搜索推广消费限额

    public String getId() {
        return id;
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

    public boolean isIs_extension() {
        return is_extension;
    }

    public void setIs_extension(boolean is_extension) {
        this.is_extension = is_extension;
    }

    public String getPreset_amount() {
        return !TextUtils.isEmpty(preset_amount) ? preset_amount : "0";
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }
}

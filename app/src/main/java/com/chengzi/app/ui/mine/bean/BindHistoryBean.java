package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.DateUtils;

public class BindHistoryBean {

    /**
     * id : 8
     * name : 北京三甲医院
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * skilled : null
     * begoodat : 1,2,33
     * grade : 6.4
     * hospital_address : 北京市雨花区中华大街3
     * update_time : null
     * type : 1
     * count : 89
     * category : 皮肤美容2361,玻尿酸,那你看专区
     */

    private String id;          // 机构id
    private String binding_id;  // 绑定机构id
    private String name;        // 机构名称
    private String image;       // 机构头像
    private String skilled;
    private String begoodat;    // 擅长id
    private String grade;       // 机构评分
    private String hospital_address; // 机构地址
    private String update_time; // 绑定/解绑时间
    private String type;        // 绑定类型( 1-已绑定 2-已解绑 )
    private String count;       // 下单量
    private String category;    // 擅长

    public String getTypeName() {
        return !TextUtils.isEmpty(type) && type.equals("1") ? " 绑定" : " 解绑";
    }

    public String getBindingTime() {
        return !TextUtils.isEmpty(update_time) ? DateUtils.dataTime(update_time, "yyyy-MM-dd") + getTypeName() : "" + getTypeName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBinding_id() {
        return !TextUtils.isEmpty(binding_id) ? binding_id : "";
    }

    public void setBinding_id(String binding_id) {
        this.binding_id = binding_id;
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

    public String getSkilled() {
        return skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public String getBegoodat() {
        return begoodat;
    }

    public void setBegoodat(String begoodat) {
        this.begoodat = begoodat;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public String getUpdate_time() {
        return !TextUtils.isEmpty(update_time) ? update_time : "0";
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return !TextUtils.isEmpty(count) ? count : "0";
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCategory() {
        return !TextUtils.isEmpty(category) ? category : "";
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

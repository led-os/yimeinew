package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class AuthenticationBean implements Serializable {

    /**
     * id : 42
     * true_name : 彭彭
     * qualnumber : 123456790
     * doccertificate_image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/1557384243286
     * aptitude_image : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/1557384277601
     * card_front : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/1557384265888
     * card_reverse : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/1557384269310
     * card : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/1557384293332
     * bussiness_aqtitude_img : null
     * guangshen_aqtitude_img : null
     * occupation_class : null
     * city_id : 110000
     * hosipital_type : null
     * occupation_class_name :
     * hospital_city : 北京市
     * hospital_type : 未设置
     */

    private String info_id;                      //医生/咨询师/医院id
    private String id;                      //医生/咨询师/医院id
    private String name;     //真实姓名
    private String true_name;     //真实姓名
    private String qualnumber;    //资质编码
    private String doccertificate_image;    //职业证书
    private String doccertificate_imagea;    //职业证书
    private String aptitude_image;          //资质证书
    private String aptitude_orther;          //资质证书
    private String card;          //医生/咨询师手持身份证
    private String card_front;    //医生/咨询师手持身份证/正面
    private String card_reverse;  //医生/咨询师手持身份证/反面
    private String bussiness_aqtitude_img;  //医院营业执照
    private String permission;  //医院医疗结构许可证
    private String guangshen_aqtitude_img;  //医院广申资质证书
    private String occupation_class;        //职位级别 1主任医师 2副主任医师 3医师 4主治医师 5国外医师
    private String city_id;
    private String hosipital_type;          //6-民营机构 7-公司机构 8-品牌连锁 9-生活美容机构
    private String occupation_class_name;   //#医生职称
    private String hospital_city;           //医院所属城市
    private String hospital_type;           //医院的类型
    private String hospital_opreation_name;           //医院运营人姓名

    public String getHospital_opreation_name() {
        return !TextUtils.isEmpty(hospital_opreation_name) ? hospital_opreation_name : "";
    }

    public void setHospital_opreation_name(String hospital_opreation_name) {
        this.hospital_opreation_name = hospital_opreation_name;
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }

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

    public String getTrue_name() {
        return !TextUtils.isEmpty(true_name) ? true_name : "";
    }

    public String getTrue_names() {
        return !TextUtils.isEmpty(true_name) ? "真实姓名：" + true_name : "真实姓名：";
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getQualnumber() {
        return !TextUtils.isEmpty(qualnumber) ? qualnumber : "";
    }

    public String getQualnumbers() {
        return !TextUtils.isEmpty(qualnumber) ? "资质编号：" + qualnumber : "资质编号：";
    }

    public void setQualnumber(String qualnumber) {
        this.qualnumber = qualnumber;
    }

    public String getDoccertificate_image() {
        return doccertificate_image;
    }

    public void setDoccertificate_image(String doccertificate_image) {
        this.doccertificate_image = doccertificate_image;
    }

    public String getAptitude_image() {
        return aptitude_image;
    }

    public void setAptitude_image(String aptitude_image) {
        this.aptitude_image = aptitude_image;
    }

    public String getCard_front() {
        return card_front;
    }

    public void setCard_front(String card_front) {
        this.card_front = card_front;
    }

    public String getCard_reverse() {
        return card_reverse;
    }

    public void setCard_reverse(String card_reverse) {
        this.card_reverse = card_reverse;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBussiness_aqtitude_img() {
        return bussiness_aqtitude_img;
    }

    public void setBussiness_aqtitude_img(String bussiness_aqtitude_img) {
        this.bussiness_aqtitude_img = bussiness_aqtitude_img;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getGuangshen_aqtitude_img() {
        return guangshen_aqtitude_img;
    }

    public void setGuangshen_aqtitude_img(String guangshen_aqtitude_img) {
        this.guangshen_aqtitude_img = guangshen_aqtitude_img;
    }

    public String getOccupation_class() {
        return !TextUtils.isEmpty(occupation_class) ? occupation_class : "";
    }

    public String getOccupation_classs() {
        if (!TextUtils.isEmpty(occupation_class)) {
            //职位级别 1-主任医师 2-副主任医师 3-医师 4-主治医师 5-国外医师
            if (occupation_class.equals("1")) {
                return "职级：主任医师";
            } else if (occupation_class.equals("2")) {
                return "职级：副主任医师";
            } else if (occupation_class.equals("3")) {
                return "职级：医师";
            } else if (occupation_class.equals("4")) {
                return "职级：主治医师";
            } else if (occupation_class.equals("5")) {
                return "职级：国外医师";
            }
        }
        return "职级：";
    }

    public String getOccupation_class_edit() {
        if (!TextUtils.isEmpty(occupation_class)) {
            //职位级别 1-主任医师 2-副主任医师 3-医师 4-主治医师 5-国外医师
            if (occupation_class.equals("1")) {
                return "主任医师";
            } else if (occupation_class.equals("2")) {
                return "副主任医师";
            } else if (occupation_class.equals("3")) {
                return "医师";
            } else if (occupation_class.equals("4")) {
                return "主治医师";
            } else if (occupation_class.equals("5")) {
                return "国外医师";
            }
        }
        return "";
    }

    public void setOccupation_class(String occupation_class) {
        this.occupation_class = occupation_class;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getHosipital_type() {
        return hosipital_type;
    }

    //6-民营机构 7-公司机构 8-品牌连锁 9-生活美容机构
    public String getHosipital_type_name() {
        if (TextUtils.isEmpty(hosipital_type)) {
            return "";
        } else if (hosipital_type.equals("1")) {
            return "民营机构";
        } else if (hosipital_type.equals("2")) {
            return "公司机构";
        } else if (hosipital_type.equals("3")) {
            return "品牌连锁";
        } else if (hosipital_type.equals("4")) {
            return "生活美容机构";
        } else {
            return "";
        }
//        return !TextUtils.isEmpty(hosipital_type) ? hospital_type : "";
    }


    public void setHosipital_type(String hosipital_type) {
        this.hosipital_type = hosipital_type;
    }

    public String getOccupation_class_name() {
        return occupation_class_name;
    }

    public void setOccupation_class_name(String occupation_class_name) {
        this.occupation_class_name = occupation_class_name;
    }

    public String getHospital_city() {
        return hospital_city;
    }

    public void setHospital_city(String hospital_city) {
        this.hospital_city = hospital_city;
    }

    public String getHospital_type() {
        return hospital_type;
    }

    public void setHospital_type(String hospital_type) {
        this.hospital_type = hospital_type;
    }

    public String getDoccertificate_imagea() {
        return doccertificate_imagea;
    }

    public void setDoccertificate_imagea(String doccertificate_imagea) {
        this.doccertificate_imagea = doccertificate_imagea;
    }

    public String getAptitude_orther() {
        return aptitude_orther;
    }

    public void setAptitude_orther(String aptitude_orther) {
        this.aptitude_orther = aptitude_orther;
    }
}

package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.utils.ScoreUtils;

/**
 * 医生/咨询师 实体类
 *
 * @ClassName:DoctorBean
 * @PackageName:com.yimei.app.ui.home.bean
 * @Create On 2019/5/8 0008   17:34
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/8 0008 handongkeji All rights reserved.
 */
public class DoctorBean {


    @SerializedName(value = "id", alternate = {"user_id"})
    private String id;         // 医生id

    @SerializedName(value = "name", alternate = {"username", "true_name"})
    private String name;       // 医生名称

    @SerializedName(value = "image", alternate = {"usericon", "user_img"})
    private String image;      // 医生头像

    @SerializedName(value = "grade", alternate = "avg_grade")
    private String grade;         // 评分/医生咨询师专业分

    private String skill_grade;   //  医生技术分/咨询师服务分

    @SerializedName(value = "bind_name", alternate = {"hospital_name", "binding_name", "institution", "institution_name"})
    private String bind_name;     // 绑定机构名称

    private String hospital_type_name;    // 机构类型名称

    @SerializedName(value = "rank", alternate = {"level", "consultant_name", "occupation_name", "title",
            "occupation_class_text", "level_name", "occupation_class"})
    private String rank;          //  医生/咨询师  职称

    @SerializedName(value = "is_Vip", alternate = {"is_VIP"})
    private int is_Vip;           //  是否vip  0 否  1 是

//    private int is_extension;     // 是否推广  1.推广 2.否

    private String begoodat;      //  擅长分类id

    @SerializedName(value = "begoodatString", alternate = {"major_text"})
    private String begoodatString;   // 擅长分类名称

    private String work_year;    //  从业年限

    @SerializedName(value = "orderNum", alternate = {"order_quantity"})
    private String orderNum;    //  下单数量
    @SerializedName(value = "sheetNum", alternate = {"sheet_num"})
    private String sheetNum;   //  咨询师 的  咨询量

    private String promotion_id;    //   推广id
    private String yunxin_accid;    //  云信id
    private int type;               //  用户id

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
        return ScoreUtils.processScores(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBind_name() {
        return bind_name;
    }

    public void setBind_name(String bind_name) {
        this.bind_name = bind_name;
    }

    public int getIs_Vip() {
        return is_Vip;
    }

    public void setIs_Vip(int is_Vip) {
        this.is_Vip = is_Vip;
    }

    public String getRank() {
        return TextUtils.isEmpty(rank) ? "" : rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSkill_grade() {
        return ScoreUtils.processScore(skill_grade);
    }

    public void setSkill_grade(String skill_grade) {
        this.skill_grade = skill_grade;
    }

    public String getHospital_type_name() {
        return hospital_type_name;
    }

    public void setHospital_type_name(String hospital_type_name) {
        this.hospital_type_name = hospital_type_name;
    }

    public int getIs_extension() {
        if (TextUtils.isEmpty(promotion_id)) {
            return 0;
        }
        long id = Long.valueOf(promotion_id);
        return id > 0 ? 1 : 0;
    }

    public String getBegoodat() {
        return !TextUtils.isEmpty(begoodat) ? begoodat : "";
    }

    public void setBegoodat(String begoodat) {
        this.begoodat = begoodat;
    }

    public String getBegoodatString() {
        return begoodatString == null ? "" : begoodatString;
    }

    public void setBegoodatString(String begoodatString) {
        this.begoodatString = begoodatString;
    }

    public String getWork_year() {
        return work_year == null ? "0" : work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getOrderNum() {
        return orderNum == null ? "0" : orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getSheetNum() {
        return !TextUtils.isEmpty(sheetNum) ? sheetNum : "0";
    }

    public void setSheetNum(String sheetNum) {
        this.sheetNum = sheetNum;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getAverageGrade() {

        double g = Double.valueOf(TextUtils.isEmpty(grade) ? "0" : grade);
        double s = Double.valueOf(TextUtils.isEmpty(skill_grade) ? "0" : skill_grade);
        return String.format("%1$1.1f", (g + s) / 2);

    }
}

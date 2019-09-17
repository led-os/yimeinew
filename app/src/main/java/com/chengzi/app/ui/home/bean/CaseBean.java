package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.chengzi.app.utils.DateTimeStampTypeAdapter;
import com.chengzi.app.utils.ScoreUtils;

public class CaseBean {

    @SerializedName(value = "case_id", alternate = {"id", "cases_id"})
    private String id;              //  案例id
    private String user_id;         //  用户id
    @SerializedName(value = "name", alternate = {"user_name", "username"})
    private String name;            //  机构名称
    private String grade;            //   机构评分
    private int is_VIP;              //  机构 是否vip  0 否  1 是
//    private int is_extension;        // 机构 是否推广  1.推广 2.否

    @SerializedName(value = "image", alternate = {"hospotail_image", "usericon"})
    private String image;  //   机构头像
    private String content;          // 案例内容
    private int like_number;         // 点赞数
    private String comment_number;      // 评论数
    private String forward_number;      // 转发数
    private String view_number;         // 查看数
    @JsonAdapter(value = DateTimeStampTypeAdapter.class)
    private long create_time;        // 注册时间

    @SerializedName(value = "fristImg", alternate = {"pre_img"})
    private String fristImg;         //  术前图

    @SerializedName(value = "lastImg", alternate = {"after_img"})
    private String lastImg;          //   术后图
    private String lastComment;        //   最新评论时间
    private int hosipital_type;      //   机构类型
    private int is_relation;        //   1 点赞  2 否

    private boolean isLike;         //  是否点赞 与is_relation 字段兼容
    private HospitalBean hospital_info;

    private String promotion_id;    //  推广
    private String categoryName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return hospital_info != null ? hospital_info.getName() : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return ScoreUtils.processScore(hospital_info != null ? hospital_info.getGrade() : grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getIs_VIP() {
        return hospital_info != null ? hospital_info.getIs_VIP() : is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getImage() {
        return hospital_info != null ? hospital_info.image : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIs_extension() {
        if (hospital_info != null) {
            return hospital_info.isIs_Extension() ? 1 : 0;
        }
        if (TextUtils.isEmpty(promotion_id)) {
            return 0;
        }
        long id = Long.valueOf(promotion_id);
        return id > 0 ? 1 : 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike_number() {
        return like_number;
    }

    public void setLike_number(int like_number) {
        this.like_number = like_number;
    }

    public String getComment_number() {
        return comment_number == null ? "0" : comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getForward_number() {
        return forward_number == null ? "0" : forward_number;
    }

    public void setForward_number(String forward_number) {
        this.forward_number = forward_number;
    }

    public String getView_number() {
        return view_number == null ? "0" : view_number;
    }

    public void setView_number(String view_number) {
        this.view_number = view_number;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFristImg() {
        return fristImg;
    }

    public void setFristImg(String fristImg) {
        this.fristImg = fristImg;
    }

    public String getLastImg() {
        return lastImg;
    }

    public void setLastImg(String lastImg) {
        this.lastImg = lastImg;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public int getHosipital_type() {
        return hosipital_type;
    }

    public void setHosipital_type(int hosipital_type) {
        this.hosipital_type = hosipital_type;
    }

    public int getIs_relation() {
        if (!TextUtils.isEmpty(String.valueOf(is_relation)) && is_relation != 0) {
            return isLike ? 1 : is_relation;
        } else {
            return isLike ? 1 : 0;
        }
    }

    public void setIs_relation(int is_relation) {
        this.is_relation = is_relation;
    }

    public boolean isLike() {
        return getIs_relation() == 1;
    }

    public void setLike(boolean like) {
        this.is_relation = like ? 1 : 0;
        this.isLike = like;
    }

    public HospitalBean getHospital_info() {
        return hospital_info;
    }

    public void setHospital_info(HospitalBean hospital_info) {
        this.hospital_info = hospital_info;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static class HospitalBean {

        private String id;          // 医院id
        private String name;        // 医院名称
        private String image;       // 医院头像
        private int is_VIP;         // 是否是vip 0-否 1-是
        private String grade;       // 医院评分
        private boolean is_Extension;       // true-推广中  false-非推广

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

        public int getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(int is_VIP) {
            this.is_VIP = is_VIP;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public boolean isIs_Extension() {
            return is_Extension;
        }

        public void setIs_Extension(boolean is_Extension) {
            this.is_Extension = is_Extension;
        }


    }
}

package com.chengzi.app.ui.homepage.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.List;

public class HospitalHomeInfoBean {

    private int is_follow;      //  是否关注  0 否  1 是
    private HospitalInfoBean info;
    private String totalOrderCount;        // 总下单
    private String totalOrderPrice;        // 总成交
    private String totalPublishCount;      // 发表数
    private String fansCount;              // 粉丝数
    private String followCount;            // 关注数
    private String replyCount;             // 回答数
    @SerializedName(value = "sheet_num", alternate = {"sheet_count"})
    private String sheet_num;              // 咨询量 sheet_count
    private String todayOrderCount;        // 今日下单数
    private List<BegoodatBean> begoodat;        // 擅长
    private List<GoodBean> featuredProduct;     // 特色商品列表
    private List<MoreHospitalBean> more_hospital;       // 相关机构展示
    private List<RelationCategoryBean> product_category;    // 商品的分类列表(默认显示所有一级分类，包括手术分类和非手术分类)

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public HospitalInfoBean getInfo() {
        return info;
    }

    public void setInfo(HospitalInfoBean info) {
        this.info = info;
    }

    public List<BegoodatBean> getBegoodat() {
        return begoodat;
    }

    public void setBegoodat(List<BegoodatBean> begoodat) {
        this.begoodat = begoodat;
    }

    public List<GoodBean> getFeaturedProduct() {
        return featuredProduct;
    }

    public void setFeaturedProduct(List<GoodBean> featuredProduct) {
        this.featuredProduct = featuredProduct;
    }

    public List<MoreHospitalBean> getMore_hospital() {
        return more_hospital;
    }

    public void setMore_hospital(List<MoreHospitalBean> more_hospital) {
        this.more_hospital = more_hospital;
    }

    public List<RelationCategoryBean> getProduct_category() {
        return product_category;
    }

    public void setProduct_category(List<RelationCategoryBean> product_category) {
        this.product_category = product_category;
    }

    public void setTotalOrderCount(String totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public void setTotalPublishCount(String totalPublishCount) {
        this.totalPublishCount = totalPublishCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getSheet_num() {
        return !TextUtils.isEmpty(sheet_num) ? sheet_num : "0";
    }

    public void setSheet_num(String sheet_num) {
        this.sheet_num = sheet_num;
    }

    public void setTodayOrderCount(String todayOrderCount) {
        this.todayOrderCount = todayOrderCount;
    }

    public String getTotalOrderCount() {
        return totalOrderCount;
    }

    public String getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public String getTotalPublishCount() {
        return totalPublishCount;
    }

    public String getFansCount() {
        return fansCount;
    }

    public String getFollowCount() {
        return followCount;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public String getTodayOrderCount() {
        return todayOrderCount;
    }

    public static class BegoodatBean {

        /**
         * id : 8
         * name : 脂肪整形
         */

        private String id;
        private String name;

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
    }

    public static class MoreHospitalBean {

        private String id;
        private String name;
        private String image;
        private boolean isVip;
        private boolean is_advert;

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

        public boolean isIsVip() {
            return isVip;
        }

        public void setIsVip(boolean isVip) {
            this.isVip = isVip;
        }

        public boolean isIs_advert() {
            return is_advert;
        }

        public void setIs_advert(boolean is_advert) {
            this.is_advert = is_advert;
        }
    }

}

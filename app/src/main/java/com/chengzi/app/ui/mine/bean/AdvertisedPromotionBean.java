package com.chengzi.app.ui.mine.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertisedPromotionBean {

    /**
     * userInfo : {"city_id":140100,"city_name":"太原市"}
     * zone_type_info : {"zone_type":1,"zone_name":"普通"}
     * categoryInfo : [{"category_id":1,"category_name":"皮肤美容2361"},{"category_id":2,"category_name":"玻尿酸"},{"category_id":3,"category_name":"肉毒素"},{"category_id":4,"category_name":"眼部整形"},{"category_id":5,"category_name":"鼻部整形"},{"category_id":6,"category_name":"隆眼整形"},{"category_id":7,"category_name":"开眼角"},{"category_id":8,"category_name":"修眉"},{"category_id":33,"category_name":"那你看专区"},{"category_id":34,"category_name":"眼部整形"},{"category_id":35,"category_name":"胸部整形"},{"category_id":38,"category_name":"眉部整形"},{"category_id":39,"category_name":"毛发美容"},{"category_id":40,"category_name":"口唇整形"}]
     * zone_type_VIP_info : {"zone_type":2,"zone_name":"VIP"}
     * categoryVIPInfo : [{"category_id":33,"category_name":"那你看专区"},{"category_id":34,"category_name":"眼部整形"},{"category_id":35,"category_name":"胸部整形"},{"category_id":38,"category_name":"眉部整形"},{"category_id":39,"category_name":"毛发美容"},{"category_id":40,"category_name":"口唇整形"}]
     */

    private UserInfoEntity userInfo;

    @SerializedName(value = "zone_type_VIP_info")
    private ZoneTypeInfoEntity zone_type_info;

    private List<PromotionSelectCategoryBean> categoryInfo;
    private List<PromotionSelectCategoryBean> categoryVIPInfo;

    public UserInfoEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoEntity userInfo) {
        this.userInfo = userInfo;
    }


    public List<PromotionSelectCategoryBean> getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(List<PromotionSelectCategoryBean> categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public List<PromotionSelectCategoryBean> getCategoryVIPInfo() {
        return categoryVIPInfo;
    }

    public void setCategoryVIPInfo(List<PromotionSelectCategoryBean> categoryVIPInfo) {
        this.categoryVIPInfo = categoryVIPInfo;
    }

    public static class UserInfoEntity {
        /**
         * city_id : 140100
         * city_name : 太原市
         */

        private int city_id;
        private String city_name;

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }

    public static class ZoneTypeInfoEntity {
        /**
         * zone_type :1
         * zone_name :普通
         */

        private int zone_type;
        private String zone_name;

        public int getZone_type() {
            return zone_type;
        }

        public void setZone_type(int zone_type) {
            this.zone_type = zone_type;
        }

        public String getZone_name() {
            return zone_name;
        }

        public void setZone_name(String zone_name) {
            this.zone_name = zone_name;
        }
    }
}

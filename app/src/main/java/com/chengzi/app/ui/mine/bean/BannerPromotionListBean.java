package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.List;

public class BannerPromotionListBean {

    /**
     * banner_list : [{"extension_id":27,"img":"http://123.jpg,223.jpg","hospital_id":9}]
     * canUse : false
     */

    private boolean canUse;        // 当前用户是否能抢这个Banner推广
    /*
    status: 表示当前用户抢广告位的状态，取值如下
        canUse = 可以抢
        gradeLess = 因为评分过低，不能抢
        isNotVip = 因为不是VIP，不能抢
        isFull = 因为五个数值已经满了，不能抢
        isHas = 因为已经抢了一个了，不能抢
    * */
    private String status;         //
    private String bannerImg;      // 表示显示在界面上的广告位图片地址
    private List<BannerListEntity> banner_list;  // banner图列表

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public String getStatus() {
//        return status;
        if (!TextUtils.isEmpty(status)) {
            if (status.equals("gradeLess")) {
                return "评分过低";
            } else if (status.equals("isNotVip")) {
                return "VIP可抢";
            } else if (status.equals("isFull")) {
                return "抢完了";
            } else if (status.equals("isHas")) {
                return "已抢";
            }
        }
        return "不可抢";
    }

    public String getStatuss() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public List<BannerListEntity> getBanner_list() {
        return banner_list;
    }

    public void setBanner_list(List<BannerListEntity> banner_list) {
        this.banner_list = banner_list;
    }

    public static class BannerListEntity {
        /**
         * extension_id : 27
         * img : http://123.jpg,223.jpg
         * hospital_id : 9
         */

        private String extension_id;     // 推广ID（删除推广时时用这个）
        private String img;              // Banner图片地址
        private String hospital_id;      // 机构ID（医院ID）

        public String getExtension_id() {
            return extension_id;
        }

        public void setExtension_id(String extension_id) {
            this.extension_id = extension_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getHospital_id() {
            return !TextUtils.isEmpty(hospital_id) ? hospital_id : "0";
        }

        public void setHospital_id(String hospital_id) {
            this.hospital_id = hospital_id;
        }
    }
}
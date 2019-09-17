package com.chengzi.app.ui.seckill.bean;

import android.text.TextUtils;

import java.util.List;

public class KillGoodsListBean {
    /**
     * data : [{"goods_id":7,"goods_skill_price":"2000.00","session":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","goods_image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","goods_price":"0.01"},{"goods_id":8,"goods_skill_price":"226.00","session":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","goods_image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","goods_price":"0.01"}]
     * end_time : 4717
     * times : 14
     */

    private String end_time;    //倒计时
    private String times;       //?点场
    private List<DataEntity> data;

    public String getEnd_time() {
        return !TextUtils.isEmpty(end_time) ? end_time : "0";
        //       return end_times;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTimes() {
        return !TextUtils.isEmpty(times) ? times : "0";
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * goods_id : 7
         * goods_skill_price : 2000.00
         * session : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * goods_image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * goods_price : 0.01
         */

        private String goods_id;      // 商品id
        private String goods_skill_price;      // 商品秒杀价格
        private String session;
        private String goods_image; // 商品封面
        private String goods_price;     // 商品价格

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_skill_price() {
            return !TextUtils.isEmpty(goods_skill_price) ? goods_skill_price : "0";
        }

        public void setGoods_skill_price(String goods_skill_price) {
            this.goods_skill_price = goods_skill_price;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public String getGoods_image() {
            return goods_image;
        }

        public void setGoods_image(String goods_image) {
            this.goods_image = goods_image;
        }

        public String getGoods_price() {
            return !TextUtils.isEmpty(goods_price) ? goods_price : "0";
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.List;

public class ZonePromotionBean {

    /**
     * userInfo : {"city_id":110100,"city_name":"北京市"}
     * category_list : [{"categoryName":"口唇整形","categoryId":40,"canCatch":true,"good_list":[]},{"categoryName":"毛发美容","categoryId":39,"canCatch":true,"good_list":[]},{"categoryName":"眉部整形","categoryId":38,"canCatch":true,"good_list":[]},{"categoryName":"胸部整形","categoryId":35,"canCatch":true,"good_list":[]},{"categoryName":"眼部整形","categoryId":34,"canCatch":true,"good_list":[]},{"categoryName":"那你看专区","categoryId":33,"canCatch":true,"good_list":[{"id":1,"extension_id":23,"name":"222开内眼角 可升级贝塞尔技术 眼综合双眼皮修改","buy_price":"45452.00","spell_price":"11255.00","image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","grade":"6.9","sales_volume":0,"preset_amount":"0.00","can_cancel":false}]},{"categoryName":"修眉","categoryId":8,"canCatch":true,"good_list":[]},{"categoryName":"开眼角","categoryId":7,"canCatch":true,"good_list":[]},{"categoryName":"隆眼整形","categoryId":6,"canCatch":true,"good_list":[]},{"categoryName":"鼻部整形","categoryId":5,"canCatch":true,"good_list":[]},{"categoryName":"眼部整形","categoryId":4,"canCatch":true,"good_list":[]},{"categoryName":"肉毒素","categoryId":3,"canCatch":true,"good_list":[]},{"categoryName":"玻尿酸","categoryId":2,"canCatch":true,"good_list":[]},{"categoryName":"皮肤美容2361","categoryId":1,"canCatch":true,"good_list":[]}]
     */

    private UserInfoEntity userInfo;         // 用户当前位置
    private List<CategoryListEntity> category_list; // 专区列表

    public UserInfoEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoEntity userInfo) {
        this.userInfo = userInfo;
    }

    public List<CategoryListEntity> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(List<CategoryListEntity> category_list) {
        this.category_list = category_list;
    }

    public static class UserInfoEntity {
        /**
         * city_id : 110100
         * city_name : 北京市
         */

        private String city_id;
        private String city_name;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }

    public static class CategoryListEntity {
        /**
         * categoryName : 口唇整形
         * categoryId : 40
         * canCatch : true
         * good_list : []
         */

        private String categoryName;    // 专区名称
        private String categoryId;         // ID
        private boolean canCatch;       // 当前用户是否能抢该推广位
        private List<GoodListEntity> good_list; // 该专区已有推广商品

        public String getCategoryName() {
            return !TextUtils.isEmpty(categoryName) ? categoryName + "专区" : "";
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public boolean isCanCatch() {
            return canCatch;
        }

        public void setCanCatch(boolean canCatch) {
            this.canCatch = canCatch;
        }

        public List<GoodListEntity> getGood_list() {
            return good_list;
        }

        public GoodListEntity getGood_lists() {
            return good_list != null && good_list.size() > 0 ? good_list.get(0) : new GoodListEntity();
        }

        public void setGood_list(List<GoodListEntity> good_list) {
            this.good_list = good_list;
        }

        public static class GoodListEntity {

            /**
             * id : 1
             * extension_id : 23
             * name : 222开内眼角 可升级贝塞尔技术 眼综合双眼皮修改
             * buy_price : 45452.00
             * spell_price : 11255.00
             * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
             * grade : 6.9
             * sales_volume : 0
             * preset_amount : 0.00
             * can_cancel : false
             */

            private String id;  // 商品ID
            private String extension_id;     // 推广的ID，删除推广时要用到
            private String name;     // 名称
            private String buy_price;   // 抢购价
            private String spell_price; // 拼团价
            private String image;       // 图片
            private String grade;       // 评分
            private String sales_volume;    // 销量
            private String preset_amount;   // 搜索推广消费限额
            private boolean can_cancel;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getExtension_id() {
                return extension_id;
            }

            public void setExtension_id(String extension_id) {
                this.extension_id = extension_id;
            }

            public String getName() {
                return !TextUtils.isEmpty(name) ? name : "";
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getBuy_price() {
                return !TextUtils.isEmpty(buy_price) ? buy_price : "0.00";
            }

            public void setBuy_price(String buy_price) {
                this.buy_price = buy_price;
            }

            public String getSpell_price() {
                return spell_price;
            }

            public void setSpell_price(String spell_price) {
                this.spell_price = spell_price;
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

            public String getSales_volume() {
                return sales_volume;
            }

            public void setSales_volume(String sales_volume) {
                this.sales_volume = sales_volume;
            }

            public String getPreset_amount() {
                return !TextUtils.isEmpty(preset_amount) ? preset_amount : "0.00";
            }

            public void setPreset_amount(String preset_amount) {
                this.preset_amount = preset_amount;
            }

            public boolean isCan_cancel() {
                return can_cancel;
            }

            public void setCan_cancel(boolean can_cancel) {
                this.can_cancel = can_cancel;
            }
        }
    }
}

package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.DateUtils;

import java.util.List;

public class RecordListBean {
    /**
     * id : 5
     * order_id : 106
     * image : []
     * create_time : 1557394040
     * hosipital_info : {"user_id":8,"name":"北京三甲医院","image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","is_VIP":1,"grade":"9.0","level_name":"","city_id":140100}
     * doctor_info : {"user_id":10,"name":"阿布","image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","is_VIP":1,"grade":"6.3","level_name":"","city_id":110100}
     * counselling_info : {"user_id":3,"name":"阿姐","image":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","is_VIP":1,"grade":"7.7","level_name":"VIP咨询师","city_id":110100}
     */

    private String id;
    private String order_id;                    // 订单id
    private String create_time;
    private HosipitalInfoEntity hosipital_info;     // 咨询师
    private HosipitalInfoEntity doctor_info;        // 医生
    private HosipitalInfoEntity counselling_info;   // 咨询师
    private List<String> image;                 // 美丽相片数组

    public String getTime() {
        return DateUtils.dataTime(create_time);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public HosipitalInfoEntity getHosipital_info() {
        return hosipital_info;
    }

    public void setHosipital_info(HosipitalInfoEntity hosipital_info) {
        this.hosipital_info = hosipital_info;
    }

    public HosipitalInfoEntity getDoctor_info() {
        return doctor_info;
    }

    public void setDoctor_info(HosipitalInfoEntity doctor_info) {
        this.doctor_info = doctor_info;
    }

    public HosipitalInfoEntity getCounselling_info() {
        return counselling_info;
    }

    public void setCounselling_info(HosipitalInfoEntity counselling_info) {
        this.counselling_info = counselling_info;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public static class HosipitalInfoEntity {
        /**
         * user_id : 8
         * name : 北京三甲医院
         * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * is_VIP : 1
         * grade : 9.0
         * level_name :
         * city_id : 140100
         */

        //医院 医生 咨询师
        private String user_id;     // 用户id
        private String name;        // 用户昵称
        private String image;       // 头像
        private String is_VIP;      // VIP 0 否 1是
        private String grade;       // 评分
        private String level_name;  // 用户职称
        private String city_id;     // 城市id

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getLevel_name() {
            return !TextUtils.isEmpty(level_name) ? level_name : "";
        }

        public void setLevel_name(String level_name) {
            this.level_name = level_name;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }
    }
}
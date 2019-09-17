package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.List;

public class UserPlanOrderDetailsBean {
    /**
     * id : 9
     * plan_number : CzB403760564070345
     * uid : 1
     * category_id : 48
     * city_id : 110000
     * plan_days : 3
     * people_number : 3
     * price : 2000.00
     * hospital_id : null
     * status : 2
     * dec : 我是阿布， 我是描述
     * category_firstid : 1
     * category_name : 呼吸1
     * create_time : 1555149335
     * update_time : null
     * delete_time : null
     * end_time : 1555408535
     * schedule : 200
     * city : 北京市
     * plan : 100%
     * join_user : [{"id":5,"plan_id":9,"plan_number":"CZ_MRC425905532708545","prepaylog_id":null,"user_id":1,"status":2,"create_status":"1","pay_price":"200.00","pay_status":1,"order_code":"MxH21Qo","order_qrcode":"www.vue_admin.com//qrcode/mrc/7e/17c/7e17cb176567ddc50b7baa4455ea8281ca0d1a50.png","create_time":null,"update_time":null,"delete_time":null,"user_name":"阿布","user_logo":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=1.jpg","is_vip":1,"user_type":1,"follow":0},{"id":11,"plan_id":9,"plan_number":"CZ_MRC507126392070170","prepaylog_id":null,"user_id":40,"status":2,"create_status":"0","pay_price":"2000.00","pay_status":0,"order_code":"MGQ1g6G","order_qrcode":"www.vue_admin.com//qrcode/mrc/cc/0a8/cc0a8a7d4cee164a8b627401995a6104f3f1370a.png","create_time":1557212639,"update_time":null,"delete_time":null,"user_name":"Jack","user_logo":"","is_vip":1,"user_type":0,"follow":0},{"id":12,"plan_id":9,"plan_number":"CZ_MRC507129773263599","prepaylog_id":17,"user_id":2,"status":2,"create_status":"0","pay_price":"2000.00","pay_status":0,"order_code":"M3Sfiy2","order_qrcode":"www.vue_admin.com//qrcode/mrc/b2/59e/b259e19845b5ad460446b228f2c63b9d16266bcb.png","create_time":1557212977,"update_time":null,"delete_time":null,"user_name":"大步","user_logo":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","is_vip":1,"user_type":0,"follow":0}]
     * join_organization : [{"plan_id":9,"hospital_id":9,"user_name":"北京美容解放医院","logo":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","order_num":1,"grade":"7.0","is_VIP":1,"is_selected":0}]
     * order_code : MxH21Qo
     * order_qrcode :
     */

    private String id;           // 美人筹发起的活动订单ID
    private String plan_number;  // 活动订单号
    private String uid;          // 发起人id
    private String category_id;  // 所属分类id（3级）
    private String city_id;      // 城市id
    private String plan_days;    // 筹集天数
    private String people_number;// 筹集人数
    private String price;        // 筹集价格
    private String hospital_id;
    private String status;       // 订单状态 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
    private String dec;          // 描述
    private String category_firstid;    // 一级分类id
    private String category_name;// 三级分类name
    private String create_time;
    private String update_time;
    private String delete_time;
    private String end_time;    // 活动订单结束时间
    private String schedule;    // 已筹金额
    private String city;        // 城市
    private String plan;        // 完成度
    private String order_code;  // 验证码
    private String order_qrcode;    // 验证二维码
    private String pay_time;    // 支付时间
    private List<JoinUserEntity> join_user; // 参与的用户列表
    private List<JoinOrganizationEntity> join_organization; // 参与的机构列表

   /* //参与人数
    public String join_user_num() {
        return join_user != null && join_user.size() > 0 ? String.valueOf(join_user.size()) : "0";
    }*/

    //普通用户订单状态 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
    public String getStatusName() {
        if (status.equals("0")) {
            return "待付款";
        } else if (status.equals("1")) {
            return "未达成";
        } else if (status.equals("2")) {
            return "待使用";
        } else if (status.equals("3")) {
            return "已完成";
        } else if (status.equals("4")) {
            return "已取消";
        } else {
            return "";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlan_number() {
        return !TextUtils.isEmpty(plan_number) ? plan_number : "";
    }

    public void setPlan_number(String plan_number) {
        this.plan_number = plan_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getPlan_days() {
        return plan_days;
    }

    public void setPlan_days(String plan_days) {
        this.plan_days = plan_days;
    }

    public String getPeople_number() {
        return !TextUtils.isEmpty(people_number) ? people_number : "0";
    }

    public void setPeople_number(String people_number) {
        this.people_number = people_number;
    }

    public String getPrice() {
        return !TextUtils.isEmpty(price) ? price : "0";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDec() {
        return !TextUtils.isEmpty(dec) ? dec : "";
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getCategory_firstid() {
        return category_firstid;
    }

    public void setCategory_firstid(String category_firstid) {
        this.category_firstid = category_firstid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPay_time() {
        return !TextUtils.isEmpty(pay_time) ? pay_time : "";
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getSchedule() {
        return !TextUtils.isEmpty(schedule) ? schedule : "0";
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCity() {
        return !TextUtils.isEmpty(city) ? city : "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlan() {
        return !TextUtils.isEmpty(plan) ? plan : "0%";
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getOrder_code() {
        return !TextUtils.isEmpty(order_code) ? order_code : " ";
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_qrcode() {
        return order_qrcode;
    }

    public void setOrder_qrcode(String order_qrcode) {
        this.order_qrcode = order_qrcode;
    }

    public List<JoinUserEntity> getJoin_user() {
        return join_user;
    }

    public void setJoin_user(List<JoinUserEntity> join_user) {
        this.join_user = join_user;
    }

    public List<JoinOrganizationEntity> getJoin_organization() {
        return join_organization;
    }

    public void setJoin_organization(List<JoinOrganizationEntity> join_organization) {
        this.join_organization = join_organization;
    }

    public static class JoinUserEntity {
        /**
         * id : 5
         * plan_id : 9
         * plan_number : CZ_MRC425905532708545
         * prepaylog_id : null
         * user_id : 1
         * status : 2
         * create_status : 1
         * pay_price : 200.00
         * pay_status : 1
         * order_code : MxH21Qo
         * order_qrcode : www.vue_admin.com//qrcode/mrc/7e/17c/7e17cb176567ddc50b7baa4455ea8281ca0d1a50.png
         * create_time : null
         * update_time : null
         * delete_time : null
         * user_name : 阿布
         * user_logo : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=1.jpg
         * is_vip : 1
         * user_type : 1
         * follow : 0
         */

        private String id;
        private String plan_id;
        private String plan_number;
        private String prepaylog_id;
        private String user_id;
        private String status;
        private String create_status;
        private String pay_price;
        private String pay_status;      // 用户支付状态  0-未支付  1-已支付
        private String order_code;
        private String order_qrcode;
        private String create_time;
        private String update_time;
        private String delete_time;
        private String user_name;       // 用户名
        private String user_logo;       // 用户头像
        private String is_vip;
        private String user_type;       // 用户类型 0-参与  1-发起
        private String follow;          // 是否关注  0 否  1 是

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getPlan_number() {
            return plan_number;
        }

        public void setPlan_number(String plan_number) {
            this.plan_number = plan_number;
        }

        public String getPrepaylog_id() {
            return prepaylog_id;
        }

        public void setPrepaylog_id(String prepaylog_id) {
            this.prepaylog_id = prepaylog_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_status() {
            return create_status;
        }

        public void setCreate_status(String create_status) {
            this.create_status = create_status;
        }

        public String getPay_price() {
            return pay_price;
        }

        public void setPay_price(String pay_price) {
            this.pay_price = pay_price;
        }

        //用户支付状态  0-未支付  1-已支付
        public String getPay_status() {
            return !TextUtils.isEmpty(pay_status) && pay_status.equals("1") ? "已付款" : "待付款";
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getOrder_code() {
            return order_code;
        }

        public void setOrder_code(String order_code) {
            this.order_code = order_code;
        }

        public String getOrder_qrcode() {
            return order_qrcode;
        }

        public void setOrder_qrcode(String order_qrcode) {
            this.order_qrcode = order_qrcode;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getDelete_time() {
            return delete_time;
        }

        public void setDelete_time(String delete_time) {
            this.delete_time = delete_time;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_logo() {
            return user_logo;
        }

        public void setUser_logo(String user_logo) {
            this.user_logo = user_logo;
        }

        public String getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(String is_vip) {
            this.is_vip = is_vip;
        }

        public String getUser_type() {
            return !TextUtils.isEmpty(user_type) && user_type.equals("1") ?
                    "发起人" : "参与人";
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }
    }

    public static class JoinOrganizationEntity {
        /**
         * plan_id : 9
         * hospital_id : 9
         * user_name : 北京美容解放医院
         * logo : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * order_num : 1
         * grade : 7.0
         * is_VIP : 1
         * is_selected : 0
         */

        private String plan_id;
        private String hospital_id; // 机构id
        private String user_name;   // 机构名称
        private String logo;        // 机构封面
        private String order_num;   // 下单量
        private String grade;       // 评分
        private String is_VIP;      // 是否VIP 0-否 1-是
        private String is_selected; // 是否选中 0-未选中 1-已选中

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(String hospital_id) {
            this.hospital_id = hospital_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }

        public String getIs_selected() {
            return is_selected;
        }

        public void setIs_selected(String is_selected) {
            this.is_selected = is_selected;
        }
    }
}

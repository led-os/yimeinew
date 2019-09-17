package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.chengzi.app.constants.Sys;

import java.util.List;

public class UserPlanOrderListBean {

    private String id;
    private String plan_id;    // 美人筹订单id
    private PlanDetailsEntity plan_details;     // 美人筹订单详情

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

    public PlanDetailsEntity getPlan_details() {
        return plan_details;
    }

    public void setPlan_details(PlanDetailsEntity plan_details) {
        this.plan_details = plan_details;
    }

    public static class PlanDetailsEntity {
        /**
         * id : 10
         * plan_number : CzB403762613016621
         * uid : 3
         * category_id : 48
         * city_id : 110000
         * plan_days : 3
         * people_number : 3
         * price : 2000.00
         * hospital_id : null
         * status : 1
         * dec : 我是阿布啊， 我是描述
         * category_firstid : 1
         * category_name : 呼吸1
         * create_time : null
         * update_time : null
         * delete_time : null
         * end_time : 0
         * schedule : 400
         * city : 北京
         * plan : 100%
         * join_user : [{"id":1,"plan_id":10,"plan_number":null,"user_id":3,"status":4,"create_status":"1","pay_price":"200.00","pay_status":1,"order_code":null,"order_qrcode":null,"create_time":null,"update_time":null,"delete_time":null,"user_name":"阿姐","follow":1},{"id":2,"plan_id":10,"plan_number":null,"user_id":5,"status":0,"create_status":"0","pay_price":"300.00","pay_status":0,"order_code":null,"order_qrcode":null,"create_time":null,"update_time":null,"delete_time":null,"user_name":"北京三甲医院","follow":0},{"id":3,"plan_id":10,"plan_number":null,"user_id":1,"status":4,"create_status":"1","pay_price":"200.00","pay_status":1,"order_code":"kl545jd","order_qrcode":"5464.jpg","create_time":null,"update_time":null,"delete_time":null,"user_name":"阿布","follow":0}]
         * join_organization : [{"plan_id":10,"hospital_id":8,"user_name":"北京三甲医院","logo":["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"],"order_num":5,"grade":"6.4"},{"plan_id":10,"hospital_id":9,"user_name":"北京美容解放医院","logo":["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"],"order_num":0,"grade":"8.4"}]
         */

        private String id;            // 美人筹发起的活动订单ID
        private String plan_number;   // 活动订单号
        private String uid;           // 发起人id
        private String category_id;   // 所属分类id（3级）
        private String city_id;       // 城市id
        private String plan_days;     // 筹集天数
        private String people_number; // 筹集人数
        private String price;         // 筹集价格
        private String hospital_id;
        private String status;        // 订单状态 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
        private String dec;           // 描述
        private String category_firstid;  // 一级分类id
        private String category_name;     // 3级分类名字
        private String create_time;
        private String update_time;
        private String delete_time;
        private long end_time;      // 活动订单结束时间
        private long remain_time;
        private String schedule;      // 已筹金额
        private String city;          // 城市
        private String plan;          // 完成度
        private List<JoinUserEntity> join_user;      // 参与的用户列表
        private List<JoinOrganizationEntity> join_organization;      // 参与的机构

        public long getRemain_time() {
            return remain_time;
        }

        public void setRemain_time(long remain_time) {
            this.remain_time = remain_time;
        }

        public String getStatus_name() {
            // 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
            if (TextUtils.equals(status, "0")) {
                return "待付款";
            } else if (TextUtils.equals(status, "1")) {
                if (AccountHelper.getIdentity() == Sys.TYPE_USER)
                    return "未达成";
                else
                    return "进行中";
            } else if (TextUtils.equals(status, "2")) {
                return "待使用";
            } else if (TextUtils.equals(status, "3")) {
                return "已完成";
            } else if (TextUtils.equals(status, "4")) {
                return "已取消";
            } else {
                return "";
            }
        }

        public String getStatus_names() {
            // 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消

            if (TextUtils.equals(status, "0") || TextUtils.equals(status, "1")) {
                return "进行中";
            } else if (TextUtils.equals(status, "2") || TextUtils.equals(status, "3")) {
                return "已完成";
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
            return plan_number;
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
            return people_number;
        }

        public void setPeople_number(String people_number) {
            this.people_number = people_number;
        }

        public String getPrice() {
            return price;
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

        public int getStatusInt() {
            return Integer.valueOf(TextUtils.isEmpty(status) ? "0" : status);
        }

        ;

        public String getDec() {
            return dec;
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

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public String getSchedule() {
            return !TextUtils.isEmpty(schedule) ? schedule : "0.00";
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
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

            private String id;
            private String plan_id;
            private String plan_number;
            private String user_id;
            private String status;
            private String create_status;
            private String pay_price;
            private String pay_status;      // 用户支付状态  0-未支付 1-已支付
            private String order_code;      //
            private String order_qrcode;
            private String create_time;
            private String update_time;
            private String delete_time;
            private String user_name;       // 用户名
            private String user_logo;       // 用户头像
            private String user_type;       // 用户类型  0-参与 1-发起
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

            public String getPay_status() {
                return pay_status;
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

            public String getUser_type() {
                return user_type;
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
             * plan_id : 10
             * hospital_id : 8
             * user_name : 北京三甲医院
             * logo : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
             * order_num : 5
             * grade : 6.4
             */

            private String plan_id;
            private String hospital_id; //机构id
            private String user_name;   //机构名称
            private String logo;        //机构封面
            private String order_num;   //下单量
            private String grade;       //评分
            private String is_VIP;      //是否是VIP  0-否 1-是
            private String is_selected;      //是否选中  0-未选中 1-已选中

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

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
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
}

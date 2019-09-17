package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.chengzi.app.utils.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserOrderListBean implements Serializable {

    private String id;              // 订单id
    private String user_id;         // 下单者用户id
    private String hospital_id;     // 医院id
    private String goods_id;        // 商品id
    private String doctor_id;       // 医生id
    private String counselling_id;  // 医生id
    private String amount;          // 订单金额
    private String amount_spreads;  // 补差价
    private String amount_discount; // 优惠金额
    private String amount_pay;      // 支付金额
    private String number;          // 购买数量（业务逻辑都是1）
    private String type;            // 订单类型 1-普通订单 2-拼购订单 3-秒杀订单
    private String status;          // 订单状态 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消
    private String h_status;        // 机构订单状态（结合status使用），默认0待使用，1待评价，2已完成
    private String payment_type;
    private String complete_time;   // 订单完成时间，时间戳   (投诉时间)
    private String checking_time;   // 验证订单的时间(时间戳秒)
    private String create_time;     // 创建时间
    private String update_time;
    private String delete_time;
    private String order_number_id; // 订单号
    private String prepaylog_id;    // 发起支付id
    private String coupon_id;       // 优惠券id
    private String cancel_reason;   // 订单取消原因
    private String order_code;      // 验证码->kl545jd
    private String order_qrcode;    // 验证二维码(图片)
    private String type_name;       // 购买方式 1-直接购买, 2-拼购, 3-限时秒杀
    private String goods_name;      // 商品名称
    private String goods_image;     // 商品封面
    private String goods_status;        //商品状态 1-上架 2-下架
    private String remain_time;     // 拼购剩余时间--->剩余秒数 -->自己计算
    private String isComplaint;     // 是否已投诉 0-未投诉，1-已投诉

    private String payTime; //  支付时间

    //
    private String status_name;          // 订单状态 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消


    //以下部分是订单详情中有的
    /**
     * pay_time :
     * doctor_info : {"user_id":10,"name":"阿布","occupation_class":"医师","is_VIP":1}
     * counselling_info : {"user_id":7,"name":"笑笑二号","consultant_rank":"普通咨询师","is_VIP":0}
     * isComplaint : 1
     * complaint_info : {"complaint_obj":"用户,医生,咨询师","complaint_content":"投诉测试"}
     * evaluation : {"doctor_skill":2,"doctor_major":3,"consultant_service":4,"consultant_major":5,"content":"医生技术没的说，咨询师服务也没说的！"}
     * spell_promoter : {"user_id":1,"user_name":"阿布","user_image":"www.vue_admin.com1.jpg","pay_status":"已付款"}
     * spell_participant : {"user_id":2,"user_name":"大步","user_image":"www.vue_admin.com1.jpg","pay_status":"待付款"}
     */

    private String pay_time;
    private DoctorInfoEntity doctor_info;           // 医生信息（如果没有指定医生，该数组为空）
    private CounsellingInfoEntity counselling_info; // 咨询师信息（如果没有指定咨询师，该数组为空）
    private ComplaintInfoEntity complaint_info;     // 投诉信息（如果没有投诉，该数组为空）
    private EvaluationEntity evaluation;            // 评价评分信息（如果没有评价，该值为空）
    private SpellPromoterEntity spell_promoter;     // 拼购订单的发起人信息
    private SpellPromoterEntity spell_participant;  // 拼购订单的参与者信息（没有参与者，该数组为空）

    //医院详情-->必有
    private UserInfoEntity user_info;  //                                  // 下单用户信息


    public String getStatus_name() {
        if (status.equals("1"))
            return "待付款";
        else if (status.equals("2"))
            return "未达成";
        else if (status.equals("3"))
            return "待使用";
        else if (status.equals("4"))
            return "待评价";
        else if (status.equals("5"))
            return "已完成";
        else if (status.equals("6"))
            return "已取消";
        else
            return "";
    }

    //机构订单状态 0-全部 1-待使用，2-待评价，3-已完成
    public String getHStatus_name() {
        if (h_status.equals("1"))
            return "待使用";
        else if (h_status.equals("2"))
            return "待评价";
        else if (h_status.equals("3"))
            return "已完成";
        else
            return "";
    }

    //订单详情中用
    public String getStatus_details_name() {
        if (status.equals("1"))
            return "待付款";
        else if (status.equals("2"))
            return "未达成";
        else if (status.equals("3"))
            return "待使用";
        else if (status.equals("4"))
            return "待评价";
        else if (status.equals("5"))
            return "已完成";
        else if (status.equals("6"))
//            return "超时未支付，订单自动取消";
            return cancel_reason;
        else
            return "";
    }

    public String getPrepaylog_id() {
        return prepaylog_id;
    }

    public void setPrepaylog_id(String prepaylog_id) {
        this.prepaylog_id = prepaylog_id;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getDoctor_id() {
        return !TextUtils.isEmpty(doctor_id) ? doctor_id : "0";
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCounselling_id() {
        return !TextUtils.isEmpty(counselling_id) ? counselling_id : "0";
    }

    public void setCounselling_id(String counselling_id) {
        this.counselling_id = counselling_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_spreads() {
        return !TextUtils.isEmpty(amount_spreads) ? amount_spreads : "0";
    }

    public void setAmount_spreads(String amount_spreads) {
        this.amount_spreads = amount_spreads;
    }

    public String getAmount_discount() {
        return amount_discount;
    }

    public void setAmount_discount(String amount_discount) {
        this.amount_discount = amount_discount;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getH_status() {
        return h_status;
    }

    public void setH_status(String h_status) {
        this.h_status = h_status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getChecking_time() {
        return checking_time;
    }

    public String getChecking_times() {   //2018年03月18日  16:12
        return !TextUtils.isEmpty(getChecking_time()) ? DateUtils.dataTime(getChecking_time(), "yyyy年MM月dd日 HH:mm") : "";
    }

    public void setChecking_time(String checking_time) {
        this.checking_time = checking_time;
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

    public String getOrder_number_id() {
        return order_number_id;
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCancel_reason() {
        return TextUtils.isEmpty(cancel_reason) ? cancel_reason : "超时未支付，订单自动取消";
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getOrder_code() {
        return !TextUtils.isEmpty(order_code) ? order_code : "";
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_qrcode() {
        return !TextUtils.isEmpty(order_qrcode) ? order_qrcode : "";
    }

    public void setOrder_qrcode(String order_qrcode) {
        this.order_qrcode = order_qrcode;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    ////商品状态 1-上架 2-下架
    public String getGoods_status() {
        return !TextUtils.isEmpty(goods_status) ? goods_status : "1";
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
    }

    public String getRemain_time() {
        return remain_time;
    }

    public void setRemain_time(String remain_time) {
        this.remain_time = remain_time;
    }

    public String getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(String isComplaint) {
        this.isComplaint = isComplaint;
    }

    public String getPay_time() { //时间戳
        return !TextUtils.isEmpty(pay_time) ? pay_time : "";
    }


    public String getPay_times() {
        return !TextUtils.isEmpty(getPay_time()) ? DateUtils.dataTime(getPay_time(), "yyyy年MM月dd日 HH:mm") : "";
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public DoctorInfoEntity getDoctor_info() {
        return doctor_info;
    }

    public void setDoctor_info(DoctorInfoEntity doctor_info) {
        this.doctor_info = doctor_info;
    }

    public CounsellingInfoEntity getCounselling_info() {
        return counselling_info;
    }

    public void setCounselling_info(CounsellingInfoEntity counselling_info) {
        this.counselling_info = counselling_info;
    }

    public ComplaintInfoEntity getComplaint_info() {
        return complaint_info;
    }

    public void setComplaint_info(ComplaintInfoEntity complaint_info) {
        this.complaint_info = complaint_info;
    }

    public EvaluationEntity getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationEntity evaluation) {
        this.evaluation = evaluation;
    }

    public SpellPromoterEntity getSpell_promoter() {
        return spell_promoter;
    }

    public void setSpell_promoter(SpellPromoterEntity spell_promoter) {
        this.spell_promoter = spell_promoter;
    }

    public SpellPromoterEntity getSpell_participant() {
        return spell_participant;
    }

    public void setSpell_participant(SpellPromoterEntity spell_participant) {
        this.spell_participant = spell_participant;
    }

    // 医生 信息（如果没有指定医生，该数组为空）
    public static class DoctorInfoEntity implements Serializable {
        /**
         * user_id : 10
         * name : 阿布
         * occupation_class : 医师
         * is_VIP : 1
         */

        private String user_id;               // 医生 用户id
        private String name;                  // 医生 姓名
        private String occupation_class;      // 医生 职称
        private String is_VIP;                // 是否是VIP 0 否 1是

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

        public String getOccupation_class() {
            return !TextUtils.isEmpty(occupation_class) ? occupation_class : "";
        }

        public void setOccupation_class(String occupation_class) {
            this.occupation_class = occupation_class;
        }

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }
    }

    // 咨询师 信息（如果没有指定咨询师，该数组为空）
    public static class CounsellingInfoEntity implements Serializable {
        /**
         * user_id : 10
         * name : 阿布
         * occupation_class : 医师
         * is_VIP : 1
         */

        private String user_id;               // 咨询师 用户id
        private String name;                  // 咨询师 姓名
        private String consultant_rank;       // 咨询师 职称
        private String is_VIP;                // 是否是VIP 0 否 1是

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

        public String getConsultant_rank() {
            return !TextUtils.isEmpty(consultant_rank) ? consultant_rank : "";
        }

        public void setConsultant_rank(String consultant_rank) {
            this.consultant_rank = consultant_rank;
        }

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }
    }

    // 投诉信息（如果没有投诉，该数组为空）
    public static class ComplaintInfoEntity {
        /**
         * complaint_obj : 用户,医生,咨询师
         * complaint_content : 投诉测试
         */

        private String complaint_obj;       // 投诉对象
        private String complaint_content;   // 投诉内容

        public String getComplaint_obj() {
            return complaint_obj;
        }

        public void setComplaint_obj(String complaint_obj) {
            this.complaint_obj = complaint_obj;
        }

        public String getComplaint_content() {
            return complaint_content;
        }

        public void setComplaint_content(String complaint_content) {
            this.complaint_content = complaint_content;
        }
    }

    // 评价评分信息（如果没有评价，该值为空）
    public static class EvaluationEntity {
        /**
         * doctor_skill : 2
         * doctor_major : 3
         * consultant_service : 4
         * consultant_major : 5
         * content : 医生技术没的说，咨询师服务也没说的！
         */

        private String doctor_skill;      // 医生技术分
        private String doctor_major;      // 医生专业分
        private String consultant_service;// 咨询师服务分
        private String consultant_major;  // 咨询师专业分
        private String content;           // 用户评论的内容

        public String getDoctor_skill() {
            return doctor_skill;
        }

        public void setDoctor_skill(String doctor_skill) {
            this.doctor_skill = doctor_skill;
        }

        public String getDoctor_major() {
            return doctor_major;
        }

        public void setDoctor_major(String doctor_major) {
            this.doctor_major = doctor_major;
        }

        public String getConsultant_service() {
            return consultant_service;
        }

        public void setConsultant_service(String consultant_service) {
            this.consultant_service = consultant_service;
        }

        public String getConsultant_major() {
            return consultant_major;
        }

        public void setConsultant_major(String consultant_major) {
            this.consultant_major = consultant_major;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // 拼购订单的发起人信息 / 拼购订单的参与者信息（没有参与者，该数组为空）
    public static class SpellPromoterEntity {
        /**
         * user_id : 1
         * user_name : 阿布
         * user_image : www.vue_admin.com1.jpg
         * pay_status : 已付款
         */

        private String user_id;       // 发起者/参与者 用户id
        private String user_name;     // 发起者/参与者 姓名
        private String user_image;    // 发起者/参与者 头像
        private String pay_status;    // 发起者/参与者 订单付款状态

        //接口未返回 需要显示是发起人还是参与人
        private String people_type;

        public SpellPromoterEntity(String user_id, String user_name, String user_image, String pay_status, String people_type) {
            this.user_id = user_id;
            this.user_name = user_name;
            this.user_image = user_image;
            this.pay_status = pay_status;
            this.people_type = people_type;
        }

        public String getPeople_type() {
            return people_type;
        }

        public void setPeople_type(String people_type) {
            this.people_type = people_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }
    }

    // 下单用户信息
    public static class UserInfoEntity {
        /**
         * user_id : 1
         * user_name : 阿布
         * user_image : www.vue_admin.com1.jpg
         * "mobile": "15801629091",                       // 下单用户电话
         * is_VIP : 1
         */

        private String user_id; // 下单用户id
        private String name;    // 下单用户昵称
        private String logo;    // 下单用户头像
        private String mobile;  // 下单用户电话
        private String is_VIP;  // 下单用户是否是VIP 0 否 1是

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIs_VIP() {
            return is_VIP;
        }

        public void setIs_VIP(String is_VIP) {
            this.is_VIP = is_VIP;
        }
    }

    public UserInfoEntity getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoEntity user_info) {
        this.user_info = user_info;
    }

    public String getPayTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        payTime = sdf.format(new Date());
        return payTime;
    }
}

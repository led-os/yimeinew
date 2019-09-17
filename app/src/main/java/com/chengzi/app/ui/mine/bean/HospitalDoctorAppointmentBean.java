package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class HospitalDoctorAppointmentBean {
    /**
     * appo_id : 25
     * customer : {"id":47,"token":"a823788c9696875f26c3bdb61cb53903","name":"大雄","headimg":"http://medicalbeauty.oss-cn-beijing.aliyuncs.com/156050458825847","cover":"http://pic.inchengzi.com/156083859588347","mobile":"18800000002","telephone":null,"address":null,"grade":"9.5","skill_grade":"9.0","type":2,"occupation_name":"医师","consultant_name":"普通咨询师","gender":1,"age":49,"vip_endtime":null,"is_frozen":false,"is_vip":false,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     * appo_name : 香香
     * appo_phone : 13426125201
     * appo_content : 看看
     * appo_time : 2019-06-19 10:00
     * String : {"id":47,"token":"a823788c9696875f26c3bdb61cb53903","name":"大雄","headimg":"http://medicalbeauty.oss-cn-beijing.aliyuncs.com/156050458825847","cover":"http://pic.inchengzi.com/156083859588347","mobile":"18800000002","telephone":null,"address":null,"grade":"9.5","skill_grade":"9.0","type":2,"occupation_name":"医师","consultant_name":"普通咨询师","gender":1,"age":49,"vip_endtime":null,"is_frozen":false,"is_vip":false,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     */

    private String appo_id;            // 预约ID
    private CustomerEntity customer;   // 预约者（客户）的信息
    // 预约时填的姓名、手机号、预约需求和预约时间
    private String appo_name;
    private String appo_phone;
    private String appo_content;
    private String appo_time;
    private CustomerEntity object;   // 被预约者（医院或医生）的信息

    public String getAppo_id() {
        return appo_id;
    }

    public void setAppo_id(String appo_id) {
        this.appo_id = appo_id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public String getAppo_name() {
        return !TextUtils.isEmpty(appo_name) ? appo_name : "";
    }

    public void setAppo_name(String appo_name) {
        this.appo_name = appo_name;
    }

    public String getAppo_phone() {
        return !TextUtils.isEmpty(appo_phone) ? appo_phone : "";
    }

    public void setAppo_phone(String appo_phone) {
        this.appo_phone = appo_phone;
    }

    public String getAppo_content() {
        return !TextUtils.isEmpty(appo_content) ? appo_content : "";
    }

    public void setAppo_content(String appo_content) {
        this.appo_content = appo_content;
    }

    public String getAppo_time() {
        return !TextUtils.isEmpty(appo_time) ? appo_time : "";
    }

    public void setAppo_time(String appo_time) {
        this.appo_time = appo_time;
    }

    public CustomerEntity getObject() {
        return object;
    }

    public void setObject(CustomerEntity object) {
        this.object = object;
    }

    public class CustomerEntity {
        /**
         * id : 47
         * token : a823788c9696875f26c3bdb61cb53903
         * name : 大雄
         * headimg : http://medicalbeauty.oss-cn-beijing.aliyuncs.com/156050458825847
         * cover : http://pic.inchengzi.com/156083859588347
         * mobile : 18800000002
         * telephone : null
         * address : null
         * grade : 9.5
         * skill_grade : 9.0
         * type : 2
         * occupation_name : 医师
         * consultant_name : 普通咨询师
         * gender : 1
         * age : 49
         * vip_endtime : null
         * is_frozen : false
         * is_vip : false
         * is_auth : true
         * is_auth_push : false
         * is_auth_dis : false
         */

        private String id;
        private String token;
        private String name;
        private String headimg;
        private String cover;
        private String mobile;
        private String telephone;
        private String address;
        private String grade;       //// 审美分
        private String skill_grade; //// 技术分
        private String type;
        private String occupation_name;
        private String consultant_name;
        private String gender;
        private String age;
        private String vip_endtime;
        private boolean is_frozen;
        private boolean is_vip;
        private boolean is_auth;
        private boolean is_auth_push;
        private boolean is_auth_dis;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGrade() {
            return !TextUtils.isEmpty(grade) ? grade : "0.0";
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSkill_grade() {
            return !TextUtils.isEmpty(skill_grade) ? skill_grade : "0.0";
        }

        public void setSkill_grade(String skill_grade) {
            this.skill_grade = skill_grade;
        }

        public String getType() {
            return !TextUtils.isEmpty(type) ? type : "4";
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOccupation_name() {
//            return occupation_name;
            if (!TextUtils.isEmpty(occupation_name)) {
                return getType().equals("2") ? occupation_name : "";
            }
            return "";
        }

        public void setOccupation_name(String occupation_name) {
            this.occupation_name = occupation_name;
        }

        public String getConsultant_name() {
            return consultant_name;
        }

        public void setConsultant_name(String consultant_name) {
            this.consultant_name = consultant_name;
        }

        public String getGender() {
            return !TextUtils.isEmpty(gender) ? gender : "0";
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getVip_endtime() {
            return vip_endtime;
        }

        public void setVip_endtime(String vip_endtime) {
            this.vip_endtime = vip_endtime;
        }

        public boolean isIs_frozen() {
            return is_frozen;
        }

        public void setIs_frozen(boolean is_frozen) {
            this.is_frozen = is_frozen;
        }

        public boolean isIs_vip() {
            return is_vip;
        }

        public void setIs_vip(boolean is_vip) {
            this.is_vip = is_vip;
        }

        public boolean isIs_auth() {
            return is_auth;
        }

        public void setIs_auth(boolean is_auth) {
            this.is_auth = is_auth;
        }

        public boolean isIs_auth_push() {
            return is_auth_push;
        }

        public void setIs_auth_push(boolean is_auth_push) {
            this.is_auth_push = is_auth_push;
        }

        public boolean isIs_auth_dis() {
            return is_auth_dis;
        }

        public void setIs_auth_dis(boolean is_auth_dis) {
            this.is_auth_dis = is_auth_dis;
        }
    }
}
package com.chengzi.app.ui.mine.bean;

/**
 * @Desc:
 * @ClassName:AliUsrInfo
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/26 0026
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class AliUserInfo {

    /**
     * alipay_user_info_share_response : {"code":"10000","msg":"Success","avatar":"https://tfs.alipayobjects.com/images/partner/TB1BSlXXDgIluNjHvSTXXc4iVXa","city":"北京市","gender":"m","is_certified":"T","is_student_certified":"F","nick_name":"梦璃","province":"北京","user_id":"2088112141881902","user_status":"T","user_type":"2"}
     * sign : sNRUDbSD3ySLUXd8bZV72utdc82s23PKPpz2I2vQ8R/A/NptqOxt8RtYWH2QorEF+/xdigEfl6YGlcVn3c5IOHa4O7xMzXWX+wmARWLRCOKlK+wR9Ur/WF7qZfKsxEEMu/zgx8BpGhFFtbpv5KomE+Ld1/4OpSqMrTKJL9PXjqvtwJZLJyWSk8PHax+LdZ/EQRDndvOFVPh7LxLhNjp3Lc9o3s3VOUYAX+MgrgsvWam0uNQH5hgQ5Jbu4I553tyd8FBEJDqsKEbb4CzEdcARH1zvau76k6kljR6jiJo42SZC0ij8gcDNlrDJ/SztW8GglpKQH8dJFwO6IrBkS750/Q==
     * accessToken : kuaijieB1032c7cc4b9c441b8d8dd018163d6B90
     * alipayuserid : 20880035772777303174928452617690
     */

    private AlipayUserInfoShareResponseBean alipay_user_info_share_response;
    private String sign;
    private String accessToken;
    private String alipayuserid;

    public AlipayUserInfoShareResponseBean getAlipay_user_info_share_response() {
        return alipay_user_info_share_response;
    }

    public void setAlipay_user_info_share_response(AlipayUserInfoShareResponseBean alipay_user_info_share_response) {
        this.alipay_user_info_share_response = alipay_user_info_share_response;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAccessToken() {
        return accessToken == null ? "" : accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAlipayuserid() {
        return alipayuserid == null ? "" : alipayuserid;
    }

    public void setAlipayuserid(String alipayuserid) {
        this.alipayuserid = alipayuserid;
    }

    public static class AlipayUserInfoShareResponseBean {
        /**
         * code : 10000
         * msg : Success
         * avatar : https://tfs.alipayobjects.com/images/partner/TB1BSlXXDgIluNjHvSTXXc4iVXa
         * city : 北京市
         * gender : m
         * is_certified : T
         * is_student_certified : F
         * nick_name : 梦璃
         * province : 北京
         * user_id : 2088112141881902
         * user_status : T
         * user_type : 2
         */

        private String code;
        private String msg;
        private String avatar;
        private String city;
        private String gender;
        private String is_certified;
        private String is_student_certified;
        private String nick_name;
        private String province;
        private String user_id;
        private String user_status;
        private String user_type;

        public String getCode() {
            return code == null ? "" : code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg == null ? "" : msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getAvatar() {
            return avatar == null ? "" : avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCity() {
            return city == null ? "" : city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGender() {
            return gender == null ? "" : gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIs_certified() {
            return is_certified == null ? "" : is_certified;
        }

        public void setIs_certified(String is_certified) {
            this.is_certified = is_certified;
        }

        public String getIs_student_certified() {
            return is_student_certified == null ? "" : is_student_certified;
        }

        public void setIs_student_certified(String is_student_certified) {
            this.is_student_certified = is_student_certified;
        }

        public String getNick_name() {
            return nick_name == null ? "" : nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getProvince() {
            return province == null ? "" : province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getUser_id() {
            return user_id == null ? "" : user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_status() {
            return user_status == null ? "" : user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getUser_type() {
            return user_type == null ? "" : user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}

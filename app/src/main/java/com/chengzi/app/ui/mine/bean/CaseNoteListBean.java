package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CaseNoteListBean {

    /**
     * cases_note_id : 3
     * cases_id : 1
     * user : {"id":8,"name":"北京三甲医院","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"15801629093","telephone":"400-1234-5678","address":"北京市朝阳区建国路181号","grade":"0.0","skill_grade":"9.0","type":4,"occupation_name":"主治医师","consultant_name":"VIP咨询师","gender":0,"age":49,"vip_endtime":null,"is_frozen":false,"is_vip":true,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     * content : rererrrrrrrrrrrrr
     * images : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * create_time : 2019/04/08
     */

    private String cases_note_id;   // 美丽日记ID
    private String cases_id;        // 所属案例ID
    private UserEntity user;
    private String content;         // 美丽日记文字内容
    private String create_time;     // 创建时间
    private List<String> images;    // 美丽日记图片

    private String casename;        //  案例类别名称
    private String like_number;
    private boolean isLike;
    private int comment_number;     //  评论数量

    public String getCases_note_id() {
        return cases_note_id;
    }

    public void setCases_note_id(String cases_note_id) {
        this.cases_note_id = cases_note_id;
    }

    public String getCases_id() {
        return cases_id;
    }

    public void setCases_id(String cases_id) {
        this.cases_id = cases_id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return !TextUtils.isEmpty(create_time) ? create_time : "";
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<String> getImages() {
        return images == null ? (images = new ArrayList<>()) : images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    public int getComment_number() {
        return comment_number;
    }

    public void setComment_number(int comment_number) {
        this.comment_number = comment_number;
    }

    public static class UserEntity {
        /**
         * id : 8
         * name : 北京三甲医院
         * headimg : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
         * mobile : 15801629093
         * telephone : 400-1234-5678
         * address : 北京市朝阳区建国路181号
         * grade : 0.0
         * skill_grade : 9.0
         * type : 4
         * occupation_name : 主治医师
         * consultant_name : VIP咨询师
         * gender : 0
         * age : 49
         * vip_endtime : null
         * is_frozen : false
         * is_vip : true
         * is_auth : true
         * is_auth_push : false
         * is_auth_dis : false
         */

        private String id;
        private String name;
        private String headimg;
        private String mobile;
        private String telephone;
        private String address;
        private String grade;
        private String skill_grade;
        private String type;
        private String occupation_name;
        private String consultant_name;
        private String gender;
        private String age;
        private Object vip_endtime;
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
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSkill_grade() {
            return skill_grade;
        }

        public void setSkill_grade(String skill_grade) {
            this.skill_grade = skill_grade;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOccupation_name() {
            return occupation_name;
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
            return gender;
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

        public Object getVip_endtime() {
            return vip_endtime;
        }

        public void setVip_endtime(Object vip_endtime) {
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

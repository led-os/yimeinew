package com.chengzi.app.ui.mine.bean;

import java.util.List;

public class LookReviewBean {


    /**
     * user_id : 26
     * doctor_id : 10
     * counselling_id : 3
     * content : 好评
     * c_content : null
     * image : ["http://medicalbeauty.oss-cn-beijing.aliyuncs.com/155739462116626","http://medicalbeauty.oss-cn-beijing.aliyuncs.com/155739462139926"]
     * evalu_create_time : 2019-05-09 05:37
     * user : {"id":26,"name":"呆萌和画师、","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"18310681301","telephone":null,"address":"北京市朝阳区建国路181号","grade":"8.9","skill_grade":"0.0","type":1,"occupation_name":"","consultant_name":"VIP咨询师","is_frozen":false,"is_vip":true,"is_auth":false,"is_auth_push":true,"is_auth_dis":false,"age":10,"gender":0}
     * doctor : {"id":10,"name":"阿布","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"15801629071","telephone":null,"address":null,"grade":"6.8","skill_grade":"4.0","type":2,"occupation_name":"主治医师","consultant_name":"VIP咨询师","is_frozen":false,"is_vip":true,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     * counselling : {"id":3,"name":"阿姐","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"17610255281","telephone":null,"address":null,"grade":"7.1","skill_grade":"7.2","type":3,"occupation_name":"主治医师","consultant_name":"VIP咨询师","is_frozen":false,"is_vip":true,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     */

    private String user_id;
    private String doctor_id;
    private String counselling_id;
    private String content;
    private String c_content;
    private String evalu_create_time;
    private LookUserNumBean user;
    private LookUserNumBean doctor;
    private LookUserNumBean counselling;
    private List<String> image;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCounselling_id() {
        return counselling_id;
    }

    public void setCounselling_id(String counselling_id) {
        this.counselling_id = counselling_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public String getEvalu_create_time() {
        return evalu_create_time;
    }

    public void setEvalu_create_time(String evalu_create_time) {
        this.evalu_create_time = evalu_create_time;
    }

    public LookUserNumBean getUser() {
        return user;
    }

    public void setUser(LookUserNumBean user) {
        this.user = user;
    }

    public LookUserNumBean getDoctor() {
        return doctor;
    }

    public void setDoctor(LookUserNumBean doctor) {
        this.doctor = doctor;
    }

    public LookUserNumBean getCounselling() {
        return counselling;
    }

    public void setCounselling(LookUserNumBean counselling) {
        this.counselling = counselling;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
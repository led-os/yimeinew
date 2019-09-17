package com.chengzi.app.ui.mine.bean;

public class LookHospitalOrderBean {
    /**
     * order_id : 128
     * user : {"id":1,"name":"阿布","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=1.jpg","mobile":"15801629092","telephone":null,"address":null,"grade":"5.2","skill_grade":"10.0","type":1,"occupation_name":"","consultant_name":"VIP咨询师","is_frozen":true,"is_vip":true,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     * doctor : {"id":7,"name":"笑笑二号","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"13269961791","telephone":null,"address":"北京市朝阳区建国路181号","grade":"5.8","skill_grade":"5.7","type":2,"occupation_name":"主治医师","consultant_name":"普通咨询师","is_frozen":false,"is_vip":false,"is_auth":false,"is_auth_push":true,"is_auth_dis":false}
     * counselling : {"id":17,"name":"咨询师二号","headimg":"https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg","mobile":"17610255285","telephone":null,"address":null,"grade":"3.0","skill_grade":"2.0","type":3,"occupation_name":"","consultant_name":"普通咨询师","is_frozen":false,"is_vip":false,"is_auth":true,"is_auth_push":false,"is_auth_dis":false}
     * create_time : 2019/05/09 13:40
     */

    private String order_id;
    private LookUserNumBean user;
    private LookUserNumBean doctor;
    private LookUserNumBean counselling;
    private String create_time;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

}

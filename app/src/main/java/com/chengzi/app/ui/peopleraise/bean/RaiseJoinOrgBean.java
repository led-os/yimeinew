package com.chengzi.app.ui.peopleraise.bean;

public class RaiseJoinOrgBean {


    private String plan_id;
    private String hospital_id;   //    #机构id
    private String user_name;    //    #机构名称
    private String logo;
    private String order_num;
    private String grade;   //    #机构评分
    private int is_VIP;     //   #是否是vip   0否  1 是
    private int is_selected;   //  #是否被选择   0 否  1 是

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

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }
}

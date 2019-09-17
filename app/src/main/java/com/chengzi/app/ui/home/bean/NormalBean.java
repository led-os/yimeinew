package com.chengzi.app.ui.home.bean;

import java.util.List;

public class NormalBean {

    private List<GoodBean> goodsList;
    private List<DoctorBean> doctorList;
    private List<DoctorBean> comList;
    private List<GoodBean> hostGoodsList;
    private List<HospitalBean> hospitalList;

    public List<GoodBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<DoctorBean> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<DoctorBean> doctorList) {
        this.doctorList = doctorList;
    }

    public List<DoctorBean> getComList() {
        return comList;
    }

    public void setComList(List<DoctorBean> comList) {
        this.comList = comList;
    }

    public List<GoodBean> getHostGoodsList() {
        return hostGoodsList;
    }

    public void setHostGoodsList(List<GoodBean> hostGoodsList) {
        this.hostGoodsList = hostGoodsList;
    }

    public List<HospitalBean> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<HospitalBean> hospitalList) {
        this.hospitalList = hospitalList;
    }
}

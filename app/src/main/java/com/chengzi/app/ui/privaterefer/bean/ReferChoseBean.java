package com.chengzi.app.ui.privaterefer.bean;

import com.chengzi.app.ui.home.bean.DoctorBean;

import java.util.ArrayList;
import java.util.List;

public class ReferChoseBean {
    private int flag;
    private List<DoctorBean> doctor;
    private List<DoctorBean> com;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<DoctorBean> getDoctor() {
        return doctor == null ? (doctor = new ArrayList<>()) : doctor;
    }

    public void setDoctor(List<DoctorBean> doctor) {
        this.doctor = doctor;
    }

    public List<DoctorBean> getCom() {
        return com == null ? (com = new ArrayList<>()) : com;
    }

    public void setCom(List<DoctorBean> com) {
        this.com = com;
    }
}

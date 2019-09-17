package com.chengzi.app.ui.homepage.hospital.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;

public class SubDoctorsViewModel extends BaseViewModel {

    public final MutableLiveData<PageBean<DoctorBean>> bindedDoctorLive = new MutableLiveData<>();

    private String hospitalId;
    private int doctorType;     //  2 医生，3 咨询师

    public void getBindedDoctor() {
        Api.getApiService(HospitalHomeService.class)
                .bindedDoctor(hospitalId, String.valueOf(doctorType), "3", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<DoctorBean>> pageBeanResponseBean) {
                        bindedDoctorLive.postValue(pageBeanResponseBean.getData());
                    }
                });
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(int doctorType) {
        this.doctorType = doctorType;
    }
}

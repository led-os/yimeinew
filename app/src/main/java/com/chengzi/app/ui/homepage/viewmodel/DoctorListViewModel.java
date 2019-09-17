package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;

import java.util.List;

public class DoctorListViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<DoctorBean>> liveData = new MutableLiveData<>();

    private int userType;
    private String hospitalId;

    private final HospitalHomeService hospitalHomeService;

    public DoctorListViewModel() {
        hospitalHomeService = Api.getApiService(HospitalHomeService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        hospitalHomeService.bindedDoctor(hospitalId,String.valueOf(userType),
                String.valueOf(pageSize),String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<DoctorBean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}

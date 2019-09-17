package com.chengzi.app.ui.homepage.doctor.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.util.List;

public class RecommandOthersViewModel extends BaseViewModel {

    private static final String[] USER_TYPE = {"doctor", "consultant"};

    public final MutableLiveData<List<DoctorBean>> recommendOthersLive = new MutableLiveData<>();

    private int userType;    //  2 医生   3 咨询师

    public void recommendUsers() {
        Api.getApiService(DoctorHomeService.class)
                .recommendUsers(USER_TYPE[userType - 2])
                .subscribe(new SimpleObserver<ResponseBean<List<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<DoctorBean>> listResponseBean) {
                        recommendOthersLive.postValue(listResponseBean.getData());
                    }
                });
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

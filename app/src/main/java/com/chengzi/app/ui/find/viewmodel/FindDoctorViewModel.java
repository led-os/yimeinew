package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.find.model.FindService;
import com.chengzi.app.ui.home.bean.DoctorBean;

import java.util.List;

public class FindDoctorViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<FindBaseBean<DoctorBean>>> doctorLive = new MutableLiveData<>();


    private final FindService findService;
    private int type;  //  0 医生 ，1 咨询师

    public FindDoctorViewModel() {
        findService = Api.getApiService(FindService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String params = type == 1?"consultant":"doctor";
        findService.findDoctor(params)
                .subscribe(new SimpleObserver<ResponseBean<List<FindBaseBean<DoctorBean>>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<FindBaseBean<DoctorBean>>> findBaseBeanResponseBean) {
                        doctorLive.postValue(findBaseBeanResponseBean.getData());
                    }
                });
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

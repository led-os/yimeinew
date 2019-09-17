package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.find.model.FindService;
import com.chengzi.app.ui.home.bean.HospitalBean;

import java.util.List;

public class FindOrgViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<FindBaseBean<HospitalBean>>> orgLive = new MutableLiveData<>();

    private final FindService findService;

    public FindOrgViewModel() {
        findService = Api.getApiService(FindService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        findService.findHospital("service_organization")
                .subscribe(new SimpleObserver<ResponseBean<List<FindBaseBean<HospitalBean>>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<FindBaseBean<HospitalBean>>> findBaseBeanResponseBean) {
                        orgLive.postValue(findBaseBeanResponseBean.getData());
                    }
                });
    }
}

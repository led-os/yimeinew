package com.chengzi.app.ui.peopleraise.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.bean.SubmitOrderBean;
import com.chengzi.app.ui.peopleraise.model.RaiseService;

public class ConfirmRaiseOrderViewModel extends BaseViewModel {

    public final MutableLiveData<RaiseBean> raiseLive = new MutableLiveData<>();
    public final MutableLiveData<SubmitOrderBean> submitOrderLive = new MutableLiveData<>();


    private String planId;
    private final RaiseService raiseService;

    public ConfirmRaiseOrderViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
    }

    public void planOrder(){
        String token = AccountHelper.getToken();
        String userId = AccountHelper.getUserId();
        raiseService.planOrder(token,userId,planId)
                .subscribe(new SimpleObserver<ResponseBean<RaiseBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<RaiseBean> raiseBeanResponseBean) {
                        raiseLive.postValue(raiseBeanResponseBean.getData());
                    }
                });
    }

    public void planSubmitOrder(){
        String token = AccountHelper.getToken();
        String userId = AccountHelper.getUserId();
        raiseService.planSubmitOrder(token,userId,planId)
                .subscribe(new SimpleObserver<ResponseBean<SubmitOrderBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<SubmitOrderBean> raiseBeanResponseBean) {
                        submitOrderLive.postValue(raiseBeanResponseBean.getData());
                    }
                });
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}

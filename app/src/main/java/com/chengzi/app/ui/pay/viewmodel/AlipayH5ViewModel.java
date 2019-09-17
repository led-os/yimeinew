package com.chengzi.app.ui.pay.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.pay.model.PayService;

public class AlipayH5ViewModel extends BaseViewModel {

    public final MutableLiveData<Integer> payStatusLive = new MutableLiveData<>();

    private String prepaylogId;
    private boolean startAlipay;
    private boolean payStart;
    private long startTime;

    public void getPayStatus(){
        Api.getApiService(PayService.class)
                .getPayStatus(prepaylogId)
                .subscribe(new SimpleObserver<ResponseBean<Integer>>() {
                    @Override
                    public void onSuccess(ResponseBean<Integer> integerResponseBean) {
                        payStatusLive.postValue(integerResponseBean.getData());
                    }
                });
    }

    public boolean isPayStart() {
        return payStart;
    }

    public void setPayStart(boolean payStart) {
        this.payStart = payStart;
    }

    public String getPrepaylogId() {
        return prepaylogId;
    }

    public void setPrepaylogId(String prepaylogId) {
        this.prepaylogId = prepaylogId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isStartAlipay() {
        return startAlipay;
    }

    public void setStartAlipay(boolean startAlipay) {
        this.startAlipay = startAlipay;
    }
}

package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.message.bean.AttentionDoctorBean;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.List;

public class DoctorSelectorViewModel extends BaseViewModel {

    public final MutableLiveData<List<AttentionDoctorBean>> attentionDoctorLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> caseForwardLive = new MutableLiveData<>();

    private String caseUserId;  //  案例所属用户id

    public void getAttentionDoctor() {
        Api.getApiService(MessageService.class)
                .getAttention(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<List<AttentionDoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<AttentionDoctorBean>> listResponseBean) {
                        attentionDoctorLive.postValue(listResponseBean.getData());
                    }
                });
    }

    public void caseForward(String toUserId) {
        Api.getApiService(MessageService.class)
                .caseForward(AccountHelper.getToken(), AccountHelper.getUserId(), caseUserId, toUserId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseForwardLive.postValue(true);
                    }
                });
    }

    public String getCaseUserId() {
        return caseUserId;
    }

    public void setCaseUserId(String caseUserId) {
        this.caseUserId = caseUserId;
    }
}

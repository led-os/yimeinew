package com.chengzi.app.ui.privaterefer.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.model.MineService;

public class PrivateReferViewModel extends BaseViewModel {

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();


    //用户信息
    public void userInfo() {
        Api.getApiService(MineService.class).userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                userInfoLiveData.postValue(userInfoBeanResponseBean.getData());
            }
        });
    }
}

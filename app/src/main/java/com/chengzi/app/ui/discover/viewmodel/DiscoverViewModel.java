package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.model.MineService;

public class DiscoverViewModel extends BaseViewModel {

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();

    private int authStatus = -1;

    //用户信息
    public void userInfo() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        Api.getApiService(MineService.class).userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                userInfoLiveData.postValue(userInfoBeanResponseBean.getData());
            }
        });
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}

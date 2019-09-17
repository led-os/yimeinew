package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.message.bean.MessageUnreadCountBean;
import com.chengzi.app.ui.message.model.MessageService;
import com.chengzi.app.ui.mine.model.MineService;

public class MainViewModel extends BaseViewModel {

    public final ObservableInt unreadCount = new ObservableInt();
    public final ObservableInt imUnreadCount = new ObservableInt();

    public final MutableLiveData<MessageUnreadCountBean> unreadMessageCountLive = new MutableLiveData<>();
    public final MutableLiveData<Integer> switchTabLive = new MutableLiveData<>();

    public void getMessageUnreadCount(){
        String userId = AccountHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            unreadMessageCountLive.postValue(new MessageUnreadCountBean());
            return;
        }
        Api.getApiService(MessageService.class)
                .notificationCount(userId)
                .subscribe(new SimpleObserver<ResponseBean<MessageUnreadCountBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<MessageUnreadCountBean> responseBean) {
                        unreadMessageCountLive.postValue(responseBean.getData());
                    }
                });
    }

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();

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
}

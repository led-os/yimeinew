package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.chengzi.app.ui.message.bean.MessageUnreadCountBean;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.ArrayList;
import java.util.List;

public class MessageViewModel extends BaseViewModel {

    public final ObservableInt systemUnreadCount = new ObservableInt();
    public final ObservableInt remindUnreadCount = new ObservableInt();
    public final ObservableInt likeAndCommentUnreadCount = new ObservableInt();
    public final ObservableInt fansUnreadCount = new ObservableInt();
    public final ObservableInt questionUnreadCount = new ObservableInt();
    public final ObservableInt userUnreadCount = new ObservableInt();
    public final ObservableInt doctorUnreadCount = new ObservableInt();
    public final ObservableInt counselorUnreadCount = new ObservableInt();
    public final ObservableInt serviceUnreadCount = new ObservableInt();


    public final MutableLiveData<MessageUnreadCountBean> unreadMessageCountLive = new MutableLiveData<>();
    public final MutableLiveData<List<RecentContact>> imUnreadMessageCountLive = new MutableLiveData<>();

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

    public void getImMessageUnreadCount(){
        String userId = AccountHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            imUnreadMessageCountLive.postValue(new ArrayList<>());
            return;
        }
        NIMClient.getService(MsgService.class)
                .queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> result, Throwable exception) {
                        imUnreadMessageCountLive.postValue(result);
                    }
                });
    }

}

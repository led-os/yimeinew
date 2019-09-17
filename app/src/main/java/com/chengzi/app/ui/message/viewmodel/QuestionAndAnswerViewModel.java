package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.message.bean.QuestionMsgBean;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.List;

public class QuestionAndAnswerViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<QuestionMsgBean>> liveData = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = AccountHelper.getUserId();
        Api.getApiService(MessageService.class)
                .questionOnlineNotify(userId,
                        String.valueOf(pageSize),
                        String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<QuestionMsgBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<QuestionMsgBean>> responseBean) {
                        liveData.postValue(responseBean.getData().getData());
                    }
                });
    }
}

package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.message.bean.FansMsgBean;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.List;

public class FansViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<FansMsgBean>> liveData = new MutableLiveData<>();

    private final MessageService messageService;

    public FansViewModel() {
        messageService = Api.getApiService(MessageService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = AccountHelper.getUserId();
        messageService.getFansMsg(userId, String.valueOf(pageSize)
                , String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<FansMsgBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<FansMsgBean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });

    }
}

package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;

import java.util.List;

public class ResolvingQuestionViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<QAbean>> liveData = new MutableLiveData<>();

    private final OnlineQAService onlineQAService;

    private String status; //  1 已解决  2 未解决

    public ResolvingQuestionViewModel() {
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        onlineQAService.questionStatus(status, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<QAbean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<QAbean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

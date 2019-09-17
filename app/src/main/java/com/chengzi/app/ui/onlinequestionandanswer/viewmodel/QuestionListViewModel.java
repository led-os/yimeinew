package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;

import java.util.List;

public class QuestionListViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<QAbean>> liveData = new MutableLiveData<>();

    private String status;
    private String keyword;

    private final OnlineQAService onlineQAService;

    public QuestionListViewModel() {
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {

        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
                .put("status", TextUtils.equals("0", status) ? null : status)
                .put("key", keyword)
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage));

        onlineQAService.questionSearch(params.params())
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

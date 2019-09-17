package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.bean.UserQuestionsBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

public class UserAskViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<UserQuestionsBean>> userQuestionsLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<UserQuestionsBean>> questionLive = new MutableLiveData<>();
    private final MineService mineService;

    public UserAskViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    private String targetUserId;

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.userQuestions(AccountHelper.getToken(), AccountHelper.getUserId(), targetUserId,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<UserQuestionsBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<UserQuestionsBean>> responseBean) {
                        userQuestionsLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        userQuestionsLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    public void getQuestion(String currentPage) {
        mineService.userQuestions(AccountHelper.getToken(), AccountHelper.getUserId(), targetUserId, currentPage, "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<UserQuestionsBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<UserQuestionsBean>> pageBeanResponseBean) {
                        questionLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }
}

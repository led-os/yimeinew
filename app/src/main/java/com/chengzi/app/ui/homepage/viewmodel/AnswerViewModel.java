package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.model.MineService;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;

import java.util.ArrayList;
import java.util.List;

public class AnswerViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<QAbean>> userAnswersLiveData = new MutableLiveData<>();
    private final MineService mineService;

    public AnswerViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    public ObservableField<String> click_id = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {  //status 问题状态 传0表示所有 1-已解决 2-未解决
        mineService.allQuestionAnswerList(AccountHelper.getUserId(), click_id.get(), "0",
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<QAbean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<QAbean>> responseBean) {
                        userAnswersLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        userAnswersLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}

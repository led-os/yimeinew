package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.OnlineQAbean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;

import java.util.List;

public class QuestionSearchViewModel extends BaseViewModel {

    private static final String HISTORY_SEARCH_TAG = "question_history_search_tag";

    public final MutableLiveData<OnlineQAbean> onlineQAlive = new MutableLiveData<>();

    public final ObservableField<String> keyword = new ObservableField<>();
    private List<String> history;

    private final OnlineQAService onlineQAService;

    public QuestionSearchViewModel() {
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    public void searchKeywords() {
        onlineQAService.questionSearchKey(TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<OnlineQAbean>>() {
                    @Override
                    public void onSuccess(ResponseBean<OnlineQAbean> onlineQAbeanResponseBean) {
                        onlineQAlive.postValue(onlineQAbeanResponseBean.getData());
                    }
                });
    }

}

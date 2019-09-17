package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;
import com.chengzi.app.utils.URLUtils;

import java.util.List;

public class AnswerQuestionViewModel extends BaseViewModel {

    public final ObservableField<String> answer = new ObservableField<>();
    public final ObservableInt answerLength = new ObservableInt();

    public final MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    private final CommonModel model = new CommonModel();
    public final MutableLiveData<List<CategoryItem>> categoryLive1 = new MutableLiveData<>();

    private String questionId;

    private final OnlineQAService onlineQAService;

    public AnswerQuestionViewModel() {
        answer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String text = answer.get();
                answerLength.set(TextUtils.isEmpty(text) ? 0 : text.length());
            }
        });
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    public void answerQuestion() {
        String userId = AccountHelper.getUserId();
        onlineQAService.answerQuestion(userId, questionId, URLUtils.encode(answer.get()))
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        liveData.postValue(true);
                    }
                });
    }


    public void categoryList1() {
        model.categoryList1()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> navigatorListBeanResponseBean) {
                        categoryLive1.postValue(navigatorListBeanResponseBean.getData());
                    }
                });
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}

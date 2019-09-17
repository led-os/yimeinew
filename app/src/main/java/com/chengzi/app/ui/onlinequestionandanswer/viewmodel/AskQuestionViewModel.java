package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;
import com.chengzi.app.utils.URLUtils;

public class AskQuestionViewModel extends BaseViewModel {

    public final ObservableField<String> questionDesc = new ObservableField<>();  //  问题描述
    public final ObservableField<String> descSupplement = new ObservableField<>();  //  描述补充
    public final ObservableInt descLength = new ObservableInt(0);
    public final ObservableInt supplementLength = new ObservableInt(0);

    public final MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    private final OnlineQAService onlineQAService;

    private String categoryId;
    public final ObservableField<String> categoryName = new ObservableField<>();

    public AskQuestionViewModel() {
        questionDesc.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String text = questionDesc.get();
                descLength.set(TextUtils.isEmpty(text) ? 0 : text.length());
            }
        });

        descSupplement.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String text = descSupplement.get();
                supplementLength.set(TextUtils.isEmpty(text) ? 0 : text.length());
            }
        });

        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    public void addQuestions() {

        String userId = AccountHelper.getUserId();
        Params params = Params.newParams()
                .put("user_id", userId)
                .put("title", URLUtils.encode(questionDesc.get()))
                .put("type", categoryId);
        if (!TextUtils.isEmpty(descSupplement.get())) {
            params.put("content", URLUtils.encode(descSupplement.get()));
        }

        onlineQAService.addQuestion(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        liveData.postValue(true);
                    }
                });
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}

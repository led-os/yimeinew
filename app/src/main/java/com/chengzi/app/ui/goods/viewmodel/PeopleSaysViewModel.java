package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;
import com.chengzi.app.ui.goods.model.PeopleSayService;

public class PeopleSaysViewModel extends BaseViewModel implements IRequest {

    private static final String[] TARGET_TYPE = {"goods", "doctor", "consultant", "serviceOrganization"};

    public final MutableLiveData<PageBean<PeopleSayQuestionBean>> peopleSayQuestionLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> publishAskLive = new MutableLiveData<>();

    public final ObservableField<String> questionContent = new ObservableField<>();

    private String targetId;    //      目标id
    private int targetType;     //      1-goods|2-doctor|3-consultant|4-serviceOrganization

    private final PeopleSayService peopleSayService;

    public PeopleSaysViewModel() {
        peopleSayService = Api.getApiService(PeopleSayService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        peopleSayService.askOthersQuestionList(TARGET_TYPE[targetType - 1], targetId,
                String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<PeopleSayQuestionBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<PeopleSayQuestionBean>> pageBeanResponseBean) {
                        peopleSayQuestionLive.postValue(pageBeanResponseBean.getData());
                    }
                });
    }

    public void submitQuestion() {
        String userId = AccountHelper.getUserId();
        peopleSayService.askOthersPushlished(userId, targetId, TARGET_TYPE[targetType - 1],
                questionContent.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        publishAskLive.postValue(true);
                    }
                });

        questionContent.set("");
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }
}

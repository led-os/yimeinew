package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.goods.bean.PeopleSayAnswerBean;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;
import com.chengzi.app.ui.goods.model.PeopleSayService;

import java.util.List;

public class PeopleSayDetailViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<PeopleSayAnswerBean>> liveData = new MutableLiveData<>();
    public final MutableLiveData<Boolean> answerLive = new MutableLiveData<>();
    public final MutableLiveData<PeopleSayQuestionBean> askOthersDetailLive = new MutableLiveData<>();

    public final ObservableField<String> answerContent = new ObservableField<>();

    private final PeopleSayService peopleSayService;

    private String questionId;
    private String targetId;    //      目标id
    private int targetType;     //      1-goods|2-doctor|3-consultant|4-serviceOrganization

    public PeopleSayDetailViewModel() {
        peopleSayService = Api.getApiService(PeopleSayService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        peopleSayService.askOthersAnswerList(questionId, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<PeopleSayAnswerBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<PeopleSayAnswerBean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public void getQuestionDetail(){
        peopleSayService.askOthersQuestionDetail(questionId)
                .subscribe(new SimpleObserver<ResponseBean<PeopleSayQuestionBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PeopleSayQuestionBean> peopleSayQuestionBeanResponseBean) {
                        askOthersDetailLive.postValue(peopleSayQuestionBeanResponseBean.getData());
                    }
                });
    }

    public void answerOthers() {
        String userId = AccountHelper.getUserId();
        peopleSayService.answerOthers(userId, questionId, answerContent.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        answerLive.postValue(true);
                    }
                });
        answerContent.set("");
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

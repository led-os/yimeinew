package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.onlinequestionandanswer.bean.AnswerBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QuestionDetailBean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;

public class QuestionDetailViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<QuestionDetailBean> liveData = new MutableLiveData<>();
    public final MutableLiveData<AnswerBean> acceptAnswerLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> deleteQuestionLive = new MutableLiveData<>();

    private final OnlineQAService onlineQAService;
    private String questionId;
    private boolean fromSelf;   //  true,从普通用户主页，提问进入
    private boolean resolved;   //  true 问题已解决，false，未解决
    private boolean updated;    //  true,数据发生变化，有新回答

    public QuestionDetailViewModel() {
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        onlineQAService.questionDetail(questionId)
                .subscribe(new SimpleObserver<ResponseBean<QuestionDetailBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<QuestionDetailBean> questionDetailBeanResponseBean) {
                        liveData.postValue(questionDetailBeanResponseBean.getData());
                    }
                });
    }

    public void acceptAnswer(AnswerBean bean) {
        onlineQAService.acceptAnswer(questionId, bean.getId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        acceptAnswerLive.postValue(bean);
                    }
                });
    }

    public void deleteQuestion(){
        onlineQAService.deleteQuestion(questionId, AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        deleteQuestionLive.postValue(true);
                    }
                });
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public boolean isFromSelf() {
        return fromSelf;
    }

    public void setFromSelf(boolean fromSelf) {
        this.fromSelf = fromSelf;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}

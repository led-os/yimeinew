package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;
import com.chengzi.app.ui.goods.model.PeopleSayService;

import java.util.List;

public class SubPeopleSayViewModel extends BaseViewModel {

    private static final String[] TARGET_TYPE = {"goods", "doctor", "consultant", "serviceOrganization"};

    public final MutableLiveData<List<PeopleSayQuestionBean>> peopleSayQuestionLive = new MutableLiveData<>();

    private String targetId;    //      目标id
    private int targetType;     //      1-goods|2-doctor|3-consultant|4-serviceOrganization

    private final PeopleSayService peopleSayService;

    public SubPeopleSayViewModel() {
        peopleSayService = Api.getApiService(PeopleSayService.class);
    }

    public void getPeopleSayList() {
        peopleSayService.askOthersQuestionList(TARGET_TYPE[targetType - 1], targetId, "2", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<PeopleSayQuestionBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<PeopleSayQuestionBean>> pageBeanResponseBean) {
                        peopleSayQuestionLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
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

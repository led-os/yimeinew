package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.EvaluateBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.util.List;

public class SubEstimateViewModel extends BaseViewModel {

    public final MutableLiveData<List<EvaluateBean>> evaluateLive = new MutableLiveData<>();

    private static final String[] TARGET_TYPE = {"goods", "doctor", "consultant", "serviceOrganization"};

    private final String type  = "1"; //  详情页默认显示全部的评价 (1 全部评价| 2 好评| 3 有图| 4 效果显著| 5 优质服务)
    private String targetId;    //      目标id
    private int targetType;     //      1-goods|2-doctor|3-consultant|4-serviceOrganization

    private final DoctorHomeService doctorHomeService;

    public SubEstimateViewModel() {
        doctorHomeService = Api.getApiService(DoctorHomeService.class);
    }

    public void evaluateList() {
        doctorHomeService.evaluateList(type, targetId, TARGET_TYPE[targetType - 1], "2", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<EvaluateBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<EvaluateBean>> pageBeanResponseBean) {
                        evaluateLive.postValue(pageBeanResponseBean.getData().getData());
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

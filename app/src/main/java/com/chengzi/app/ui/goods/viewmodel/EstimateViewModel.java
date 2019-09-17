package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.bean.EvaluateBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.util.List;

public class EstimateViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<EvaluateBean>> liveData = new MutableLiveData<>();

    private static final String[] TARGET_TYPE = {"goods", "doctor", "consultant", "serviceOrganization"};

    private String type = "1"; //  详情页默认显示全部的评价 (1 全部评价| 2 好评| 3 有图| 4 效果显著| 5 优质服务)
    private String targetId;    //      目标id
    private int targetType;     //      1-goods|2-doctor|3-consultant|4-serviceOrganization

    private final DoctorHomeService doctorHomeService;

    public EstimateViewModel() {
        doctorHomeService = Api.getApiService(DoctorHomeService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        doctorHomeService.evaluateList(type, targetId, TARGET_TYPE[targetType - 1],
                String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<EvaluateBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<EvaluateBean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

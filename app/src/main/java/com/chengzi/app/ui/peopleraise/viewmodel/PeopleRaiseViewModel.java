package com.chengzi.app.ui.peopleraise.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.model.RaiseService;

import java.util.List;

public class PeopleRaiseViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<RaiseBean>> liveData = new MutableLiveData<>();

    private String keyword;
    private String categoryId;
    private String status;

    private final RaiseService raiseService;

    public PeopleRaiseViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("category_id", categoryId)   //  美人筹类型（必）
                .put("type", status)      //  订单状态 1 进行中 2-已成功 4-未达成（非必传，默认1）
                .put("limit", String.valueOf(pageSize))    //  分页（非）
                .put("page", String.valueOf(currentPage))
                .put("search", TextUtils.isEmpty(keyword) ? null : keyword);//  搜索内容（如果是美人筹页面搜索 必须带此参数）【复用】

        raiseService.planList(params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<RaiseBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<RaiseBean>> pageBeanResponseBean) {
                        liveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

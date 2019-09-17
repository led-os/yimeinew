package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.find.model.FindService;
import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.List;

public class FindGoodsViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<FindBaseBean<GoodBean>>> goodsLive = new MutableLiveData<>();

    private final FindService findService;

    public FindGoodsViewModel() {
        findService = Api.getApiService(FindService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {

        findService.findGoods("goods")
                .subscribe(new SimpleObserver<ResponseBean<List<FindBaseBean<GoodBean>>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<FindBaseBean<GoodBean>>> listResponseBean) {
                        goodsLive.postValue(listResponseBean.getData());
                    }
                });

    }
}

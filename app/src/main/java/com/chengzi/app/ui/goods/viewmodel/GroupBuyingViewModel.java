package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.model.GoodService;

import java.util.List;

public class GroupBuyingViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<SpellBean>> liveData = new MutableLiveData<>();

    private GoodDetailBean goodDetailBean;
    private String goodsId;

    private final GoodService goodService;

    public GroupBuyingViewModel() {
        goodService = Api.getApiService(GoodService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        goodService.goodsSpellList(goodsId, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<SpellBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<SpellBean>> responseBean) {
                        liveData.postValue(responseBean.getData().getData());
                    }
                });
    }

    public GoodDetailBean getGoodDetailBean() {
        return goodDetailBean;
    }

    public void setGoodDetailBean(GoodDetailBean goodDetailBean) {
        this.goodDetailBean = goodDetailBean;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}

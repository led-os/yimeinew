package com.chengzi.app.ui.rankinglist.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Params;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class GoodsRankListViewModel extends RankingListBaseViewModel implements IRequest {

    public final MutableLiveData<List<GoodBean>> goodsListLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("category_id", getCategoryId())
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("page_size", "10")
                .put("page", "1");
        rankingService.rankingGoods("1", params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<GoodBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<GoodBean>> pageBeanResponseBean) {
                        goodsListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

}

package com.chengzi.app.ui.rankinglist.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Params;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.rankinglist.bean.SearchRankingBean;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class SearchRankingListViewModel extends RankingListBaseViewModel implements IRequest {

    public final MutableLiveData<List<SearchRankingBean>> searchLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("category_id", null)   //  搜索不需要此参数)
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("page_size", "10")
                .put("page", "1");
        rankingService.rankingSearch("4", params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<SearchRankingBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<SearchRankingBean>> pageBeanResponseBean) {
                        searchLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }
}

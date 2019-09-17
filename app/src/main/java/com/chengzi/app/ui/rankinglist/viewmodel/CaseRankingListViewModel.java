package com.chengzi.app.ui.rankinglist.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Params;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class CaseRankingListViewModel extends RankingListBaseViewModel implements IRequest {

    public final MutableLiveData<List<CaseBean>> casesListLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("category_id", getCategoryId())
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("page_size", "10")
                .put("page", "1")
                .put("user_id",TextUtils.isEmpty(AccountHelper.getUserId())?"":AccountHelper.getUserId())
                .put("date_format", "timestamp");
        rankingService.rankingCase("5", params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseBean>> pageBeanResponseBean) {
                        casesListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

}

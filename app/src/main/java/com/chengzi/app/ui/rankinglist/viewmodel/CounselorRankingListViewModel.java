package com.chengzi.app.ui.rankinglist.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Params;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class CounselorRankingListViewModel extends RankingListBaseViewModel implements IRequest {

    public final MutableLiveData<List<DoctorBean>> doctorsListLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("category_id", getCategoryId())
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("page_size", "10")
                .put("page", "1");
        rankingService.rankingDoctor("3", params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<DoctorBean>> pageBeanResponseBean) {
                        doctorsListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }


}

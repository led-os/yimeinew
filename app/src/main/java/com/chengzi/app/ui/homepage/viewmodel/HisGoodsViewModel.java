package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.homepage.bean.RelationCategoryBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.util.List;

public class HisGoodsViewModel extends BaseViewModel {

    public final MutableLiveData<List<RelationCategoryBean>> relationCategoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<GoodBean>> relativeGoodsLive = new MutableLiveData<>();

    private String userId;
    private int userType;   //  2 医生 ,3 咨询师 ，4 机构

    private final DoctorHomeService doctorHomeService;

    public HisGoodsViewModel() {
        doctorHomeService = Api.getApiService(DoctorHomeService.class);
    }

    public void relationGoodsCategorys() {
        doctorHomeService.relationGoodsCategorys(userId)
                .subscribe(new SimpleObserver<ResponseBean<List<RelationCategoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<RelationCategoryBean>> listResponseBean) {
                        relationCategoryLive.postValue(listResponseBean.getData());
                    }
                });
    }

    public void relationGoods(String categoryId) {
        doctorHomeService.relationGoods(userId, categoryId, String.valueOf(Integer.MAX_VALUE), "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<GoodBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<GoodBean>> pageBeanResponseBean) {
                        relativeGoodsLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

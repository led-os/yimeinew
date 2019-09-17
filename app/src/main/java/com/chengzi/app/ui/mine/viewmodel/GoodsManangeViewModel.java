package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.mine.bean.GoodsListManageBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.model.SearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsManangeViewModel extends BaseViewModel implements IRequest {

    //商品列表 设置/取消特色 上/下架
    public final MutableLiveData<List<GoodsListManageBean>> goodsListLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> goodsChangeFeatureLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> goodsChangeStatusLive = new MutableLiveData<>();
    //商品分类
    public final MutableLiveData<List<CategoryItem>> goodsCategoryLiveData = new MutableLiveData<>();

    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);
    public final ObservableInt selectedTabPos = new ObservableInt(-1);

    public String status, category_id;
    private final MineHospitalService mineService;

    public GoodsManangeViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    @Override   //列表
    public void onRequest(int currentPage, int pageSize) {
        HashMap<String, String> map = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("uid", AccountHelper.getUserId())
                .put("type", String.valueOf(AccountHelper.getIdentity()))
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage))
                .put("status", status)  //商品状态 1-上架 2-下架 3-特色（筛选用）
                .put("category_id", category_id)  //商品一级分类id（筛选用）
//                .put("category_id2", category_id2)  //商品二级分类id（筛选用）
//                .put("category_id3", category_id3)  //商品三级分类id（筛选用）
                .params();
        mineService.goodsListGuanLi(map)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<GoodsListManageBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<GoodsListManageBean>> bean) {
                        goodsListLive.postValue(bean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        goodsListLive.postValue(new ArrayList<>());
                    }
                });
    }

    //设置/取消特色
    public void goodsChangeFeature(String goods_id) {
        mineService.goodsChangeFeature(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId()
                , goods_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        goodsChangeFeatureLive.postValue(bean);
                    }
                });
    }

    //上/下架
    public void goodsChangeStatus(String goods_id) {
        mineService.goodsChangeStatus(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId()
                , goods_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        goodsChangeStatusLive.postValue(bean);
                    }
                });
    }

    //  商品分类
    public void goodsCategory() {
        Api.getApiService(SearchService.class)
                .goodsCatrgory().subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
            @Override
            public void onSuccess(ResponseBean<List<CategoryItem>> responseBean) {

                List<CategoryItem> categoryItems = responseBean.getData();
                CategoryItem categoryItem = new CategoryItem();
                categoryItem.setName("全部商品");
                categoryItems.add(0, categoryItem);

                goodsCategoryLiveData.postValue(responseBean.getData());
            }
        });
    }
}
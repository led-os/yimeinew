package com.chengzi.app.ui.search.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.model.SearchService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class SearchGoodsViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<GoodBean>> goodsListLive = new MutableLiveData<>();
    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);
    public final ObservableInt selectedTabPos = new ObservableInt(-1);


    private String keyword;  //  搜索关键字
    private DropDownPopup.PopupBean sortParam;  //  排序item
    private DropDownPopup.PopupBean orgParam;   //  选择机构item
    private String categoryId;   //  一级分类id
    private String categoryId2;  //  二级分类id
    private String categoryId3;  //  三级分类id

    private final SearchService searchService;

    public SearchGoodsViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
//                .put("key", keyword)
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage))
                .put("category_id", categoryId)
                .put("category_id2", categoryId2)
                .put("category_id3", categoryId3)
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE));
        if (!TextUtils.isEmpty(keyword)) {
            params.put("key", keyword);
        }
        if (sortParam != null) {
            params.put(sortParam.getParam(), sortParam.getValue());
        }
        if (orgParam != null) {
            params.put(orgParam.getParam(), orgParam.getValue());
        }
        searchService.searchGoods(params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<GoodBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<GoodBean>> pageBeanResponseBean) {
                        goodsListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public DropDownPopup.PopupBean getSortParam() {
        return sortParam;
    }

    public void setSortParam(DropDownPopup.PopupBean sortParam) {
        this.sortParam = sortParam;
    }

    public DropDownPopup.PopupBean getOrgParam() {
        return orgParam;
    }

    public void setOrgParam(DropDownPopup.PopupBean orgParam) {
        this.orgParam = orgParam;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(String categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(String categoryId3) {
        this.categoryId3 = categoryId3;
    }

}

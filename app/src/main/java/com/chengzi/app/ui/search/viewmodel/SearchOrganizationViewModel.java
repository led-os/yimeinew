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
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.model.SearchService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class SearchOrganizationViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<HospitalBean>> orgLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);
    public final ObservableInt selectedTabPos = new ObservableInt(-1);

    private final SearchService searchService;
    private final CommonModel commonModel = new CommonModel();

    private String keyword;
    private String categoryId;
    private DropDownPopup.PopupBean sortItem;  //  智能排序
    private DropDownPopup.PopupBean orgItem;   //  机构排序

    public SearchOrganizationViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {

        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage))
//                .put("key", keyword)
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("begoodat", categoryId);
        if (!TextUtils.isEmpty(keyword)) {
            params.put("key", keyword);
        }
        if (sortItem != null) {
            params.put(sortItem.getParam(), sortItem.getValue());
        }

        if (orgItem != null) {
            params.put(orgItem.getParam(), orgItem.getValue());
        }

        searchService.searchOrg(params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<HospitalBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<HospitalBean>> pageBeanResponseBean) {
                        orgLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public void categoryList() {
        commonModel.categoryList1()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> navigatorListBeanResponseBean) {
//                        List<CategoryItem> categoryItems = navigatorListBeanResponseBean.getData().getPut();
                        List<CategoryItem> categoryItems = navigatorListBeanResponseBean.getData();
                        CategoryItem categoryItem = new CategoryItem();
                        categoryItem.setName("全部");
                        categoryItems.add(0, categoryItem);
                        categoryLive.postValue(categoryItems);
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

    public DropDownPopup.PopupBean getSortItem() {
        return sortItem;
    }

    public void setSortItem(DropDownPopup.PopupBean sortItem) {
        this.sortItem = sortItem;
    }

    public DropDownPopup.PopupBean getOrgItem() {
        return orgItem;
    }

    public void setOrgItem(DropDownPopup.PopupBean orgItem) {
        this.orgItem = orgItem;
    }
}

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
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.model.SearchService;

import java.util.List;

public class SearchCounselorViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<DoctorBean>> counselorLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);
    public final ObservableInt selectedTabPos = new ObservableInt(-1);

    private final CommonModel commonModel = new CommonModel();
    private final SearchService searchService;

    private String keyword;   //  搜索关键字
    private String categoryId;  //  咨询师擅长商品id
    private DropDownPopup.PopupBean titleType;   //  职称分类
    private DropDownPopup.PopupBean orgType;   //  咨询师 所属的机构类型

    public SearchCounselorViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage))
//                .put("key", keyword)
                .put("category_id", categoryId);
        if (!TextUtils.isEmpty(keyword)) {
            params.put("key", keyword);
        }
        if (titleType != null) {
            params.put(titleType.getParam(), titleType.getValue());
        }
        if (orgType != null) {
            params.put(orgType.getParam(), orgType.getValue());
        }

        searchService.searchConsultant((params.params()))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<DoctorBean>> pageBeanResponseBean) {
                        counselorLive.postValue(pageBeanResponseBean.getData().getData());
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

    public DropDownPopup.PopupBean getTitleType() {
        return titleType;
    }

    public void setTitleType(DropDownPopup.PopupBean titleType) {
        this.titleType = titleType;
    }

    public DropDownPopup.PopupBean getOrgType() {
        return orgType;
    }

    public void setOrgType(DropDownPopup.PopupBean orgType) {
        this.orgType = orgType;
    }
}

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

public class SearchDoctorViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<DoctorBean>> doctorLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);
    public final ObservableInt selectedTabPos = new ObservableInt(-1);

    private String keyword;
    private DropDownPopup.PopupBean professionAgeItem; //  从业年限
    private DropDownPopup.PopupBean sortItem;     //  排序
    private String categoryId;    //  擅长商品的分类id

    private final SearchService searchService;
    private final CommonModel commonModel = new CommonModel();

    public SearchDoctorViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
//                .put("key", keyword)
                .put("limit", String.valueOf(pageSize))
                .put("page", String.valueOf(currentPage))
                .put("category_id", categoryId);//  擅长商品 （非必传）
        if (!TextUtils.isEmpty(keyword)) {
            params.put("key", keyword);
        }
        if (professionAgeItem != null) {
            String name = professionAgeItem.getName();
            switch (name) {
                case "不限年限":
                case "10年以上":
                    params.put(professionAgeItem.getParam(), professionAgeItem.getValue());
                    break;
                default:
                    String param = professionAgeItem.getParam();
                    String value = professionAgeItem.getValue();
                    String[] pKeys = param.split(",");
                    String[] pValues = value.split("-");
                    if (pKeys.length != pValues.length) {
                        return;
                    }
                    for (int i = 0; i < pKeys.length; i++) {
                        params.put(pKeys[i], pValues[i]);
                    }
                    break;
            }
        }

        if (sortItem != null) {
            params.put(sortItem.getParam(), sortItem.getValue());
        }

        searchService.searchDoctor(params.params())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<DoctorBean>> pageBeanResponseBean) {
                        doctorLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });

    }

    public void categoryList() {
        commonModel.categoryList1()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> categoryItemsBean) {
                        List<CategoryItem> categoryItems = categoryItemsBean.getData();
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

    public DropDownPopup.PopupBean getProfessionAgeItem() {
        return professionAgeItem;
    }

    public void setProfessionAgeItem(DropDownPopup.PopupBean professionAgeItem) {
        this.professionAgeItem = professionAgeItem;
    }

    public DropDownPopup.PopupBean getSortItem() {
        return sortItem;
    }

    public void setSortItem(DropDownPopup.PopupBean sortItem) {
        this.sortItem = sortItem;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}

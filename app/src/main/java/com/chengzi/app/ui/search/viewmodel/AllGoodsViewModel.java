package com.chengzi.app.ui.search.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.peopleraise.model.RaiseService;
import com.chengzi.app.ui.search.model.SearchService;

import java.util.List;

public class AllGoodsViewModel extends BaseViewModel {

    public final ObservableInt selectedPos = new ObservableInt();
    private CategoryItem category;  //  一级分类
    private CategoryItem category2;  //  二级分类
    private CategoryItem category3;  //  三级分类

    private boolean onlyThirdCategory;  //  true  只能选择三级分类

    private boolean opCategory;     //  true 手术分类，false 所有分类

    //商品分类
    public final MutableLiveData<List<CategoryItem>> goodsCategoryLiveData = new MutableLiveData<>();

    private final SearchService searchService;

    public AllGoodsViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    public void loadCategory(){
        if (opCategory) {
            planCategory();
        }else{
            goodsCategory();
        }
    }

    //  商品分类
    private void goodsCategory() {
        searchService.goodsCatrgory().subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
            @Override
            public void onSuccess(ResponseBean<List<CategoryItem>> responseBean) {
                goodsCategoryLiveData.postValue(responseBean.getData());
            }
        });
    }

    private void planCategory(){
        Api.getApiService(RaiseService.class)
                .planCategory()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> listResponseBean) {
                        goodsCategoryLiveData.postValue(listResponseBean.getData());
                    }
                });
    }

    public CategoryItem getCategory() {
        return category;
    }

    public void setCategory(CategoryItem category) {
        this.category = category;
    }

    public CategoryItem getCategory2() {
        return category2;
    }

    public void setCategory2(CategoryItem category2) {
        this.category2 = category2;
    }

    public CategoryItem getCategory3() {
        return category3;
    }

    public void setCategory3(CategoryItem category3) {
        this.category3 = category3;
    }

    public boolean isOnlyThirdCategory() {
        return onlyThirdCategory;
    }

    public void setOnlyThirdCategory(boolean onlyThirdCategory) {
        this.onlyThirdCategory = onlyThirdCategory;
    }

    public boolean isOpCategory() {
        return opCategory;
    }

    public void setOpCategory(boolean opCategory) {
        this.opCategory = opCategory;
    }
}

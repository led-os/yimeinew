package com.chengzi.app.ui.peopleraise.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.peopleraise.model.RaiseService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.subjects.PublishSubject;

public class PeopleRaiseActivityViewModel extends BaseViewModel {

    public final ObservableField<String> keyword = new ObservableField<>();
    public final MutableLiveData<String> searchLive = new MutableLiveData<>();
    public final MutableLiveData<String> categoryIdLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    private final CommonModel commonModel = new CommonModel();
    private PublishSubject publishSubject = PublishSubject.create();

    public PeopleRaiseActivityViewModel() {
        keyword.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (TextUtils.isEmpty(keyword.get())) {
                    searchLive.postValue("");
                } else {
//                    publishSubject.onNext("");
                }
            }
        });
        publishSubject.skip(300, TimeUnit.MILLISECONDS)
                .subscribe(o -> searchLive.postValue(keyword.get()));
    }

    public void categoryList() {

        Api.getApiService(RaiseService.class)
                .planCategory()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> listResponseBean) {
                        categoryLive.postValue(listResponseBean.getData());
                    }
                });

    }


}

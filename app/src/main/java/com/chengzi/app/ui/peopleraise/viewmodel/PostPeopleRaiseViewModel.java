package com.chengzi.app.ui.peopleraise.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.peopleraise.bean.PostRaiseBean;
import com.chengzi.app.ui.peopleraise.model.RaiseService;

public class PostPeopleRaiseViewModel extends BaseViewModel {

    public final ObservableField<String> description = new ObservableField<>();    //   描述
    public final ObservableField<String> price = new ObservableField<>();       //  价格

    public final MutableLiveData<PostRaiseBean> addLive = new MutableLiveData<>();

    private String categoryId;    //  三级分类id
    private String cityId;        //  城市id
    private String days;          //  时间天数
    private String peopleNum;     //  目标人数

    private final RaiseService raiseService;

    public PostPeopleRaiseViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
    }

    public void planAdd(){

        String userId = AccountHelper.getUserId();
        Params params = Params.newParams()
                .put("user_id", userId)
                .put("category_id", categoryId)
                .put("city_id", cityId)
                .put("plan_days", days)
                .put("people_number", peopleNum)
                .put("price", price.get())
                .put("dec",description.get());
        raiseService.planAdd(params.params())
                .subscribe(new SimpleObserver<ResponseBean<PostRaiseBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PostRaiseBean> responseBean) {
                        addLive.postValue(responseBean.getData());
                    }
                });
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

}

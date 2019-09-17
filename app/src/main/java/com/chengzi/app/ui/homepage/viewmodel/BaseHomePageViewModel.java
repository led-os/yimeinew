package com.chengzi.app.ui.homepage.viewmodel;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;


public class BaseHomePageViewModel extends BaseViewModel {

//    public final MutableLiveData<ResponseBean> addVisitLiveData = new MutableLiveData<>();

    //  //Visitcount - 记录用户访问四大类的数据（黄）
    //    //uid	Number    【必填】当前访问的用户ID
    //    //id	Number    【必填】当前访问的对象ID（医生ID/咨询师ID/医院ID/商品ID等）
    //   //type	Number    【必填】访问对象类型，1/咨询师；2/医生；3/机构；4/商品。
    public void addVisit(String id, String type) {
        if (AccountHelper.isLogin() && !AccountHelper.getUserId().equals(id)) {
            Api.getApiService(HospitalHomeService.class)
                    .addVisit(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type)
                    .subscribe(new SimpleObserver<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean responseBean) {
//                            addVisitLiveData.postValue(new Pair<>(forumBean, collected ? 0 : 1));
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            super.onError(code, errorMsg);

                        }
                    });
        }
    }
}
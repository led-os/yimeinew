package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.account.model.AccountService;
import com.chengzi.app.ui.mine.bean.PromotionAgreementBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

/**
 * ① 4种身份 用户协议
 * ② 推广规则
 *
 * @ClassName:IWantPopularizeViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/13 0013   18:02
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/13 0013 handongkeji All rights reserved.
 */

public class UserAgreementViewModel extends BaseViewModel {//implements IRequest {

    //我的推广--获取推广规则内容(syx)
    public final MutableLiveData<ResponseBean<PromotionAgreementBean>> promotionAgreementLiveData = new MutableLiveData<>();
    //用户使用协议
    public final MutableLiveData<ResponseBean<PromotionAgreementBean>> getAgreementDetailsLiveData = new MutableLiveData<>();


    private final MineHospitalService mineService;

    public UserAgreementViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    //我的推广--获取推广规则内容(syx)
    public void promotionAgreement() {
        mineService.promotionAgreement(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<PromotionAgreementBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PromotionAgreementBean> responseBean) {
                        promotionAgreementLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        promotionAgreementLiveData.postValue(null);
                    }
                });
    }

    /**
     * user_type	Number
     * 类型 1-用户 2-医生 3-咨询师 4-机构（必须）
     * <p>
     * info_type	Number
     * 消息类型 1-协议 2-VIP 3-推广 （必须）
     */
    public void getAgreementDetails() {
        Api.getApiService(AccountService.class)
                .getAgreementDetails(String.valueOf(AccountHelper.getIdentity()), "1")
                .subscribe(new SimpleObserver<ResponseBean<PromotionAgreementBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PromotionAgreementBean> responseBean) {
                        getAgreementDetailsLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        getAgreementDetailsLiveData.postValue(null);
                    }
                });
    }
}
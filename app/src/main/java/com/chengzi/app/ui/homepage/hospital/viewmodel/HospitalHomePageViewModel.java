package com.chengzi.app.ui.homepage.hospital.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;
import com.chengzi.app.ui.homepage.model.UserHomeService;
import com.chengzi.app.ui.homepage.viewmodel.BaseHomePageViewModel;

public class HospitalHomePageViewModel extends BaseHomePageViewModel {

    public final ObservableBoolean requestCallPermission = new ObservableBoolean();
    public final ObservableBoolean requestCallPermissionHospital = new ObservableBoolean();

    public final MutableLiveData<HospitalHomeInfoBean> hospitalDetailLive = new MutableLiveData<>();
    public final MutableLiveData<Integer> attentionLive = new MutableLiveData<>();

    private String hospitalId;
    private boolean self;    //  true 从个人中心查看自己的主页，false 查看别人的主页
    private String promotionId;     //  推广id

    private String head_path;

    public String getHead_path() {
        return head_path;
    }

    public void setHead_path(String head_path) {
        this.head_path = head_path;
    }

    //  用户主页头部--更换自己的封面
    public final MutableLiveData<ResponseBean> changeCoverLive = new MutableLiveData<>();

    private final HospitalHomeService hospitalHomeService;

    public HospitalHomePageViewModel() {
        hospitalHomeService = Api.getApiService(HospitalHomeService.class);
    }

    public void getHospitalDetail() {
        //未登录的情况下。当前uid为null,传0可不报错uid不能为空
        String userId = AccountHelper.getUserId();
        userId = !TextUtils.isEmpty(userId) ? userId : "0";
        hospitalHomeService.hospitalDetailInfo(userId, hospitalId)
                .subscribe(new SimpleObserver<ResponseBean<HospitalHomeInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<HospitalHomeInfoBean> hospitalHomeInfoBeanResponseBean) {
                        hospitalDetailLive.postValue(hospitalHomeInfoBeanResponseBean.getData());
                    }
                });
    }

    //Consultant - 关注/取消关注（冀）
    public void findFollow(boolean attention) {
        Api.getApiService(UserHomeService.class)
                .findFollow(AccountHelper.getToken(), AccountHelper.getUserId(), hospitalId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        attentionLive.postValue(attention ? 0 : 1);
                    }
                });
    }

    public void promotionCut() {

        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId())?"0":AccountHelper.getUserId();
        hospitalHomeService.promotionCutHospitalSearch(promotionId, userId,"1")
                .subscribe();
    }

    //用户主页头部--更换自己的封面
    public void changeCover() {
        Api.getApiService(UserHomeService.class)
                .changeCover(AccountHelper.getToken(), AccountHelper.getUserId(), getHead_path())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        changeCoverLive.postValue(responseBean);
                    }
                });
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}

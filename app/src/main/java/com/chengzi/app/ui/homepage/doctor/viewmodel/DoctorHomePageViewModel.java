package com.chengzi.app.ui.homepage.doctor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;
import com.chengzi.app.ui.homepage.model.UserHomeService;
import com.chengzi.app.utils.PreferenceManager;

public class DoctorHomePageViewModel extends HospitalHomePageViewModel {

    public final MutableLiveData<DoctorHomeInfoBean> doctorHomeLive = new MutableLiveData<>();
    public final MutableLiveData<Integer> attentionLive = new MutableLiveData<>();

    //  用户主页头部--更换自己的封面
    public final MutableLiveData<String> changeCoverLive = new MutableLiveData<>();

    public final ObservableBoolean requestCallPermissionDoc = new ObservableBoolean();
    public final ObservableBoolean requestCallPermissionOrg = new ObservableBoolean();

    private String doctorId;
    private boolean self;    //  true 从个人中心查看自己的主页，false 查看别人的主页
    private int userType;   //  2 医生   3 咨询师
    private String promotionId;     //  推广id

    private final DoctorHomeService doctorHomeService;

    public DoctorHomePageViewModel() {
        doctorHomeService = Api.getApiService(DoctorHomeService.class);
    }

    public void doctorHomeInfo() {
        String longitude = PreferenceManager.getPreference(PreferenceManager.LONGITUDE);
        String latitude = PreferenceManager.getPreference(PreferenceManager.LATITUDE);
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId();
        doctorHomeService.doctorHomePage(userId, doctorId, longitude, latitude)
                .subscribe(new SimpleObserver<ResponseBean<DoctorHomeInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<DoctorHomeInfoBean> doctorHomeInfoBeanResponseBean) {
                        doctorHomeLive.postValue(doctorHomeInfoBeanResponseBean.getData());
                    }
                });
    }

    //Consultant - 关注/取消关注（冀）
    public void findFollow(boolean attention) {
        Api.getApiService(UserHomeService.class)
                .findFollow(AccountHelper.getToken(), AccountHelper.getUserId(), doctorId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        attentionLive.postValue(attention ? 0 : 1);
                    }
                });
    }

    //用户主页头部--更换自己的封面
    public void changeCover(String path) {
        Api.getApiService(UserHomeService.class)
                .changeCover(AccountHelper.getToken(), AccountHelper.getUserId(), path)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        changeCoverLive.postValue(path);
                    }
                });
    }

    public void promotionCut() {

        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "0" : AccountHelper.getUserId();
        if (userType == 2) {
            doctorHomeService
                    .promotionCutDoctorSearch(promotionId, userId, "1")
                    .subscribe();
        } else {
            doctorHomeService.promotionCutConsultantSearch(promotionId, userId, "1")
                    .subscribe();
        }
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}

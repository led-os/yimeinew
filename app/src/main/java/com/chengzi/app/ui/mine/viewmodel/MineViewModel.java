package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.bean.IsSignBean;
import com.chengzi.app.ui.mine.bean.PromoteRoutingBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;
import com.chengzi.app.ui.mine.model.MineHospitalService;
import com.chengzi.app.ui.mine.model.MineService;

/**
 * @Desc:
 * @ClassName:MineViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/29 0029
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class MineViewModel extends BaseViewModel {

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();
    //User - 获取用户签到状态（冀）
    public final MutableLiveData<ResponseBean<IsSignBean>> isSignLiveData = new MutableLiveData<>();
    //用户签到(普通用户)
    public final MutableLiveData<ResponseBean> responseBeanLiveData = new MutableLiveData<>();

    //设置座机
    public final MutableLiveData<ResponseBean> phoneSettingLiveData = new MutableLiveData<>();

    //我要推荐
    public final MutableLiveData<ResponseBean<PromoteRoutingBean>> promoteRoutingLiveData = new MutableLiveData<>();
    //医院/咨询师/医生认证页面
    public final MutableLiveData<ResponseBean<AuthenticationBean>> authenticationLiveData = new MutableLiveData<>();


//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    //用户信息
    public void userInfo() {
        Api.getApiService(MineService.class).userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                userInfoLiveData.postValue(userInfoBeanResponseBean.getData());
            }
        });
    }

    //用户签到
    public void isSign() {
        Api.getApiService(MineService.class).isSign(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean<IsSignBean>>() {
            @Override
            public void onSuccess(ResponseBean<IsSignBean> responseBean) {
                isSignLiveData.postValue(responseBean);
            }
        });
    }

    //用户签到
    public void userSign() {
        Api.getApiService(MineService.class).userSign(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }


    //我要推荐
    public void promoteRouting() {
        Api.getApiService(MineDoctorOrCounselorService.class)
                .promoteRouting(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                promoteRoutingLiveData.postValue(responseBean);
            }
        });
    }

    //设置座机
    public void phoneSetting(String telephone) {
        Api.getApiService(MineHospitalService.class)
                .phoneSetting(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), telephone).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                phoneSettingLiveData.postValue(responseBean);
            }
        });
    }

    /**
     * 拒绝后获取提交信息->用户认证页面展示用
     * //医院/咨询师/医生认证页面
     */
    public void authentication() {
        Api.getApiService(MineService.class).authentication(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<AuthenticationBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AuthenticationBean> responseBean) {
                        authenticationLiveData.postValue(responseBean);
                    }
                });
    }
}
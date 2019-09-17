package com.chengzi.app.ui.account.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.account.bean.ThirdLoginBean;
import com.chengzi.app.ui.account.model.AccountService;
import com.chengzi.app.ui.bean.account.UserInfoBean;

import java.util.HashMap;

/**
 * 登录
 *
 * @ClassName:LoginViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/19 0019   15:26
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class LoginViewModel extends BaseViewModel {

    //手机号  密码
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> pwd = new ObservableField<>();

    //登录
    public final MutableLiveData<UserInfoBean> loginLiveData = new MutableLiveData<>();
    //绑定手机号
    public final MutableLiveData<ResponseBean<UserInfoBean>> wxBindLiveData = new MutableLiveData<>();

    public final MutableLiveData<ThirdLoginBean> thirdLoginLiveData = new MutableLiveData<>();

    private final AccountService accountService;

    public LoginViewModel() {
        accountService = Api.getApiService(AccountService.class);
    }

    //获取验证码
    public final MutableLiveData<ResponseBean> sendCodeLiveData = new MutableLiveData<>();

    //获取验证码  device_id=1代表安卓
    public void sendCode(String mobile) {
        accountService.sendCode(mobile, String.valueOf(AccountHelper.getIdentity()), "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        sendCodeLiveData.postValue(bean);
                    }
                });
    }

    //登录 device_id 1安卓
    public void login() {
        accountService.login(mobile.get(), pwd.get(), String.valueOf(AccountHelper.getIdentity()), "1")
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        loginLiveData.postValue(userInfoBeanResponseBean.getData());
                    }
                });
    }

    //三方登录
    public void thirdLogin(String open_id, String unionid, String img) {
        HashMap<String, String> params = Params.newParams()
                .put("open_id", open_id)
                .put("unionid", unionid)
                .put("img", img)
                .params();
        accountService.thirdLogin(params)
                .subscribe(new SimpleObserver<ThirdLoginBean>() {
                    @Override
                    public void onSuccess(ThirdLoginBean userInfoBeanResponseBean) {
                        thirdLoginLiveData.postValue(userInfoBeanResponseBean);
                    }
                });
    }

    //三方登录
    public void thirdBind(String mobile, String repassword, String password, String m_code) {
        HashMap<String, String> params = Params.newParams()
                .put("user_id", AccountHelper.getUserId())
                .put("open_id", AccountHelper.getOpenId())
                .put("img", AccountHelper.getIconurl())
                .put("unionid", AccountHelper.getUid())
                .put("c_type", String.valueOf(AccountHelper.getIdentity()))
                .put("is_read", "1")
                .put("mobile", mobile)
                .put("repassword", repassword)
                .put("password", password)
                .put("m_code", m_code)
                .params();
        accountService.wxBind(params)
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        wxBindLiveData.postValue(userInfoBeanResponseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                    }
                });
    }
}
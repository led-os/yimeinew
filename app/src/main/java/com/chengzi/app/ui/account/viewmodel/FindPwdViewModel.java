package com.chengzi.app.ui.account.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.account.model.AccountService;

/**
 * 找回密码
 *
 * @ClassName:FindPwdViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/6/25 0025   17:51
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/6/25 0025 handongkeji All rights reserved.
 */

public class FindPwdViewModel extends BaseViewModel {

    //手机号 验证码 密码 重复密码
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> m_code = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> repassword = new ObservableField<>();


    //获取验证码
    public final MutableLiveData<ResponseBean> sendCodeLiveData = new MutableLiveData<>();
    //注册
    public final MutableLiveData<ResponseBean> retrievePasswordLiveData = new MutableLiveData<>();

    private final AccountService accountService;

    public FindPwdViewModel() {
        accountService = Api.getApiService(AccountService.class);
    }

    //获取验证码  device_id=1代表安卓
    public void sendCode() {
        accountService.sendCode(mobile.get(), String.valueOf(AccountHelper.getIdentity()), "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        sendCodeLiveData.postValue(bean);
                    }
                });
    }

    /***
     * 重置/找回密码
     */
    public void retrievePassword() {
        accountService.retrievePassword(mobile.get(), password.get(), repassword.get(), m_code.get(), "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        retrievePasswordLiveData.postValue(responseBean);
                    }
                });
    }
}
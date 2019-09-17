package com.chengzi.app.ui.account.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.account.model.AccountService;
import com.chengzi.app.ui.bean.account.UserInfoBean;

import java.util.HashMap;

/**
 * 注册
 *
 * @ClassName:RegisterViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/19 0019   16:11
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class RegisterViewModel extends BaseViewModel {

    //手机号 验证码 密码 重复密码  推荐手机号
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> m_code = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> repassword = new ObservableField<>();
    public final ObservableField<String> rec_mobile = new ObservableField<>();

    //获取验证码
    public final MutableLiveData<ResponseBean> sendCodeLiveData = new MutableLiveData<>();
    //注册
    public final MutableLiveData<UserInfoBean> registerLiveData = new MutableLiveData<>();

    private final AccountService accountService;

    public RegisterViewModel() {
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
     * 注册
     * 手机号 短信验证码 密码 再次密码 推荐人手机号
     * 用户类型 协议是否选择（4，未选中） 城市id(需要android端和ios端参数填入)
     */
    public void register() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile.get());
        map.put("m_code", m_code.get());
        map.put("password", password.get());
        map.put("repassword", repassword.get());
        if (!TextUtils.isEmpty(rec_mobile.get()))
            map.put("rec_mobile", rec_mobile.get());
        map.put("c_type", String.valueOf(AccountHelper.getIdentity()));
        map.put("is_read", "1");
        map.put("city_id", "110100");
        accountService.register(map)
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        registerLiveData.postValue(userInfoBeanResponseBean.getData());
                    }
                });
    }

    //绑定手机号
    public final MutableLiveData<ResponseBean<UserInfoBean>> wxBindLiveData = new MutableLiveData<>();

    //三方登录
    public void thirdBind() {
        HashMap<String, String> params = Params.newParams()
                .put("user_id", AccountHelper.getUserId())
                .put("open_id", AccountHelper.getOpenId())
                .put("img", AccountHelper.getIconurl())
                .put("unionid", AccountHelper.getUid())
                .put("c_type", String.valueOf(AccountHelper.getIdentity()))
                .put("is_read", "1")
                .put("mobile", mobile.get())
                .put("rec_mobile", rec_mobile.get())
                .put("repassword", repassword.get())
                .put("password", password.get())
                .put("m_code", m_code.get())
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
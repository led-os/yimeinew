package com.chengzi.app.ui.mine.model;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

import java.util.HashMap;

/**
 * @Desc:
 * @ClassName:UpdatePhoneAndPwdViewModel
 * @PackageName:com.yimei.app.ui.mine.model
 * @Create On 2019/4/29 0029
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class UpdatePhoneAndPwdViewModel extends BaseViewModel {
    public final MutableLiveData<ResponseBean> responseBeanLiveData = new MutableLiveData<>();

    public void changeMobile(String mobile, String code) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("mobile", mobile)// mobile
                .put("code", code)//验证码
                .params();
        Api.getApiService(MineService.class).changeMobile(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }

    public void changePwd(String mobile, String password, String passwordRe, String code) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("telephone", mobile)
                .put("password", password)// 新密码
                .put("passwordRe", passwordRe)// 新密码To
                .put("code", code)//验证码
                .params();
        Api.getApiService(MineService.class).changePwd(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }
}
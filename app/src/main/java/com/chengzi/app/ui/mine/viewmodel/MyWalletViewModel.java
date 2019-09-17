package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.chengzi.app.ui.mine.bean.AliAuthoInfo;
import com.chengzi.app.ui.mine.bean.AliUserInfo;
import com.chengzi.app.ui.mine.bean.WalletBean;
import com.chengzi.app.ui.mine.model.MineService;
import com.google.gson.JsonObject;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

import java.util.HashMap;

/**
 * @Desc:
 * @ClassName:MyWalletViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/25 0025
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class MyWalletViewModel extends BaseViewModel {


    public static final String BIND_ACCOUNT_TYPE_WX = "0";//微信

    public static final String BIND_ACCOUNT_TYPE_ALI = "1";//支付宝

    public static final String BIND_ACCOUNT_TYPE_ACCOUNT = "2";//对公账户

    public final MutableLiveData<ResponseBean<WalletBean>> walletInfoLiveData = new MutableLiveData<>();

    public final MutableLiveData<ResponseBean> responseBeanLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> withDrawLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<AliAuthoInfo>> aliAuthInfoLiveData = new MutableLiveData<>();

    public final MutableLiveData<ResponseBean<AliUserInfo>> aliUserInfoLiveData = new MutableLiveData<>();

    public final MutableLiveData<String> aliAuthLive = new MutableLiveData<>();


    //提现方式 0-微信，1-支付宝 2-对公账户 （必须）
    public ObservableField<String> type = new ObservableField<>();


    /**
     * 钱包信息
     */
    public void walletInfo() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .params();
        Api.getApiService(MineService.class).walletInfo(params).subscribe(new SimpleObserver<ResponseBean<WalletBean>>() {
            @Override
            public void onSuccess(ResponseBean<WalletBean> walletBeanResponseBean) {
                walletInfoLiveData.postValue(walletBeanResponseBean);
            }
        });
    }

    /**
     * 绑定微信
     *
     * @param open_id
     */
    public void bindWX(String open_id) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", BIND_ACCOUNT_TYPE_WX)//账号类型，0微信，1支付宝，2对公账户
                .put("open_id", open_id)// 微信唯一标识如：openID
                .params();
        Api.getApiService(MineService.class).bindAccount(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }


    /**
     * 绑定支付宝
     *
     * @param authCode -支付宝账号
     */
    public void bindALi(String authCode) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", BIND_ACCOUNT_TYPE_ALI)//账号类型，0微信，1支付宝，2对公账户
                .put("account", authCode)   // 支付宝账户
                .params();
        Api.getApiService(MineService.class).bindAccount(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }

    /**
     * 绑定对公账户
     *
     * @param name      真实姓名
     * @param card_id   身份证号
     * @param bank_id   银行卡号
     * @param bank_name 银行名称
     *                  //     * @param tel       预留手机号
     *                  //     * @param m_code    手机验证码
     */
    public void bindCoAccount(String name, String card_id, String bank_id, String bank_name, //String tel, String m_code,
                              String province_name, String city_name, String companyBankName) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", BIND_ACCOUNT_TYPE_ACCOUNT)//账号类型，0微信，1支付宝，2对公账户
                .put("name", name)
                .put("card_id", card_id)
                .put("bank_id", bank_id)
                .put("bank_name", bank_name)
                .put("company_bank_name", companyBankName)
//                .put("sheng_name", province_name)
//                .put("shi_name", city_name)
//                .put("tel", tel)
//                .put("m_code", m_code)
                .params();
        Api.getApiService(MineService.class).bindAccount(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }


    /**
     * 解绑提现账号
     *
     * @param type
     */
    public void unBindAccount(String type) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", type)//账号类型，0微信，1支付宝，2对公账户
                .params();
        Api.getApiService(MineService.class).unbindAccount(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }

//    /**
//     * 获取支付宝授权参数
//     */
//    public void getAliAuthInfo() {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .params();
//        Api.getApiService(MineService.class).getAliAuthInfo(params).subscribe(new SimpleObserver<ResponseBean<AliAuthoInfo>>() {
//            @Override
//            public void onSuccess(ResponseBean<AliAuthoInfo> aliAuthoInfoResponseBean) {
//                aliAuthInfoLiveData.postValue(aliAuthoInfoResponseBean);
//            }
//        });
//    }
//
//
//    /**
//     * 获取支付宝用户信息
//     *
//     * @param authInfo
//     */
//    public void getAliUserInfo(String authInfo) {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .params();
//        Api.getApiService(MineService.class).getAliUserInfo(params).subscribe(new SimpleObserver<ResponseBean<AliUserInfo>>() {
//            @Override
//            public void onSuccess(ResponseBean<AliUserInfo> aliUserInfoResponseBean) {
//                aliUserInfoLiveData.postValue(aliUserInfoResponseBean);
//            }
//        });
//    }

    /**
     * 提现
     *
     * @param money
     */
    public void withDraw(String money) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("money", money)
                .put("type", type.get())
                .params();
        Api.getApiService(MineService.class).withdraw(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                withDrawLiveData.postValue(responseBean);
            }
        });

    }

    public void aliAuthParams() {
        Api.getApiService(MineService.class)
                .aliAccountRequestParam(AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<JsonObject>>() {
                    @Override
                    public void onSuccess(ResponseBean<JsonObject> jsonObjectResponseBean) {
                        JsonObject jsonObject = jsonObjectResponseBean.getData();
                        if (jsonObject.has("request_str")) {
                            aliAuthLive.postValue(jsonObject.get("request_str").getAsString());
                        } else {
                            aliAuthLive.postValue(null);
                        }
                    }
                });
    }

}

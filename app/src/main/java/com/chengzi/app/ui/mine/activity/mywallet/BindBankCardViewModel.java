package com.chengzi.app.ui.mine.activity.mywallet;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.chengzi.app.ui.mine.model.MineService;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

import java.util.HashMap;

public class BindBankCardViewModel extends BaseViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> idCardNumber = new ObservableField<>();
    public final ObservableField<String> bankCardNumber = new ObservableField<>();
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> provinceName = new ObservableField<>();
    public final ObservableField<String> cityName = new ObservableField<>();

    public String bankName;
    public String bankId;


    public final MutableLiveData<Boolean> bindBankcardLive = new MutableLiveData<>();
    public static final String BIND_ACCOUNT_TYPE_ACCOUNT = "2";//对公账户


    public void bindBankCard() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", BIND_ACCOUNT_TYPE_ACCOUNT)//账号类型，0微信，1支付宝，2对公账户
                .put("name", name.get())
                .put("card_id", idCardNumber.get())
                .put("bank_id", bankCardNumber.get())
                .put("bank_name", bankName)
                .put("sheng_name", provinceName.get())
                .put("shi_name", cityName.get())
                .put("tel", mobile.get())
//                .put("m_code", m_code)
                .params();
        Api.getApiService(MineService.class).bindAccount(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                bindBankcardLive.postValue(true);
            }
        });

    }

}

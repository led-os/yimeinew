package com.chengzi.app.ui.account.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.account.model.AccountService;

import java.util.HashMap;

/**
 * @Desc:
 * @ClassName:HospitalCertifiedViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/30 0030
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class HospitalCertifiedViewModel extends BaseViewModel {

    public ObservableField<String> cardUrl = new ObservableField<>();

    public ObservableField<String> card_front = new ObservableField<>();

    public ObservableField<String> card_reverse = new ObservableField<>();

    public ObservableField<String> bussiness_aqtitude_img = new ObservableField<>();
    public ObservableField<String> permission = new ObservableField<>();

    public ObservableField<String> hospital_opreation_name = new ObservableField<>();

    public ObservableField<String> hosipital_type = new ObservableField<>();

    public ObservableField<String> guangshen_aqtitude_img = new ObservableField<>();

    public ObservableField<String> real_name = new ObservableField<>();
    public ObservableField<String> city_id = new ObservableField<>();

    public MutableLiveData<ResponseBean> responseLiveData = new MutableLiveData<>();

    /*public void certified() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("card", cardUrl.get())//手持身份证   （必传）
                .put("card_front", card_front.get())//   身份证正面  （必）
                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
                .put("bussiness_aqtitude_img", bussiness_aqtitude_img.get())//   医院营业执照  （必传）
                .put("permission", permission.get())// 医院医疗机构许可证  （必传）
                .put("hospital_opreation_name", hospital_opreation_name.get())// 运营人姓名   （必）
                .put("hospital_name", real_name.get())
                .put("city_id", city_id.get())
                .put("hosipital_type", hosipital_type.get())//医院类型     （必传 ）1民营机构 2公司机构 3品牌连锁 4生活美容机构
                .put("guangshen_aqtitude_img", guangshen_aqtitude_img.get())// 广审表（非）-->改为必传2019-6-25
                .params();
        Api.getApiService(AccountService.class).hospitalApprove(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }

    public void certified(String info_id) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("info_id", info_id)
                .put("card", cardUrl.get())//手持身份证   （必传）
                .put("card_front", card_front.get())//   身份证正面  （必）
                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
                .put("bussiness_aqtitude_img", bussiness_aqtitude_img.get())//   医院营业执照  （必传）
                .put("permission", permission.get())// 医院医疗机构许可证  （必传）
                .put("hospital_opreation_name", hospital_opreation_name.get())// 运营人姓名   （必）
                .put("hospital_name", real_name.get())
                .put("city_id", city_id.get())
                .put("hosipital_type", hosipital_type.get())//医院类型     （必传 ）1民营机构 2公司机构 3品牌连锁 4生活美容机构
                .put("guangshen_aqtitude_img", guangshen_aqtitude_img.get())// 广审表（非）
                .params();
        Api.getApiService(AccountService.class).hospitalRestart(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }*/
    public void user_audit() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("card", cardUrl.get())//手持身份证   （必传）
                .put("card_front", card_front.get())//   身份证正面  （必）
                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
                .put("bussiness_aqtitude_img", bussiness_aqtitude_img.get())//   医院营业执照  （必传）
                .put("permission", permission.get())// 医院医疗机构许可证  （必传）
                .put("hospital_opreation_name", hospital_opreation_name.get())// 运营人姓名   （必）
                .put("hospital_name", real_name.get())
                .put("city_id", city_id.get())
                .put("hosipital_type", hosipital_type.get())//医院类型     （必传 ）1民营机构 2公司机构 3品牌连锁 4生活美容机构
                .put("guangshen_aqtitude_img", guangshen_aqtitude_img.get())// 广审表（非）-->改为必传2019-6-25
                .params();
        Api.getApiService(AccountService.class).user_audit(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }
}
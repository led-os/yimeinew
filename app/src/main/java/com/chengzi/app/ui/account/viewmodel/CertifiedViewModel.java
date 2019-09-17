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
 * 认证
 *
 * @ClassName:CertifiedViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/25 0025   13:52
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/25 0025 handongkeji All rights reserved.
 */

public class CertifiedViewModel extends BaseViewModel {


    public ObservableField<String> cardUrl = new ObservableField<>();

    public ObservableField<String> card_front = new ObservableField<>();

    public ObservableField<String> card_reverse = new ObservableField<>();

    public ObservableField<String> aptitude_image = new ObservableField<>();

    public ObservableField<String> aptitude_orther = new ObservableField<>();

    public ObservableField<String> doccertificate_image = new ObservableField<>();

    public ObservableField<String> doccertificate_imagea = new ObservableField<>();

    public ObservableField<String> real_name = new ObservableField<>();

    public ObservableField<String> qualnumber = new ObservableField<>();

    public ObservableField<String> occupation_class = new ObservableField<>();

    public MutableLiveData<ResponseBean> responseLiveData = new MutableLiveData<>();

    public void user_audit() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("card", cardUrl.get())//手持身份证   （必传）
                .put("card_front", card_front.get())//   身份证正面  （必）
                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
                .put("doccertificate_image", doccertificate_image.get())//医生职业证书编号（必）
                .put("doccertificate_imagea", doccertificate_imagea.get())// 医生职业证书地点（必）
                .put("aptitude_image", aptitude_image.get())//其他资质证书照片  （非必传）
                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
                .put("real_name", real_name.get())
                .put("c_type", AccountHelper.getIdentity() + "")
                .put("occupation_class", occupation_class.get())//1主任医师 2副主任医师 3医师 4主治医师 5国外医师
                .put("qualnumber", qualnumber.get())
                .params();
        Api.getApiService(AccountService.class).user_audit(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }

//    public void docotorCertified() {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .put("card", cardUrl.get())//手持身份证   （必传）
//                .put("card_front", card_front.get())//   身份证正面  （必）
//                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
//                .put("doccertificate_image", doccertificate_image.get())//医生职业证书编号（必）
//                .put("doccertificate_imagea", doccertificate_imagea.get())// 医生职业证书地点（必）
//                .put("aptitude_image", aptitude_image.get())//其他资质证书照片  （非必传）
//                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
//                .put("real_name", real_name.get())
//                .put("c_type", AccountHelper.getIdentity() + "")
//                .put("occupation_class", occupation_class.get())//1主任医师 2副主任医师 3医师 4主治医师 5国外医师
//                .put("qualnumber", qualnumber.get())
//                .params();
//        Api.getApiService(AccountService.class).doctorApprove(params)
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        responseLiveData.postValue(responseBean);
//                    }
//                });
//    }
//
//    public void doctorRestart(String info_id) {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .put("info_id", info_id)
//                .put("card", cardUrl.get())//手持身份证   （必传）
//                .put("card_front", card_front.get())//   身份证正面  （必）
//                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
//                .put("doccertificate_image", doccertificate_image.get())//医生职业证书编号（必）
//                .put("doccertificate_imagea", doccertificate_imagea.get())// 医生职业证书地点（必）
//                .put("aptitude_image", aptitude_image.get())//其他资质证书照片  （非必传）
//                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
//                .put("real_name", real_name.get())
//                .put("c_type", AccountHelper.getIdentity() + "")
//                .put("occupation_class", occupation_class.get())//1主任医师 2副主任医师 3医师 4主治医师 5国外医师
//                .put("qualnumber", qualnumber.get())
//                .params();
//        Api.getApiService(AccountService.class).doctorRestart(params)
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        responseLiveData.postValue(responseBean);
//                    }
//                });
//    }
}
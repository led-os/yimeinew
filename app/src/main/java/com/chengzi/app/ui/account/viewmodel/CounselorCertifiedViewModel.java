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
 * @ClassName:CounselorCertifiedViewModel
 * @PackageName:com.yimei.app.ui.account.viewmodel
 * @Create On 2019/4/30 0030
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class CounselorCertifiedViewModel extends BaseViewModel {

    public ObservableField<String> cardUrl = new ObservableField<>();

    public ObservableField<String> card_front = new ObservableField<>();

    public ObservableField<String> card_reverse = new ObservableField<>();

    public ObservableField<String> aptitude_image = new ObservableField<>();

    public ObservableField<String> aptitude_orther = new ObservableField<>();

    public ObservableField<String> real_name = new ObservableField<>();


    public MutableLiveData<ResponseBean> responseLiveData = new MutableLiveData<>();

    public void user_audit() {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("card", cardUrl.get())//手持身份证   （必传）
                .put("card_front", card_front.get())//   身份证正面  （必）
                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
                .put("aptitude_image", aptitude_image.get())//资质证书照片  （非必传）
                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
                .put("real_name", real_name.get())// 真实姓名   （必）
                .params();
        Api.getApiService(AccountService.class).user_audit(params)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        responseLiveData.postValue(responseBean);
                    }
                });
    }
//    public void certified() {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .put("card", cardUrl.get())//手持身份证   （必传）
//                .put("card_front", card_front.get())//   身份证正面  （必）
//                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
//                .put("aptitude_image", aptitude_image.get())//资质证书照片  （非必传）
//                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
//                .put("real_name", real_name.get())// 真实姓名   （必）
//                .params();
//        Api.getApiService(AccountService.class).counselorApprove(params)
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        responseLiveData.postValue(responseBean);
//                    }
//                });
//    }

//    public void certified(String info_id) {
//        HashMap<String, String> params = Params.newParams()
//                .put("token", AccountHelper.getToken())
//                .put("user_id", AccountHelper.getUserId())
//                .put("info_id", info_id)
//                .put("card", cardUrl.get())//手持身份证   （必传）
//                .put("card_front", card_front.get())//   身份证正面  （必）
//                .put("card_reverse", card_reverse.get())//   身份证反面  （必）
//                .put("aptitude_image", aptitude_image.get())//资质证书照片  （非必传）
//                .put("aptitude_orther", aptitude_orther.get())// 其他资质证书照片  （非必传）
//                .put("real_name", real_name.get())// 真实姓名   （必）
//                .params();
//        Api.getApiService(AccountService.class).counselorRestart(params)
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        responseLiveData.postValue(responseBean);
//                    }
//                });
//    }
}

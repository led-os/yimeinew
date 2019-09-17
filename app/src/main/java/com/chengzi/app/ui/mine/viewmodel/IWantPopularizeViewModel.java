package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.ui.mine.bean.DoctorPromotionBean;
import com.chengzi.app.ui.mine.bean.HospitalPromotionBean;
import com.chengzi.app.ui.mine.bean.PromotionInfoBean;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;
import com.chengzi.app.ui.pay.bean.PaymentBean;
import com.chengzi.app.ui.pay.model.PayService;

import java.util.List;

/**
 * 医院->我要推广
 *
 * @ClassName:IWantPopularizeViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/13 0013   18:02
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/13 0013 handongkeji All rights reserved.
 */

public class IWantPopularizeViewModel extends BaseViewModel implements IRequest {

    // 我的推广--主页(syx)
    public final MutableLiveData<ResponseBean<PromotionInfoBean>> promotionInfoLiveData = new MutableLiveData<>();

    // Promotion - 我的搜索推广--医生/咨询师列表(syx)
    public final MutableLiveData<List<DoctorPromotionBean>> doctorOrConsultantPromotionLive = new MutableLiveData<>();

    // 我的推广--搜索推广-医院推广，查看医院信息(syx)
    public final MutableLiveData<ResponseBean<HospitalPromotionBean>> hospitalPromotionLiveData = new MutableLiveData<>();
    // Promotion - 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
    public final MutableLiveData<ResponseBean> setSearchPromotionAmountLiveData = new MutableLiveData<>();
    // 我的推广-添加搜索推广(包括商品、医院等所有的搜索推广)
    public final MutableLiveData<ResponseBean> addSearchPromotionItemLiveData = new MutableLiveData<>();
    // 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
    public final MutableLiveData<ResponseBean> removeSearchPromotionItemLiveData = new MutableLiveData<>();
    // 我的推广--商品分类列表(syx)
    public final MutableLiveData<ResponseBean<List<PromotionSelectCategoryBean>>> promotionSelectCategoryLiveData = new MutableLiveData<>();
    // 我的推广--支付(syx)
    public final MutableLiveData<ResponseBean<SpreadsOrderBean>> promotionPaymentLiveData = new MutableLiveData<>();

    private final MineHospitalService mineService;

    public IWantPopularizeViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    public int doctorOrCounselorPromotion;

    @Override //医生/咨询师推广列表
    public void onRequest(int currentPage, int pageSize) {
        if (doctorOrCounselorPromotion == 2) {    //2医生 3咨询师
            mineService.doctorPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), String.valueOf(currentPage), String.valueOf(pageSize))
                    .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorPromotionBean>>>() {
                        @Override
                        public void onSuccess(ResponseBean<PageBean<DoctorPromotionBean>> data) {
                            doctorOrConsultantPromotionLive.postValue(data.getData().getData());
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            super.onError(code, errorMsg);
                            doctorOrConsultantPromotionLive.postValue(null);
                        }
                    });
        } else if (doctorOrCounselorPromotion == 3) {
            mineService.consultantPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), String.valueOf(currentPage), String.valueOf(pageSize))
                    .subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorPromotionBean>>>() {
                        @Override
                        public void onSuccess(ResponseBean<PageBean<DoctorPromotionBean>> data) {
                            doctorOrConsultantPromotionLive.postValue(data.getData().getData());
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            super.onError(code, errorMsg);
                            doctorOrConsultantPromotionLive.postValue(null);
                        }
                    });
        }
    }

    //我的推广--主页(syx)
    public void promotionInfo() {
        mineService.promotionInfo(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<PromotionInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PromotionInfoBean> responseBean) {
                        promotionInfoLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        promotionInfoLiveData.postValue(null);
                    }
                });
    }

    //查看医院推广信息
    public void hospitalPromotion() {
        mineService.hospitalPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<HospitalPromotionBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<HospitalPromotionBean> responseBean) {
                        hospitalPromotionLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        hospitalPromotionLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
     *
     * @param id     要设置的对象的id，比如医生ID、商品ID
     * @param type   类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param amount 设置的限额数
     */
    public void setSearchPromotionAmount(String id, String type, String amount) {
        mineService.setSearchPromotionAmount(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type, amount)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        setSearchPromotionAmountLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        setSearchPromotionAmountLiveData.postValue(null);
                    }
                });
    }


    /**
     * 添加搜索推广(包括商品、医院等所有的搜索推广)
     *
     * @param id       【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type     【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param //amount 设置的限额数，不填则默认为0，不限额
     */
    public void addSearchPromotionItem(String id, String type) {
        mineService.addSearchPromotionItem(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addSearchPromotionItemLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        addSearchPromotionItemLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
     *
     * @param id   【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type 【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     */
    public void removeSearchPromotionItem(String id, String type) {
        mineService.removeSearchPromotionItem(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        removeSearchPromotionItemLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        removeSearchPromotionItemLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广--商品分类列表(syx)
     *
     * @param zone_type //专区类型 （1.普通;2.VIP）
     */
    public void promotionSelectCategory(String zone_type) {
        mineService.promotionSelectCategory(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), zone_type)
                .subscribe(new SimpleObserver<ResponseBean<List<PromotionSelectCategoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<PromotionSelectCategoryBean>> responseBean) {
                        promotionSelectCategoryLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        promotionSelectCategoryLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广--商品分类列表(syx)
     *
     * @param money      支付金额
     * @param //pay_mode 1-支付宝 2-微信
     */
    public void promotionPayment(String money) {
/**
 @Field("pay_mode") String pay_mode,   // 支付方式 1-支付宝 2-微信
 @Field("pay_type") String pay_type,   // 支付类型 1-VIP 2-推广费用 3-订单 4-美人筹
 @Field("type") String type);          // 4医院
 */
        mineService.promotionCreateOrder(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), money)
                .subscribe(new SimpleObserver<ResponseBean<SpreadsOrderBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<SpreadsOrderBean> responseBean) {
                        promotionPaymentLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        promotionPaymentLiveData.postValue(null);
                    }
                });
    }


    public final MutableLiveData<PaymentBean> payLive = new MutableLiveData<>();

    public void payment(String payWay, String prepaylog_id) {   //  用此id 发起支付
        String paymentType = TextUtils.equals(payWay, Sys.wxPay) ? "wechat_app" : "ali_app";
        Api.getApiService(PayService.class).payment(prepaylog_id, paymentType)
                .subscribe(new SimpleObserver<ResponseBean<PaymentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<PaymentBean> paymentBeanResponseBean) {
                        payLive.postValue(paymentBeanResponseBean.getData());
                    }
                });
    }

    public void payNotify(String prepaylog_id){
        Api.getApiService(PayService.class).payNotify(prepaylog_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }
}
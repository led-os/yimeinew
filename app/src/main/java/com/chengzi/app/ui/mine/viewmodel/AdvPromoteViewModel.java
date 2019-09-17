package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.BannerPromotionListBean;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.bean.ZonePromotionBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.List;

/**
 * banner广告推广
 *
 * @ClassName:AdvPromoteViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/15 0015   09:33
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/15 0015 handongkeji All rights reserved.
 */

public class AdvPromoteViewModel extends BaseViewModel {
    // 我的推广--商品分类列表(syx)
    public final MutableLiveData<ResponseBean<List<PromotionSelectCategoryBean>>> categoryLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<List<PromotionSelectCategoryBean>>> categoryVipLiveData = new MutableLiveData<>();
    // 推荐广告列表
    public final MutableLiveData<ResponseBean<BannerPromotionListBean>> bannerPromotionListLiveData = new MutableLiveData<>();
    // 我要推广 - 新增加推广（黄） 用于 我的 - 我要推广中，专区推广和Banner广告位推广
    public final MutableLiveData<ResponseBean> addPromotionLiveData = new MutableLiveData<>();
    // Promotion - 专区推广--主页(syx)
    public final MutableLiveData<ResponseBean<ZonePromotionBean>> zonePromotionLiveData = new MutableLiveData<>();
    // 删除推广位（黄） 用于 我的 - 我要推广中，删除专区推广和Banner广告位推广
    public final MutableLiveData<ResponseBean> removePromotionLiveData = new MutableLiveData<>();

    private final MineHospitalService mineService;

    public AdvPromoteViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    /**
     * 我的推广--商品分类列表(syx)
     *
     * @param //zone_type //【必填】专区类型 （1.普通;2.VIP）
     */
    public void advCategory() {
        mineService.bannerPromotionCategory("1")
                .subscribe(new SimpleObserver<ResponseBean<List<PromotionSelectCategoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<PromotionSelectCategoryBean>> responseBean) {
                        categoryLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        categoryLiveData.postValue(null);
                    }
                });
    }

    public void advVipCategory() {
        mineService.bannerPromotionCategory("2")
                .subscribe(new SimpleObserver<ResponseBean<List<PromotionSelectCategoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<PromotionSelectCategoryBean>> responseBean) {
                        categoryVipLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        categoryVipLiveData.postValue(null);
                    }
                });
    }

    //广告列表
    public void bannerPromotionList(String zone_type, String cate_id) {
        mineService.bannerPromotionList(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), zone_type, cate_id)
                .subscribe(new SimpleObserver<ResponseBean<BannerPromotionListBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<BannerPromotionListBean> responseBean) {
                        bannerPromotionListLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bannerPromotionListLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我要推广中，专区推广和Banner广告位推广
     *
     * @param c_type     类型 1-banner推广 2-商品推广
     * @param cate_id    专区（分类）ID
     * @param zone_type  专区类型 （1.普通;2.VIP）
     * @param model_id   模式ID，商品推广时是商品ID，banner推广时是医院ID
     * @param //amount   预支付金额
     * @param banner_img 封面图，当类型为 Banner推广时，该项必填
     */
    public void addPromotion(String c_type, String cate_id, String zone_type, String model_id, String banner_img) {
        mineService.addPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), c_type, cate_id, zone_type, model_id, banner_img)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addPromotionLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        addPromotionLiveData.postValue(null);
                    }
                });
    }

    public void addPromotion(String c_type, String cate_id, String zone_type, String model_id, String amount, String banner_img) {
        mineService.addPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), c_type, cate_id, zone_type, model_id, amount, banner_img)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addPromotionLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        addPromotionLiveData.postValue(null);
                    }
                });
    }

    /**
     * 专区推广
     *
     * @param zone_type 专区类型 （1.普通;2.VIP）
     */
    public void zonePromotion(String zone_type) {
        String user_id = AccountHelper.getUserId();
        mineService.zonePromotion(AccountHelper.getToken(), user_id, user_id, zone_type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        zonePromotionLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        zonePromotionLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广-删除推广位（黄） 用于 我的 - 我要推广中，删除专区推广和Banner广告位推广
     *
     * @param id 【必填】要删除的推广的ID
     */
    public void removePromotion(String id) {
        String user_id = AccountHelper.getUserId();
        mineService.removePromotion(AccountHelper.getToken(), user_id, user_id, id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        removePromotionLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        removePromotionLiveData.postValue(null);
                    }
                });
    }
}
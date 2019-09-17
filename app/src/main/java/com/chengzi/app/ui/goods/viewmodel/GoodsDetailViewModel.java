package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.model.GoodService;
import com.chengzi.app.ui.homepage.viewmodel.BaseHomePageViewModel;
import com.chengzi.app.utils.PreferenceManager;

public class GoodsDetailViewModel extends BaseHomePageViewModel {

    public final ObservableField<String> loopPos = new ObservableField();
    public final ObservableBoolean requestCallPermission = new ObservableBoolean();
    public final MutableLiveData<GoodDetailBean> goodDetailLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> addToCarLive = new MutableLiveData<>();

    private String goodId;
    private String promotionId;     //  推广id
    private String type = "0";     //  推广id  1-专区推广  2-搜索推广
//    private boolean is_buy = false;     //是否可抢购可购买(秒杀列表-)

    private final GoodService goodService;

    public GoodsDetailViewModel() {
        goodService = Api.getApiService(GoodService.class);
    }

    public void getGoodDetails() {
        String longitude = PreferenceManager.getPreference(PreferenceManager.LONGITUDE);
        String latidute = PreferenceManager.getPreference(PreferenceManager.LATITUDE);
        goodService.goodsDetails(goodId, longitude, latidute)
                .subscribe(new SimpleObserver<ResponseBean<GoodDetailBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<GoodDetailBean> goodDetailBeanResponseBean) {
                        goodDetailLive.postValue(goodDetailBeanResponseBean.getData());
                    }
                });
    }

    public void addToCart(String doctorId, String counselorId) {
        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("goods_id", goodId)
                .put("doctor_id", doctorId)
                .put("counselling_id", counselorId)
                .put("is_ad",!TextUtils.isEmpty(promotionId) && !TextUtils.equals(promotionId,"0")?"1":"0");
        goodService.addToCart(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addToCarLive.postValue(true);
                    }
                });
    }

    public void promotionCut() {

        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId())?"0":AccountHelper.getUserId();
        goodService.promotionCutZone(promotionId, userId,"1")
                .subscribe();
    }

    public void promotionCutProductSearch() {

        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId())?"0":AccountHelper.getUserId();
        goodService.promotionCutProductSearch(promotionId, userId,"1")
                .subscribe();
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    //  推广id  1-专区推广  2-搜索推广
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

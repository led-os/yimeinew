package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.model.GoodService;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.mine.bean.CouponListBean;
import com.chengzi.app.ui.mine.model.MineService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class ConfirmGoodsOrderViewModel extends BaseViewModel {

    public final ObservableField<GoodDetailBean> goodDetail = new ObservableField<>();
    public final MutableLiveData<List<CouponListBean>> couponLive = new MutableLiveData<>();
    public final MutableLiveData<List<String>> confirmOrderLive = new MutableLiveData<>();

    //  商品数量  拼购时 只能为 1 个， 普通下单无数量限制，秒杀下单有数量限制
    public final MutableLiveData<Integer> goodsNumLive = new MutableLiveData<>();

    // 订单类型 1-普通下单 2-发起拼购，参与拼购 3-秒杀下单 （必须）
    private static final String[] ORDER_TYPE_TEXT = {"1", "3", "2", "2"};

    private int type;   //   0 普通下单，1 秒杀下单，2 发起拼购，3 参与拼购
    private boolean isAd;   //  true 推广订单

    private String orderId;     //   参与拼购时的拼购订单id
    private SpellBean spellBean;    //  拼购订单信息

    private DoctorBean doctor;          //   选择的医生
    private DoctorBean counselor;       //  选择的咨询师
    private CouponListBean coupon;      //  选择的优惠券

    private double payPrice;    //  实际支付金额单价

    private final MineService mineService;

    public ConfirmGoodsOrderViewModel() {
        mineService = Api.getApiService(MineService.class);
        goodsNumLive.setValue(1);
    }

    //  获取优惠券
    public void getMyCoupon() {
        mineService.couponList(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<List<CouponListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CouponListBean>> listResponseBean) {
                        couponLive.postValue(listResponseBean.getData());
                    }
                });
    }

    //  确认下单（冀）
    public void confirmOrder() {

        String goodId = goodDetail.get().getId();

        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("goods_id", goodId)                     //      商品id （必须）
                .put("goods_num", String.valueOf(goodsNumLive.getValue()))  //  商品数量，拼购时只能传1（必须）
                .put("type", ORDER_TYPE_TEXT[type])         //  订单类型 1-普通下单 2-发起拼购，参与拼购 3-秒杀下单 （必须）
                .put("doctor_id", doctor == null ? null : doctor.getId())   //  医生id（与咨询师，二者不能同时为空）
                .put("counselling_id", counselor == null ? null : counselor.getId())    //  咨询师id（与医生，二者不能同时为空）
                .put("order_id", orderId)      //   拼购发起id，type为2时，参与拼购时传此参数，否则为发起拼购
                .put("coupon_id", coupon == null ? null : coupon.getId())
                .put("longitude", PreferenceManager.getPreference(PreferenceManager.LONGITUDE))
                .put("latitude", PreferenceManager.getPreference(PreferenceManager.LATITUDE))
                .put("is_ad", isAd ? "1" : "0");

        Api.getApiService(GoodService.class)
                .confirmOrder(params.params())
                .subscribe(new SimpleObserver<ResponseBean<List<String>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<String>> listResponseBean) {
                        confirmOrderLive.postValue(listResponseBean.getData());
                    }
                });
    }


    //获取商品库存（冀）
    public void getGoodsStock(int requireNum) {
        Api.getApiService(GoodService.class).getGoodsStock(goodDetail.get().getId())
                .subscribe(new SimpleObserver<ResponseBean<Integer>>() {
                    @Override
                    public void onSuccess(ResponseBean<Integer> integerResponseBean) {
                        Integer data = integerResponseBean.getData();
                        if (data < 0) {  //  不限数量
                            goodsNumLive.postValue(requireNum);
                        } else if (data == 0) {   //  库存不足
                            goodsNumLive.postValue(1);
                            ToastUtils.showShort("库存不足");
                        } else {
                            if (requireNum <= data) {
                                goodsNumLive.postValue(requireNum);
                            } else {
                                goodsNumLive.postValue(data);
                                ToastUtils.showShort("库存不足");
                            }
                        }
                    }
                });
    }

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public DoctorBean getCounselor() {
        return counselor;
    }

    public void setCounselor(DoctorBean counselor) {
        this.counselor = counselor;
    }

    public CouponListBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponListBean coupon) {
        this.coupon = coupon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public SpellBean getSpellBean() {
        return spellBean;
    }

    public void setSpellBean(SpellBean spellBean) {
        this.spellBean = spellBean;
    }

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }
}

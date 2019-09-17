package com.chengzi.app.ui.goods.activity;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityConfirmGoodsOrderBinding;
import com.chengzi.app.dialog.DiscountCouponDialog;
import com.chengzi.app.dialog.SelectDoctorDialog;
import com.chengzi.app.event.OrderPaymentTypeEvent;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.fragment.ConfirmGoodsJoinSpellFragment;
import com.chengzi.app.ui.goods.fragment.ConfirmGoodsOrderFragment;
import com.chengzi.app.ui.goods.fragment.ConfirmGoodsSpellFragment;
import com.chengzi.app.ui.goods.viewmodel.ConfirmGoodsOrderViewModel;
import com.chengzi.app.ui.mine.bean.CouponListBean;
import com.chengzi.app.ui.pay.activity.PayOrderActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起拼购
 *
 * @ClassName:LaunchGroupBuyActivity
 * @PackageName:com.yimei.app.ui.goods.activity
 * @Create On 2019/4/16 0016   17:05
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class ConfirmGoodsOrderActivity extends BaseActivity<ActivityConfirmGoodsOrderBinding,
        ConfirmGoodsOrderViewModel> implements View.OnClickListener {

    public static final int ORDER_TYPE_NORMAL = 0x00;   //普通下单
    public static final int ORDER_TYPE_SECKILL = 0x01;  //秒杀下单
    public static final int ORDER_TYPE_SPELL = 0x02;    //发起拼购
    public static final int ORDER_TYPE_SPELL_JOIN = 0x03;   //参与拼购

    private static final String EXTRA_ORDER_TYPE = "extra_order_type";
    private static final String EXTRA_ORDER_INFO = "extra_order_info";
    private static final String EXTRA_ORDER_IS_AD = "extra_order_is_ad";

    /**
     * @param context
     * @param type    0 普通下单，1 秒杀下单，2 发起拼购，3 参与拼购
     */
    public static void start(Context context, int type, boolean isAd) {
        Intent intent = new Intent(context, ConfirmGoodsOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_TYPE, type);
        intent.putExtra(EXTRA_ORDER_IS_AD, isAd);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param spellBean 参与拼购时，参与的拼购订单
     * @param type      0 普通下单，1 秒杀下单，2 发起拼购，3 参与拼购
     */
    public static void start(Context context, SpellBean spellBean, int type, boolean isAd) {
        Intent intent = new Intent(context, ConfirmGoodsOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_TYPE, type)
                .putExtra(EXTRA_ORDER_IS_AD, isAd)
                .putExtra(EXTRA_ORDER_INFO, spellBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_confirm_goods_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        parseIntent();

        mBinding.setListener(this);
        mBinding.setBean(mViewModel.goodDetail.get());
        mViewModel.getMyCoupon();

        if (mViewModel.getType() == ORDER_TYPE_SPELL) {
            mBinding.topBar.setCenterText(getString(R.string.launch_group_buy));
        } else {
            mBinding.topBar.setCenterText(getString(R.string.confirm_order));
        }

        //   0 普通下单，1 秒杀下单，2 发起拼购，3 参与拼购
        GoodDetailBean bean = mViewModel.goodDetail.get();
        if (mViewModel.getType() == ORDER_TYPE_NORMAL) {
            String buy_price = bean.getBuy_price();
            mViewModel.setPayPrice(Double.valueOf(TextUtils.isEmpty(buy_price) ? "0" : buy_price));
        } else if (mViewModel.getType() == ORDER_TYPE_SECKILL) {
            String kill_price = bean.getKill_price();
            mViewModel.setPayPrice(Double.valueOf(TextUtils.isEmpty(kill_price) ? "0" : kill_price));
        } else {
            String spell_price = bean.getSpell_price();
            mViewModel.setPayPrice(Double.valueOf(TextUtils.isEmpty(spell_price) ? "0" : spell_price));
        }

        addGoodsInfoFragment();

        observe();
    }

    private void parseIntent() {
        int orderType = getIntent().getIntExtra(EXTRA_ORDER_TYPE, 0);
        mViewModel.setType(orderType);

        boolean isAd = getIntent().getBooleanExtra(EXTRA_ORDER_IS_AD, false);
        mViewModel.setAd(isAd);

        SpellBean spellBean = (SpellBean) getIntent().getSerializableExtra(EXTRA_ORDER_INFO);
        if (spellBean != null) {
            mViewModel.setOrderId(spellBean.getOrder_id());
            mViewModel.setSpellBean(spellBean);
        }
    }

    private void observe() {
        mViewModel.confirmOrderLive.observe(this, strings -> {
            dismissLoading();
            EventBus.getDefault().postSticky(new OrderPaymentTypeEvent(mViewModel.getType() <= 1 ? 1 : 2));
            PayOrderActivity.start(this, strings);
            finish();
        });

        mViewModel.goodsNumLive.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                CouponListBean coupon = mViewModel.getCoupon();
                double amount = 0;
                if (coupon != null) {
                    amount = Double.valueOf(coupon.getAmount());  //  抵扣金额
                }
                mBinding.tvPayAmount.setText(getString(R.string.price_place_holder_with_unit_d, integer * mViewModel.getPayPrice() - amount));
            }
        });
    }

    private void addGoodsInfoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mViewModel.getType() == ORDER_TYPE_NORMAL || mViewModel.getType() == ORDER_TYPE_SECKILL) {
            //   0 普通下单，1 秒杀下单
            ConfirmGoodsOrderFragment fragment = ConfirmGoodsOrderFragment.
                    newInstance(getSupportFragmentManager());
            if (!fragment.isAdded()) {
                transaction.add(R.id.header_container, fragment, ConfirmGoodsOrderFragment.class.getSimpleName());
            }
        } else if (mViewModel.getType() == ORDER_TYPE_SPELL) {
            //  2 发起拼购
            ConfirmGoodsSpellFragment fragment = ConfirmGoodsSpellFragment.
                    newInstance(getSupportFragmentManager());
            if (!fragment.isAdded()) {
                transaction.add(R.id.header_container, fragment, ConfirmGoodsSpellFragment.class.getSimpleName());
            }
        } else {
            //  3 参与拼购
            ConfirmGoodsJoinSpellFragment fragment = ConfirmGoodsJoinSpellFragment.
                    newInstance(getSupportFragmentManager(), mViewModel.getSpellBean());
            if (!fragment.isAdded()) {
                transaction.add(R.id.header_container, fragment, ConfirmGoodsJoinSpellFragment.class.getSimpleName());
            }
        }

        transaction.commit();
    }

    @Subscribe(sticky = true)
    public void fetchData(GoodDetailBean bean) {
        mViewModel.goodDetail.set(bean);

    }

    @Override
    public void onClick(View v) {

        if (!ClickEvent.shouldClick(v)) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_select_coupon:    //  选择优惠券
                selectCoupon();
                break;
            case R.id.btn_select_doctor:        //  选择医生
                SelectDoctorDialog selectDoctorDialog = new SelectDoctorDialog();
                selectDoctorDialog.setUserType(2);
                selectDoctorDialog.setData(mViewModel.goodDetail.get().getDoctor_data());
                selectDoctorDialog.show(getSupportFragmentManager(), SelectDoctorDialog.class.getSimpleName());
                selectDoctorDialog.setListener(object -> {
                    mViewModel.setDoctor(object);
                    mBinding.tvSelectDoctor.setText(object.getName());
                });
                break;
            case R.id.btn_select_counselor:     //  选择咨询师
                selectDoctorDialog = new SelectDoctorDialog();
                selectDoctorDialog.setUserType(3);
                selectDoctorDialog.setData(mViewModel.goodDetail.get().getCounselling_data());
                selectDoctorDialog.show(getSupportFragmentManager(), SelectDoctorDialog.class.getSimpleName());
                selectDoctorDialog.setListener(object -> {
                    mViewModel.setCounselor(object);
                    mBinding.tvSelectCounselor.setText(object.getName());
                });
                break;
            case R.id.btn_confirm_order:        //  确认订单
                confirmOrder();
                break;
        }
    }

    private void selectCoupon() {
        DiscountCouponDialog discountCouponDialog = new DiscountCouponDialog();
        mViewModel.couponLive.observe(discountCouponDialog, couponListBeans -> {

            List<CouponListBean> list = new ArrayList<>(couponListBeans);

            //  订单金额不满足使用的优惠券不要显示
            double totalAmount = mViewModel.goodsNumLive.getValue() * mViewModel.getPayPrice();
            for (int i = 0; i < list.size(); i++) {
                CouponListBean couponListBean = list.get(i);
                double full_amount = couponListBean.getFull_amount();
                if (totalAmount < full_amount) {
                    list.remove(i);
                    i--;
                }
            }

            if (list.isEmpty()) {
                ToastUtils.showLong("没有可用的优惠券");
                discountCouponDialog.dismiss();
                return;
            }

            discountCouponDialog.setData(list);
            GoodDetailBean goodDetailBean = mViewModel.goodDetail.get();
            if (goodDetailBean != null && goodDetailBean.getHospital_data() != null) {
                discountCouponDialog.setHospitalName(goodDetailBean.getHospital_data().getName());
            }
        });
        discountCouponDialog.setSelectedCoupon(mViewModel.getCoupon());
        discountCouponDialog.show(getSupportFragmentManager(), DiscountCouponDialog.class.getSimpleName());
        discountCouponDialog.setSelectedlistener(object -> {
            GoodDetailBean goodDetailBean = mViewModel.goodDetail.get();
            mViewModel.setCoupon(object);
            if (object == null) {
                mBinding.tvCoupon.setText("");
                mBinding.tvPayAmount.setText(getString(R.string.price_place_holder_with_unit_d, mViewModel.goodsNumLive.getValue() * mViewModel.getPayPrice()));
            } else {
                mBinding.tvCoupon.setText("已抵扣" + object.getAmount() + "元");
                double amount = Double.valueOf(object.getAmount());  //  抵扣金额
                double price = mViewModel.goodsNumLive.getValue() * mViewModel.getPayPrice() - amount;
                //      选择优惠券后 更改 实际支付金额
                mBinding.tvPayAmount.setText(getString(R.string.price_place_holder_with_unit_d, price));
            }
        });
    }

    private void confirmOrder() {

        if (AccountHelper.shouldLogin(this)) {
            return;
        }

        if (mViewModel.getDoctor() == null && mViewModel.getCounselor() == null) {
            ToastUtils.showShort("请选择一个医生或咨询师");
            return;
        }

        if (mViewModel.getType() == ORDER_TYPE_SPELL_JOIN && TextUtils.isEmpty(mViewModel.getOrderId())) {
            ToastUtils.showShort("参与拼购数据错误");
            return;
        }
        showLoading("");
        mViewModel.confirmOrder();
    }
}

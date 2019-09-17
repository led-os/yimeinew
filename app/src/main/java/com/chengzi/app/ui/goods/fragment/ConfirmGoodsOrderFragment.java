package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.blankj.utilcode.util.SizeUtils;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentConfirmGoodsOrderBinding;
import com.chengzi.app.ui.goods.activity.ConfirmGoodsOrderActivity;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.ConfirmGoodsOrderViewModel;

/**
 * 普通下单-确认订单页 头部商品信息
 *
 * @ClassName:ConfirmGoodsOrderFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/5/22 0022   10:42
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/22 0022 handongkeji All rights reserved.
 */
public class ConfirmGoodsOrderFragment extends BaseFragment<FragmentConfirmGoodsOrderBinding,
        ConfirmGoodsOrderViewModel> {

    public ConfirmGoodsOrderFragment() {
    }

    public static ConfirmGoodsOrderFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(ConfirmGoodsOrderFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new ConfirmGoodsOrderFragment();
            fragment.setArguments(args);
        }

        return (ConfirmGoodsOrderFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_confirm_goods_order;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        GoodDetailBean goodDetailBean = viewModel.goodDetail.get();
        binding.setBean(goodDetailBean);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
        spannableStringBuilder.append("¥", absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        if (viewModel.getType() == ConfirmGoodsOrderActivity.ORDER_TYPE_SECKILL) {
            //  秒杀中，显示秒杀价
            spannableStringBuilder.append(goodDetailBean.getKill_price());
        } else {
            spannableStringBuilder.append(goodDetailBean.getBuy_price());
        }

        binding.tvPrice.setText(spannableStringBuilder);

        binding.numberActionView.setBtnDecrease(number -> {
            viewModel.getGoodsStock(number);
        });

        binding.numberActionView.setBtnIncrease(number -> {
            viewModel.getGoodsStock(number);
        });
        observe();
    }

    private void observe() {
        viewModel.goodsNumLive.observe(this, integer -> {
            binding.numberActionView.setNumber(integer);
        });
    }
}

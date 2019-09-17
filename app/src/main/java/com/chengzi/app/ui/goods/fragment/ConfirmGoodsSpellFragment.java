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
import com.chengzi.app.databinding.FragmentConfirmGoodsSpellBinding;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.ConfirmGoodsOrderViewModel;

/**
 * 发起拼购--确认订单页头部商品信息
 *
 * @ClassName:ConfirmGoodsSpellFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/5/22 0022   10:47
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/22 0022 handongkeji All rights reserved.
 */
public class ConfirmGoodsSpellFragment extends BaseFragment<FragmentConfirmGoodsSpellBinding,
        ConfirmGoodsOrderViewModel> {

    public ConfirmGoodsSpellFragment() {
    }

    public static ConfirmGoodsSpellFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(ConfirmGoodsSpellFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new ConfirmGoodsSpellFragment();
            fragment.setArguments(args);
        }
        return (ConfirmGoodsSpellFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_confirm_goods_spell;
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
        spannableStringBuilder.append("拼团价  ¥", absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(goodDetailBean.getSpell_price());

        binding.tvPrice.setText(spannableStringBuilder);

    }
}

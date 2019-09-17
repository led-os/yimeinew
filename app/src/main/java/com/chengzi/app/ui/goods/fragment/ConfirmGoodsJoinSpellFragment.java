package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.blankj.utilcode.util.SizeUtils;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentConfirmGoodsJoinSpellBinding;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.viewmodel.ConfirmGoodsOrderViewModel;

/**
 * 参与拼购--确认订单头部商品信息
 *
 * @ClassName:ConfirmGoodsJoinSpellFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/5/22 0022   10:46
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/22 0022 handongkeji All rights reserved.
 */
public class ConfirmGoodsJoinSpellFragment extends BaseFragment<FragmentConfirmGoodsJoinSpellBinding,
        ConfirmGoodsOrderViewModel> {

    private static final String EXTRA_ORDER_INFO = "extra_order_info";

    public ConfirmGoodsJoinSpellFragment() {
    }

    public static ConfirmGoodsJoinSpellFragment newInstance(FragmentManager fm, SpellBean spellBean) {
        ConfirmGoodsJoinSpellFragment fragment = (ConfirmGoodsJoinSpellFragment) fm.findFragmentByTag(ConfirmGoodsJoinSpellFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putSerializable(EXTRA_ORDER_INFO, spellBean);
            fragment = new ConfirmGoodsJoinSpellFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_confirm_goods_join_spell;
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

        SpellBean spellBean = (SpellBean) getArguments().getSerializable(EXTRA_ORDER_INFO);
        binding.setSpellBean(spellBean);

    }
}

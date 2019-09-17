package com.chengzi.app.ui.goods.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.adapter.GroupBuyAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSubSpellBinding;
import com.chengzi.app.ui.goods.activity.ConfirmGoodsOrderActivity;
import com.chengzi.app.ui.goods.activity.GroupBuyingActivity;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.goods.viewmodel.SubSpellViewModel;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;

public class SubSpellFragment extends RefreshableFragment<FragmentSubSpellBinding, SubSpellViewModel>
        implements View.OnClickListener {

    private GroupBuyAdapter groupBuyAdapter;

    public SubSpellFragment() {
    }

    public static SubSpellFragment newInstance(FragmentManager fm, String goodsId) {
        Fragment fragment = fm.findFragmentByTag(SubSpellFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putString(Sys.EXTRA, goodsId);
            fragment = new SubSpellFragment();
            fragment.setArguments(args);
        }
        return (SubSpellFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sub_spell;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String goodsId = getArguments().getString(Sys.EXTRA);
        viewModel.setGoodsId(goodsId);

        GoodsDetailViewModel detailViewModel = ViewModelProviders.of(getActivity()).get(GoodsDetailViewModel.class);
        detailViewModel.goodDetailLive.observe(this, bean -> {
            viewModel.setGoodDetailBean(bean);
            if (bean == null) {
                return;
            }
            if (bean.getIs_killing() == 1) {
                long current_time = bean.getCurrent_time() * 1000;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(current_time);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int kill_time = bean.getKill_time();

                viewModel.setKilling(hourOfDay >= kill_time && hourOfDay < kill_time + 2);

            } else {
                viewModel.setKilling(false);
            }
            boolean hide = viewModel.isKilling() || viewModel.isSpellEmpty();
            binding.getRoot().setVisibility(hide ? View.GONE : View.VISIBLE);
        });

        binding.setListener(this);
        initGroupBuy();

        viewModel.spellLive.observe(this, pageBean -> {

            refreshComplete = true;
            finishRefresh();

            int totalNum = pageBean.getTotal();
            List<SpellBean> spellBeans = pageBean.getData();
            groupBuyAdapter.setNewData(spellBeans);
            binding.tvPurchaseNum.setText(getString(R.string.some_people_are_purchaseing, totalNum));

            viewModel.setSpellEmpty(spellBeans == null || spellBeans.isEmpty());
            boolean hide = viewModel.isKilling() || viewModel.isSpellEmpty();
            binding.getRoot().setVisibility(hide ? View.GONE : View.VISIBLE);

        });
    }

    private void initGroupBuy() {
        groupBuyAdapter = new GroupBuyAdapter();
        binding.recyclerGroupBuy.setAdapter(groupBuyAdapter);
        binding.recyclerGroupBuy.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        groupBuyAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.btn_join_group_buy:   //  去拼购

                    if (AccountHelper.shouldLogin(getActivity())) {
                        return;
                    }

                    SpellBean item = groupBuyAdapter.getItem(position);
                    String user_id = item.getUser_id();
                    if (TextUtils.equals(user_id, AccountHelper.getUserId())) {
                        String order_id = item.getOrder_id();
                        //  拼购列表，点击去拼购，如果是自己发起的，应该跳到订单详情
                        MyOrderListDetailsActivity.start(getActivity(), order_id, 2, 0);
                        return;
                    }

                    GoodDetailBean bean = viewModel.getGoodDetailBean();
                    if (bean == null) {
                        return;
                    }

                    GoodsDetailViewModel detailViewModel = ViewModelProviders.of(getActivity()).get(GoodsDetailViewModel.class);
                    String promotionId = detailViewModel.getPromotionId();
                    boolean isAd = !TextUtils.isEmpty(promotionId) && !TextUtils.equals(promotionId, "0");

                    EventBus.getDefault().postSticky(bean);
                    ConfirmGoodsOrderActivity.start(getActivity(),
                            item, ConfirmGoodsOrderActivity.ORDER_TYPE_SPELL_JOIN, isAd);
                    break;
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.goodsSpellList();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.goodsSpellList();
    }

    @Override
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_more_group_buy:  //  查看更多拼购
                GoodDetailBean bean = viewModel.getGoodDetailBean();
                if (bean == null) {
                    return;
                }

                GoodsDetailViewModel detailViewModel = ViewModelProviders.of(getActivity()).get(GoodsDetailViewModel.class);
                String promotionId = detailViewModel.getPromotionId();
                boolean isAd = !TextUtils.isEmpty(promotionId) && !TextUtils.equals(promotionId, "0");

                EventBus.getDefault().postSticky(bean);
                GroupBuyingActivity.start(getActivity(), viewModel.getGoodsId(), isAd);
                break;
        }
    }
}

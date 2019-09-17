package com.chengzi.app.ui.home.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentTradingDynamicBinding;
import com.chengzi.app.databinding.ItemTradingLayoutBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.TradeListBean;
import com.chengzi.app.ui.home.viewmodel.MainDynamicViewModel;
import com.chengzi.app.ui.home.viewmodel.RecommandViewModel;
import com.chengzi.app.utils.TimeUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 首页--交易动态
 *
 * @ClassName:TradingDynamicFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   19:24
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class TradingDynamicFragment extends BaseFragment<FragmentTradingDynamicBinding, MainDynamicViewModel> {


    private boolean isResumed;
    private boolean isHidden;

    private TradingAdapter adapter;

    ///倒计时
    public static TradingDynamicFragment newInstance() {
        Bundle args = new Bundle();
        TradingDynamicFragment fragment = new TradingDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TradingDynamicFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_trading_dynamic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        adapter = new TradingAdapter();
        binding.recyclerTradingDynamic.setAdapter(adapter);

        viewModel.tradeListLive.observe(this, tradeListBeans -> {
            dismissLoading();

            if (tradeListBeans != null && tradeListBeans.size() > 0) {
                binding.recyclerTradingDynamic.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);
                adapter.setNewData(tradeListBeans);
                startAction();
            } else {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerTradingDynamic.setVisibility(View.GONE);
            }
        });
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (position >= 0 && adapter.getData() != null && adapter.getData().size() > 0) {
                GoodsDetailActivity.start(getActivity(), adapter.getData().get(position).getGoods_id());
            }
        });
        handler.sendEmptyMessageDelayed(0, 1500);

        RecommandViewModel homeViewModel = ViewModelProviders.of(getActivity()).get(RecommandViewModel.class);
        homeViewModel.refreshLive.observe(this,aBoolean -> {
            viewModel.tradeList();
        });
    }

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {

        WeakReference<TradingDynamicFragment> fragment;

        public MyHandler(TradingDynamicFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (fragment.get() == null) {
                return;
            }
            TradingDynamicFragment instance = fragment.get();
            List<TradeListBean> data = instance.adapter.getData();
            if (data != null && data.size() > 0) {
                TradeListBean o = data.get(0);
                instance.adapter.remove(0);
                instance.adapter.addData(o);
                if (instance.isResumed && instance.getUserVisibleHint() && !instance.isHidden) {
                    sendEmptyMessageDelayed(0, 1500);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.tradeList();
        isResumed = true;
        startAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
        stopAction();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startAction();
        } else {
            stopAction();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.isHidden = hidden;
        if (isHidden) {
            stopAction();
        } else {
            startAction();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAction();
    }

    private void startAction() {
        boolean isVisible = isResumed && getUserVisibleHint() && !isHidden;
        if (isVisible && !handler.hasMessages(0) && adapter != null && adapter.getData() != null && adapter.getData().size() > 0) {
            handler.sendEmptyMessageDelayed(0, 1500);
        }
    }

    private void stopAction() {
        handler.removeMessages(0);
    }

    private static class TradingAdapter extends BaseQuickAdapter<TradeListBean, BaseViewHolder> {

        public TradingAdapter() {
            super(R.layout.item_trading_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, TradeListBean item) {
            ItemTradingLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getUser_img());
            binding.setType(item.getType());
            binding.executePendingBindings();
            //1-普通订单 2-拼购订单
            boolean commonOrder = item.getType().equals("1");
            String strs = "";
            if (commonOrder) {  //购买商品
                strs = "购买了" + item.getGoods_name() + "项目";
            } else {
                //拼购剩余时间  天/时/分
                //天 小时 分钟
                strs = TimeUtils.formatDayHourMinuteTime(item.getSurplus());
            }
            //倒计时/购买的商品名

            String str = "还差1人";
            SpannableString ss = new SpannableString(str);
            ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#D40000"));
            ss.setSpan(fcs, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            binding.text1.setText(ss);
            binding.tvTimeOrGoodsname.setText(strs);
        }
    }
}
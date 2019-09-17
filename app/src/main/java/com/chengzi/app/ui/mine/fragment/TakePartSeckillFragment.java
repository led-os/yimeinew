package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.hwangjr.rxbus.RxBus;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentTakePartSeckillBindingImpl;
import com.chengzi.app.databinding.ItemTakePartSeckillLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.mine.activity.takepartseckill.ConfirmTakePartActivity;
import com.chengzi.app.ui.mine.bean.KillHospitalListBean;
import com.chengzi.app.ui.mine.viewmodel.TakePartSeckillViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

import java.util.ArrayList;

/**
 * 参与秒杀 fragment
 *
 * @ClassName:TakePartSeckillFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/12 0012   10:16
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */

public class TakePartSeckillFragment extends LazyloadFragment<FragmentTakePartSeckillBindingImpl, TakePartSeckillViewModel>
        implements OrderStatusHelp.TakePartSeckillTypeChange {
    //分类id
    private String cate_id;
    private PagingLoadHelper helper;

    public static TakePartSeckillFragment newInstance(String cate_id) {
        TakePartSeckillFragment fragment = new TakePartSeckillFragment();
        Bundle args = new Bundle();
        args.putString("cate_id", cate_id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void typeChange(String cate_id) {
        if (this.cate_id.equals(cate_id)) {
            helper.onPulldown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperObservableManager.unregisterObserver(OrderStatusHelp.TakePartSeckillTypeChange.class, this);
        RxBus.get().unregister(this);
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_take_part_seckill;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            cate_id = arguments.getString("cate_id");
        }

        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.TakePartSeckillTypeChange.class, this);

        TakePartSeckillAdapter adapter = new TakePartSeckillAdapter();
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        //秒杀
        viewModel.category_id.set(cate_id);
        viewModel.killHospitalListLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });

        //取消参与
        viewModel.killCancelLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                OrderStatusHelp.refreshTakePartSeckillList(cate_id);
            }
        });
    }

    public class TakePartSeckillAdapter extends BaseQuickAdapter<KillHospitalListBean, BaseViewHolder> {
        public TakePartSeckillAdapter() {
            super(R.layout.item_take_part_seckill_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, KillHospitalListBean item) {
            ItemTakePartSeckillLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            binding.setUrl(item.getLogo());
            binding.executePendingBindings();

            //tv_money
            //如果没参与秒杀 则显示原价 参与秒杀则显示秒杀价
            //// 是否参与过 0-否 1-是
            String is_participation = item.getIs_participation();
            if (is_participation.equals("0")) { //没有参与 要参与的
                //隐藏原价 设置红色价格为原价 (隐藏限购 隐藏时段) 显示参与秒杀 隐藏取消参与
                binding.tvOriginalPrice.setVisibility(View.GONE);
                binding.tvMoney.setText(item.getPractical_price());
                binding.llAttention.setVisibility(View.GONE);
                binding.tvConfirmTakePart.setVisibility(View.VISIBLE);
                binding.tvCancleTakePart.setVisibility(View.GONE);
            } else {
                //显示原价 设置红色价格为秒杀价 (显示限购 显示时段) 隐藏参与秒杀 显示取消参与
                binding.tvOriginalPrice.setVisibility(View.VISIBLE);
                binding.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                binding.tvMoney.setText(item.getKill_price());
                binding.llAttention.setVisibility(View.VISIBLE);
                binding.tvConfirmTakePart.setVisibility(View.GONE);
                binding.tvCancleTakePart.setVisibility(View.VISIBLE);
            }
        }
    }

    private ClickEventHandler<KillHospitalListBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_confirm_take_part:  //确认参与
                //// 是否参与过 0-否 1-是
                if (bean.getIs_participation().equals("0"))
                    ConfirmTakePartActivity.start(getContext(), bean, cate_id);
                break;
            case R.id.tv_cancle_take_part:     //取消参与
                new MessageDialogBuilder(getContext())
                        .setMessage("确认取消参与？")
                        .setSureListener(v -> {
                            showLoading(Sys.LOADING);
                            viewModel.killCancel(bean.getGoods_id());
                        })
                        .show();
                break;
        }
    };
}
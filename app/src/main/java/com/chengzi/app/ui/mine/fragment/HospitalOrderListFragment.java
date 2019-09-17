package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.chengzi.app.databinding.FragmentHospitalOrderListBindingImpl;
import com.chengzi.app.databinding.ItemMyOrderListLayoutBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.mine.activity.myorder.ComplaintsActivity;
import com.chengzi.app.ui.mine.activity.myorder.HospitalEvaluationActivity;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

import java.util.ArrayList;

/**
 * 医院订单管理
 *
 * @ClassName:HospitalOrderListFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/5/6 0006   16:20
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/6 0006 handongkeji All rights reserved.
 */

public class HospitalOrderListFragment extends LazyloadFragment<FragmentHospitalOrderListBindingImpl, MyOrderListViewModel>
        implements OrderStatusHelp.OrderTypeChange {


    //机构订单状态 0-全部 1-待使用，2-待评价，3-已完成
    private int h_status;
    //订单类型  3医院(1-普通订单和秒杀订单 2-拼购订单 （必须）)
    private int type = 3;
    private PagingLoadHelper helper;
    private MyOrderListAdapter adapter;

    public static HospitalOrderListFragment newInstance(int h_status) {
        HospitalOrderListFragment fragment = new HospitalOrderListFragment();
        Bundle args = new Bundle();
        args.putInt("h_status", h_status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_order_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.OrderTypeChange.class, this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            h_status = arguments.getInt("h_status", 0);
        }
        viewModel.h_status = h_status;
        viewModel.type.set(type);
        adapter = new MyOrderListAdapter(myOrderClickListener);
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        //订单详情
        adapter.setOnItemClickListener((adapter1, view, position) ->
                MyOrderListDetailsActivity.start(getContext(), adapter.getData().get(position).getId(), type, 0)
        );

        observe();
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    //刷新订单-->
    @Override
    public void typeChange(int type) {
        if (h_status == this.h_status) {
            helper.onPulldown();
        }
    }

    @Override
    public void typeChange(int type, int status) {

    }

    private void observe() {    // 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消
        //订单列表
        viewModel.userOrderListLive.observe(this, userOrderListBean -> {
            if (userOrderListBean != null && userOrderListBean.size() > 0) {
                helper.onComplete(userOrderListBean);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        //确认使用-->医院
        viewModel.orderConfirmUseLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                //确认使用成功  //刷新-->医院状态下的 全部/待使用/待评价
                OrderStatusHelp.refreshOrderList(0);
                OrderStatusHelp.refreshOrderList(1);
                OrderStatusHelp.refreshOrderList(2);
            }
        });

    }

    private ClickEventHandler<UserOrderListBean> myOrderClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_cancel:  //  取消订单
                new MessageDialogBuilder(getContext())
                        .setMessage("确认取消订单?")
                        .setSureListener(v -> {
                            showLoading(Sys.LOADING);
                            viewModel.userOrderCancel(bean.getId());
                        })
                        .show();
                break;
//            case R.id.tv_payment:  //  付款
//                List<String> order_id = new ArrayList<>();
//                order_id.add(bean.getId());
//                PayOrderActivity.start(getContext(), order_id);
//                break;
            case R.id.tv_difference:  //  确认使用 补差价  //TODO:隐藏确认使用 防止医院误操作
                if (this.type == 3) {
                    new MessageDialogBuilder(getContext())
                            .setMessage("确认用户已使用？")
                            .setSureListener(v -> {
//                                        toast("确认用户已使用");
                                        showLoading(Sys.LOADING);
                                        viewModel.orderConfirmUse(bean.getId());
                                    }
                            )
                            .show();
//                } else {
//                    PriceSpreadActivity.start(getContext(), bean.getId());
                }
                break;
            case R.id.tv_complaints:  //  投诉
                ComplaintsActivity.start(getContext(), bean.getId(), 3);
                break;
            case R.id.tv_evaluation:  //  评价
                startActivity(new Intent(getContext(), HospitalEvaluationActivity.class)
                        .putExtra("order_id", bean.getId())
                );
                break;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperObservableManager.unregisterObserver(OrderStatusHelp.OrderTypeChange.class, this);
        RxBus.get().unregister(this);
    }

    public class MyOrderListAdapter extends BaseQuickAdapter<UserOrderListBean, BaseViewHolder> {
        private ClickEventHandler<UserOrderListBean> clickEventHandler;

        public MyOrderListAdapter(ClickEventHandler<UserOrderListBean> clickEventHandler) {
            super(R.layout.item_my_order_list_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, UserOrderListBean item) {
            ItemMyOrderListLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.setUrl(item.getGoods_image());

            //是否显示补差价(如果补差价金额为空 不显示补差价)
            binding.tvAmountSpreads.setVisibility(!TextUtils.isEmpty(item.getAmount_spreads()) && !TextUtils.equals(item.getAmount_spreads(), "0") ? View.VISIBLE : View.GONE);

//       h_status 机构订单状态 0-全部 1-待使用，2-待评价，3-已完成
//        isComplaint;  是否已投诉 0-未投诉，1-已投诉(待评价时 使用)
            String h_status = item.getH_status();
            String isComplaint = item.getIsComplaint();
            binding.tvOrderType.setText(item.getHStatus_name());
            binding.clTime.setVisibility(View.GONE);
//        //设置显示的button
            if (TextUtils.equals(h_status, "1")) {
                //TODO: 隐藏医院的确认使用 以防止医院误操作
                //待使用 -->显示 确认使用
//                setShowStatusBtn(false, false, false, binding);
                binding.llShowStatus.setVisibility(View.GONE);
            } else if (TextUtils.equals(h_status, "2")) {
                //待评价 -->显示 投诉+评价
                boolean complaints = false;
                if (TextUtils.equals(isComplaint, "0")) {
                    //显示投诉
                    complaints = true;
                } else {
                    //隐藏投诉
                    complaints = false;
                }
                setShowStatusBtn(false, complaints, true, binding);
            } else {
                //已完成 -->不显示
                binding.llShowStatus.setVisibility(View.GONE);
            }
        }

        /**
         * @param difference 确认使用
         * @param complaints 投诉
         * @param evaluation 评价
         * @param binding
         */
        private void setShowStatusBtn(boolean difference, boolean complaints, boolean evaluation, ItemMyOrderListLayoutBinding binding) {
            binding.llShowStatus.setVisibility(View.VISIBLE);
            binding.tvCancel.setVisibility(View.GONE);
            binding.tvPayment.setVisibility(View.GONE);
            //TODO: 隐藏医院的确认使用 以防止医院误操作
//            binding.tvDifference.setText("确认使用");
//            binding.tvDifference.setVisibility(difference ? View.VISIBLE : View.GONE);
            binding.tvDifference.setVisibility(View.GONE);
            binding.tvComplaints.setVisibility(complaints ? View.VISIBLE : View.GONE);
            binding.tvEvaluation.setVisibility(evaluation ? View.VISIBLE : View.GONE);
        }
    }
}
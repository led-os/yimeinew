package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.hwangjr.rxbus.RxBus;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.MyOrderListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentMyOrderListBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.OrderPaymentTypeEvent;
import com.chengzi.app.ui.mine.activity.myorder.ComplaintsActivity;
import com.chengzi.app.ui.mine.activity.myorder.EvaluationActivity;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;
import com.chengzi.app.ui.mine.activity.myorder.PriceSpreadActivity;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.ui.pay.activity.PayOrderActivity;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 普通/拼购订单
 *
 * @ClassName:MyOrderListFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/4 0004   10:26
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */

public class MyOrderListFragment extends LazyloadFragment<FragmentMyOrderListBindingImpl, MyOrderListViewModel>
        implements OrderStatusHelp.OrderTypeChange {

    //订单状态 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消（非必须）
    private int status;
    //订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）
    private int type;
    private PagingLoadHelper helper;
    private MyOrderListAdapter adapter;

    public static MyOrderListFragment newInstance(int type, int status) {
        MyOrderListFragment fragment = new MyOrderListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    private Timer timer = new Timer();
    private TimerTask timerTask;

    //倒计时
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask = null;
        }
        super.onDestroy();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_my_order_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.OrderTypeChange.class, this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            status = arguments.getInt("status", 0);
            type = arguments.getInt("type", 1);

        }
        viewModel.status = status;
        viewModel.type.set(type);
        adapter = new MyOrderListAdapter(myOrderClickListener);
        //解决闪烁问题
        adapter.setHasStableIds(true);
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

    @Override
    public void typeChange(int type) {
        if (type == this.type) {
            helper.onPulldown();
        }
    }

    //刷新订单-->
    @Override
    public void typeChange(int type, int status) {
        if (type == this.type && status == this.status) {
            helper.onPulldown();
        }
    }

    private void observe() {    // 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消
        //订单列表
        viewModel.userOrderListLive.observe(this, userOrderListBean -> {
            if (userOrderListBean != null && userOrderListBean.size() > 0) {
                helper.onComplete(userOrderListBean);
                //接下来要从服务端取到数据，加入dataList中
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                };
                try {
                    timer.schedule(timerTask, 1000, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        //取消订单
        viewModel.userOrderCancelListLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                //取消成功  //刷新-->当前type(普通/拼购)状态下的 全部/待付款/未达成/待使用
                OrderStatusHelp.refreshOrderList(type, 0);
                OrderStatusHelp.refreshOrderList(type, 1);
                OrderStatusHelp.refreshOrderList(type, 2);
                OrderStatusHelp.refreshOrderList(type, 3);
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
            case R.id.tv_payment:  //  付款
                List<String> order_id = new ArrayList<>();
                order_id.add(bean.getId());
                EventBus.getDefault().postSticky(new OrderPaymentTypeEvent(type));
                PayOrderActivity.start(getContext(), order_id);
                break;
            case R.id.tv_difference:  //  补差价
                PriceSpreadActivity.start(getContext(), bean.getId(), type);
                break;
            case R.id.tv_complaints:  //  投诉
                ComplaintsActivity.start(getContext(), bean.getId(), type);
                break;
            case R.id.tv_evaluation:  //  评价
                startActivity(new Intent(getContext(), EvaluationActivity.class)
                        .putExtra("type", type)         //刷新订单时 使用
                        .putExtra("order_id", bean.getId()));
                break;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperObservableManager.unregisterObserver(OrderStatusHelp.OrderTypeChange.class, this);
        RxBus.get().unregister(this);
    }
}

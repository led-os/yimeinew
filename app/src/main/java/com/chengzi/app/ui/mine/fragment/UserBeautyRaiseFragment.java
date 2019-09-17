package com.chengzi.app.ui.mine.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.BeautyRaiseAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentUserBeautyRaiseBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.OnBeautyPayStatusChangeEvent;
import com.chengzi.app.ui.mine.activity.order_beauty_raise.BeautyRaiseDetailsActivity;
import com.chengzi.app.ui.mine.bean.UserPlanOrderListBean;
import com.chengzi.app.ui.mine.viewmodel.UserBeautyRaiseViewModel;
import com.chengzi.app.ui.pay.activity.PayActivity;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的美人筹
 * 美人筹Fragment
 * ① 普通用户美人筹
 * ② 医院美人筹
 *
 * @ClassName:UserBeautyRaiseFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/3 0003   09:54
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */

public class UserBeautyRaiseFragment extends LazyloadFragment<FragmentUserBeautyRaiseBindingImpl, UserBeautyRaiseViewModel>
        implements OrderStatusHelp.BeautyOrderTypeChange {
    //-1全部 0待付款 1未达成 2待使用 3已完成 4已取消
    private int status = -1;
    // 1-进行中，2-待使用，3-已完成
    private int h_status = -1;
    private PagingLoadHelper helper;
    private BeautyRaiseAdapter adapter;

    private int identity = AccountHelper.getIdentity();

    public static UserBeautyRaiseFragment newInstance(int status) {
        UserBeautyRaiseFragment fragment = new UserBeautyRaiseFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    public static UserBeautyRaiseFragment newInstance1(int h_status) {
        UserBeautyRaiseFragment fragment = new UserBeautyRaiseFragment();
        Bundle args = new Bundle();
        args.putInt("h_status", h_status);
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
        return R.layout.fragment_user_beauty_raise;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        Bundle arguments = getArguments();
        if (arguments != null) {
            status = arguments.getInt("status", -1);
            if (status != -1)
                viewModel.status = status;
            h_status = arguments.getInt("h_status", -1);
            if (h_status != -1)
                viewModel.h_status = h_status;
        }
        //注册观察者-->刷新订单
        SuperObservableManager.registerObserver(OrderStatusHelp.BeautyOrderTypeChange.class, this);

        initRecyclerView();

        observe();
    }

    @Subscribe
    public void onPayStatusChange(OnBeautyPayStatusChangeEvent event) {
        helper.start();
    }


    private void initRecyclerView() {
        adapter = new BeautyRaiseAdapter(beautyRaiseClickListener);
        binding.swipeRefreshView.setAdapter(adapter);
//        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            BeautyRaiseDetailsActivity.start(getContext(), adapter.getData().get(position).getId(), adapter.getData().get(position).getPlan_id(), AccountHelper.getIdentity(), 0);
        });
    }

    private void observe() {
        //订单列表
        viewModel.userPlanOrderListLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
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
        //普通用户取消订单
        viewModel.userPlanOrderCancelLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                //-1全部 0待付款 1未达成 2待使用 3已完成 4已取消
                //刷新  -1 0 1 2 3
                OrderStatusHelp.refreshBeautyOrderList(-1);
                OrderStatusHelp.refreshBeautyOrderList(0);
                OrderStatusHelp.refreshBeautyOrderList(1);
                OrderStatusHelp.refreshBeautyOrderList(2);
                OrderStatusHelp.refreshBeautyOrderList(3);
            }
        });
        //机构取消参与美人筹
        viewModel.cancelJoinToPlanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                //订单状态  1-进行中，2-待使用，3-已完成
                //刷新  1
                OrderStatusHelp.refreshBeautyOrderList(1);
            }
        });
        //机构确认使用
        viewModel.orderConfirmUseLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                //订单状态  1-进行中，2-待使用，3-已完成
                OrderStatusHelp.refreshBeautyOrderList(2);
                OrderStatusHelp.refreshBeautyOrderList(3);
            } else {
                dismissLoading();
            }
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    private ClickEventHandler<UserPlanOrderListBean> beautyRaiseClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_cancel:  //  取消订单 /  取消参与
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    new MessageDialogBuilder(getContext())
                            .setMessage("确认取消订单？")
                            .setSureListener(v -> {
                                        showLoading(Sys.LOADING);
                                        viewModel.userPlanOrderCancel(bean.getId());
                                    }
                            )
                            .show();
                } else {
                    new MessageDialogBuilder(getContext())
                            .setMessage("确认取消参与？")
                            .setSureListener(v -> {
                                        showLoading(Sys.LOADING);
                                        viewModel.cancelJoinToPlan(bean.getPlan_id());
                                    }
                            )
                            .show();
                }
                break;
            case R.id.tv_invited:  //  付款 / 确认使用
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    PayActivity.start(getContext(), bean.getId(), 2);
                } else {
                    new MessageDialogBuilder(getContext())
                            .setMessage("确认用户已使用？")
                            .setSureListener(v -> {
                                        showLoading(Sys.LOADING);
                                        viewModel.orderConfirmUse(bean.getId(), "2");
                                    }
                            )
                            .show();
                }
                break;
        }
    };

    @Override
    public void typeChange(int status) {
        if (identity == Sys.TYPE_USER && status == this.status) {
            helper.onPulldown();
        } else if (identity == Sys.TYPE_HOSPITAL && status == this.h_status) {
            helper.onPulldown();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SuperObservableManager.unregisterObserver(OrderStatusHelp.BeautyOrderTypeChange.class, this);
        EventBus.getDefault().unregister(this);
    }
}
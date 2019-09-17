package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.nevermore.oceans.ob.Dispatcher;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PayCarOrderAdapter;
import com.chengzi.app.databinding.ActivityPayOrderSuccessBinding;
import com.chengzi.app.event.UserOrderEvent;
import com.chengzi.app.event.UserOrderIdEvent;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;
import com.chengzi.app.utils.OrderStatusHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 多订单支付成功页面
 *
 * @ClassName:PayOrderSuccessActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/6/6 0006   10:43
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/6/6 0006 handongkeji All rights reserved.
 */
public class PayOrderSuccessActivity extends BaseActivity<ActivityPayOrderSuccessBinding, BaseViewModel> {

    private PayCarOrderAdapter adapter;
    private List<String> orderIds;
    private int type;   //  1-普通订单和秒杀订单 2-拼购订单

    public static void start(Context context) {
        Intent intent = new Intent(context, PayOrderSuccessActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_order_success;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initRecyclerView();

        EventBus.getDefault().register(this);

        mBinding.tvSure.setOnClickListener(v -> {
            if (orderIds == null || orderIds.isEmpty()) {
                finish();
                return;
            }
            if (orderIds.size() > 1) {
                SuperObservableManager.notify(OrderStatusHelp.OrderTypeChange.class, new Dispatcher<OrderStatusHelp.OrderTypeChange>() {
                    @Override
                    public void call(OrderStatusHelp.OrderTypeChange orderTypeChange) {
                        orderTypeChange.typeChange(type);
                    }
                });
            } else {
                String orderId = orderIds.get(0);
                MyOrderListDetailsActivity.start(this, orderId, type, 0);
            }
            finish();
        });
    }

    private void initRecyclerView() {
        adapter = new PayCarOrderAdapter();
        adapter.setPaySuccess(1);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Subscribe(sticky = true)
    public void getPaymentData(UserOrderEvent event) {
        adapter.setNewData(event.getList());
    }

    @Subscribe(sticky = true)
    public void getOrderIds(UserOrderIdEvent event) {
        this.orderIds = event.getList();
    }

    @Subscribe(sticky = true)
    public void getOrderType(Integer type) {
        this.type = type;
    }
}

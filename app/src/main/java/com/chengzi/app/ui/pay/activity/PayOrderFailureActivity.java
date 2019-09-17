package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PayCarOrderAdapter;
import com.chengzi.app.databinding.ActivityPayOrderFailureBinding;
import com.chengzi.app.event.UserOrderEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * @ClassName:PayOrderFailureActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/6/11 0011   13:48
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/6/11 0011 handongkeji All rights reserved.
 */
public class PayOrderFailureActivity extends BaseActivity<ActivityPayOrderFailureBinding, BaseViewModel> implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, PayOrderFailureActivity.class);
        context.startActivity(intent);
    }

    private PayCarOrderAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_order_failure;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBinding.setListener(this);
        initRecyclerView();

        EventBus.getDefault().register(this);

    }

    private void initRecyclerView() {
        adapter = new PayCarOrderAdapter();
        adapter.setPaySuccess(2);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Subscribe(sticky = true)
    public void getPaymentData(UserOrderEvent event) {
        adapter.setNewData(event.getList());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                onBackPressed();
                break;
        }
    }
}

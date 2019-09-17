package com.chengzi.app.ui.pay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPaySuccessBindingImpl;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.utils.DateUtils;
import com.chengzi.app.utils.OrderStatusHelp;

/**
 * 支付成功
 *
 * @ClassName:PaySuccessActivity
 * @PackageName:com.yimei.app.ui.pay.activity
 * @Create On 2019/4/2 0002   14:14
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class PaySuccessActivity extends BaseActivity<ActivityPaySuccessBindingImpl, BaseViewModel> {

    private SpreadsOrderBean spreadsOrderBean;
    private int type;

    public static void start(Context context, SpreadsOrderBean spreadsOrderBean, int type) {
        context.startActivity(new Intent(context, PaySuccessActivity.class)
                .putExtra("spreadsOrderBean", spreadsOrderBean)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        spreadsOrderBean = (SpreadsOrderBean) getIntent().getSerializableExtra("spreadsOrderBean");
        //订单编号 支付金额 支付时间
        mBinding.elSpreadsNumber.setContent(spreadsOrderBean.getSpreads_number());
        mBinding.elSpreads.setContent("¥" + spreadsOrderBean.getSpreads());
        mBinding.elCreateTime.setContent(DateUtils.dataTime(spreadsOrderBean.getCreate_time(), "yyyy年MM月dd日 hh:mm"));

        type = getIntent().getIntExtra("type", 1);

        //查看订单
        mBinding.tvSure.setOnClickListener(v -> {
            ////刷新订单->(全部订单0 和待使用3)
            OrderStatusHelp.refreshOrderList(type, 0);
            OrderStatusHelp.refreshOrderList(type, 3);
            finish();
            MyOrderListDetailsActivity.start(this, spreadsOrderBean.getOrder_id(), type, 0);
        });
    }
}
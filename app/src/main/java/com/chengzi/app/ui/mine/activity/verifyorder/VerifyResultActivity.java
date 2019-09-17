package com.chengzi.app.ui.mine.activity.verifyorder;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityVerifyResultBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderListDetailsActivity;
import com.chengzi.app.ui.mine.activity.order_beauty_raise.BeautyRaiseDetailsActivity;
import com.chengzi.app.ui.mine.bean.VerificationOrderBean;
import com.chengzi.app.ui.mine.viewmodel.UserBeautyRaiseViewModel;

/**
 * 验证结果
 *
 * @ClassName:VerifyResultActivity
 * @PackageName:com.yimei.app.ui.mine.activity.verifyorder
 * @Create On 2019/4/12 0012   14:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */

public class VerifyResultActivity extends BaseActivity<ActivityVerifyResultBindingImpl, UserBeautyRaiseViewModel> {

    private VerificationOrderBean data;
    private String type;

    public static VerifyResultActivity activity;
    private String order_id;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_verify_result;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        activity = this;
        mBinding.setListener(clickListener);
        data = (VerificationOrderBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            mBinding.setBean(data);
            //private String type;            // 订单类型，1-普通，拼购，秒杀 2-美人筹
            //    private String goods_id;        // 商品id，美人筹订单，该字段为null
            //    private String goods_image;     // 商品封面，美人筹订单，该字段为空，使用给的默认图
            type = data.getType();
            order_id = data.getOrder_id();
            if (type.equals("1"))
//                mBinding.setUrl(data.getGoods_image());
                ImageLoader.loadImage(mBinding.ivOrderPic,data.getGoods_image());
            else
                ImageLoader.loadImage(mBinding.ivOrderPic, R.drawable.meirenchou);
            //支付钱 和 补差价
            mBinding.tvMoney.setText("（共支付" + data.getAmount_total() + "元，其中补差价" + data.getAmount_spreads() + "元）");
        }

        //机构确认使用
        mViewModel.orderConfirmUseLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                //订单状态  1-进行中，2-待使用，3-已完成
                //from=1 ->验证订单
//                BeautyRaiseDetailsActivity.start(this, order_id, "", Sys.TYPE_HOSPITAL, 1);
                //详情 区分 普通订单和美人筹
                if (type.equals("1")) {     //普通
                    MyOrderListDetailsActivity.start(this, order_id, 3, 1);
                } else {
                    BeautyRaiseDetailsActivity.start(this, order_id, "", Sys.TYPE_HOSPITAL, 1);
                }
                finish();
            } else {
                dismissLoading();
            }
        });
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_order_layout:  //  订单详情
                //详情 区分 普通订单和美人筹
                if (type.equals("1")) {     //普通
                    MyOrderListDetailsActivity.start(this, order_id, 3, 1);
                } else {
                    BeautyRaiseDetailsActivity.start(this, order_id, "", Sys.TYPE_HOSPITAL, 1);
                }
                break;
            case R.id.tv_evaluation:  //  确认使用
                new MessageDialogBuilder(this)
                        .setMessage("确认用户已使用？")
                        .setSureListener(v -> {
                            showLoading(Sys.LOADING);
                            // 订单类型，1-普通，拼购，秒杀 2-美人筹
                            mViewModel.orderConfirmUse(order_id, type);
                        })
                        .show();
                break;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activity != null)
            activity = null;
    }
}
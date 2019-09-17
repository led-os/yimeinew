package com.chengzi.app.ui.mine.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityOpenVipBindingImpl;
import com.chengzi.app.databinding.ItemOpenVipLayoutBindingImpl;
import com.chengzi.app.event.BuyVipEvent;
import com.chengzi.app.ui.mine.bean.OpenVipBean;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;
import com.chengzi.app.ui.pay.activity.PayActivity;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.lzy.imagepicker.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 开通vip
 *
 * @ClassName:OpenVipActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/2 0002   10:28
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class OpenVipActivity extends BaseActivity<ActivityOpenVipBindingImpl, MyVipViewModel> implements View.OnClickListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, OpenVipActivity.class);
        context.startActivity(intent);
    }


    ///选中的VIP套餐position
    private int choosePosition = 0;
    //选中的套餐id
    private String vip_id;
    private OpenVipAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        mBinding.setListener(this::onClick);
        showLoading(Sys.LOADING);
        if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
            //设置状态栏全透明
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.statusBarLightMode(this);
            //普通用户
            mBinding.llVipMine.setVisibility(View.VISIBLE);
            mBinding.llVipOther.setVisibility(View.GONE);
            mBinding.rvTabLayout.setLayoutManager(new GridLayoutManager(this, 3));
            adapter = new OpenVipAdapter();
            mBinding.rvTabLayout.setAdapter(adapter);

            adapter.setOnItemClickListener((adapter1, view, position) -> {
                choosePosition = position;
                adapter.notifyDataSetChanged();
                OpenVipBean openVipBean = adapter.getData().get(position);
                String money = openVipBean.getPrice();
                mBinding.tvMoney.setText(money + "元");
                vip_id = openVipBean.getId();
//                toast("选中的id：" + id + "\n" + "选中的name：" + openVipBean.getProduct_name());
            });
        } else {    //其他
            mBinding.llVipMine.setVisibility(View.GONE);
            mBinding.llVipOther.setVisibility(View.VISIBLE);
        }
        mViewModel.getVipPrice();
        mViewModel.getVipPriceLiveData.observe(this, listResponseBean -> {
            dismissLoading();
            if (listResponseBean != null && listResponseBean.getData() != null && listResponseBean.getData().size() > 0) {
                List<OpenVipBean> data = listResponseBean.getData();
                OpenVipBean openVipBean = data.get(0);
                if (AccountHelper.getIdentity() == 1) {
                    adapter.setNewData(data);
                } else {
                    mBinding.setBean(openVipBean);
                }
                vip_id = openVipBean.getId();
                String money = openVipBean.getPrice();
                mBinding.tvMoney.setText(money + "元");
            }
        });
        //
//        mViewModel.getOrderLiveData.observe(this, getVipOrderBean -> {
//            dismissLoading();
//            if (getVipOrderBean != null && getVipOrderBean.getData() != null) {
//                //到支付前页面
//                PayActivity.start(this, getVipOrderBean.getData());
//            }
//        });
    }

    @Subscribe
    public void onBuyVipSuccess(BuyVipEvent event){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
            case R.id.iv_finish1:
                finish();
                break;
            case R.id.tv_open:      ///开通VIP
            case R.id.tv_open1:
//                showLoading(Sys.LOADING);
//                mViewModel.getOrder(vip_id);
                PayActivity.start(this, vip_id, 1);
                break;
        }
    }

    private class OpenVipAdapter extends BaseQuickAdapter<OpenVipBean, BaseViewHolder> {

        public OpenVipAdapter() {
            super(R.layout.item_open_vip_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, OpenVipBean item) {
            ItemOpenVipLayoutBindingImpl layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
            if (choosePosition == helper.getAdapterPosition()) {
                helper.getView(R.id.ll_item).setSelected(true);
                helper.setTextColor(R.id.tv_day, Color.parseColor("#FFFFFF"))
                        .setTextColor(R.id.tv_money, Color.parseColor("#FFFFFF"));
            } else {
                helper.getView(R.id.ll_item).setSelected(false);
                helper.setTextColor(R.id.tv_day, Color.parseColor("#333333"))
                        .setTextColor(R.id.tv_money, Color.parseColor("#EB2525"));
            }
        }
    }
}

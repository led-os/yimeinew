package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemMyOrderListLayoutBinding;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.utils.TimeUtils;

/**
 * 普通/拼购订单 adapter
 *
 * @ClassName:BeautyRaiseAdapter
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/4/3 0003   10:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
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
        binding.tvAmountSpreads.setVisibility(!TextUtils.isEmpty(item.getAmount_spreads()) && !item.getAmount_spreads().equals("0") ? View.VISIBLE : View.GONE);

        //// 订单类型 1-普通订单 2-拼购订单 3-秒杀订单
        String type = item.getType();
//        status;       订单状态 1-待付款 2-未达成 3-待使用 4-待评价 5-已完成 6-已取消
//        isComplaint;  是否已投诉 0-未投诉，1-已投诉(待评价时 使用)
        String status = item.getStatus();
        binding.tvOrderType.setText(item.getStatus_name());
        String isComplaint = item.getIsComplaint();

//        显示时间戳倒计时-->拼购订单中 待付款和未达成显示
        if (TextUtils.equals(type,"2")) {
            if (TextUtils.equals(status,"1") || TextUtils.equals(status,"2")) {
                binding.clTime.setVisibility(View.VISIBLE);
                String remain_time = item.getRemain_time();
                //天 小时 分钟
                binding.tvTime.setText(TimeUtils.formatDayHourMinuteTime(remain_time));
                //倒计时-1
                String nowTime = String.valueOf(Long.parseLong(remain_time) - 1);
                item.setRemain_time(nowTime);
            } else
                binding.clTime.setVisibility(View.GONE);
        } else {
            binding.clTime.setVisibility(View.GONE);
        }
//        //设置显示的button
        if (TextUtils.equals(status,"1")) {
            //带付款 -->显示 取消订单+付款
            setShowStatusBtn(true, true, false, false, false, binding);
        } else if (TextUtils.equals(status,"2")) {
            //未达成 -->显示 取消订单
            setShowStatusBtn(true, false, false, false, false, binding);
        } else if (TextUtils.equals(status,"3")) {
            //待使用 -->显示 补差价+取消订单
            setShowStatusBtn(true, false, true, false, false, binding);
        } else if (TextUtils.equals(status,"4")) {
            //待评价 -->显示 投诉+评价
            boolean complaints = false;
            if (TextUtils.equals(isComplaint,"0")) {
                //显示投诉
                complaints = true;
            } else {
                //隐藏投诉
                complaints = false;
            }
            setShowStatusBtn(false, false, false, complaints, true, binding);
        } else {   //status.equals("5")&&status.equals("6")
            //已完成/已取消-->隐藏
            binding.llShowStatus.setVisibility(View.GONE);
        }
    }

    /**
     * @param cancel     取消
     * @param payment    付款
     * @param difference 补差价
     * @param complaints 投诉
     * @param evaluation 评价
     * @param binding
     */
    private void setShowStatusBtn(boolean cancel, boolean payment, boolean difference,
                                  boolean complaints, boolean evaluation, ItemMyOrderListLayoutBinding binding) {
        binding.llShowStatus.setVisibility(View.VISIBLE);
        binding.tvCancel.setVisibility(cancel ? View.VISIBLE : View.GONE);
        binding.tvPayment.setVisibility(payment ? View.VISIBLE : View.GONE);
        binding.tvDifference.setVisibility(difference ? View.VISIBLE : View.GONE);
        binding.tvComplaints.setVisibility(complaints ? View.VISIBLE : View.GONE);
        binding.tvEvaluation.setVisibility(evaluation ? View.VISIBLE : View.GONE);
    }
}
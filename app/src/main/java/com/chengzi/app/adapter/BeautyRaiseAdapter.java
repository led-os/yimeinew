package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ItemBeautyRaiseLayoutBinding;
import com.chengzi.app.ui.mine.bean.UserPlanOrderListBean;

/**
 * 美人筹 adapter
 *
 * @ClassName:BeautyRaiseAdapte
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/4/3 0003   10:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class BeautyRaiseAdapter extends BaseQuickAdapter<UserPlanOrderListBean, BaseViewHolder> {

    private ClickEventHandler<UserPlanOrderListBean> clickEventHandler;

    public BeautyRaiseAdapter(ClickEventHandler<UserPlanOrderListBean> clickEventHandler) {
        super(R.layout.item_beauty_raise_layout);
        this.clickEventHandler = clickEventHandler;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserPlanOrderListBean item) {
        ItemBeautyRaiseLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        UserPlanOrderListBean.PlanDetailsEntity plan_details = item.getPlan_details();
        if (plan_details != null)
            binding.setBean(item);
        binding.setListener(clickEventHandler);
        binding.executePendingBindings();
        if (plan_details != null) {
            //设置进度
            String plan = plan_details.getPlan();
            if (plan.contains("%")) {
                try {
                    binding.customProgressBar.setProgress(Integer.parseInt(deleteString(plan, '%')));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    binding.customProgressBar.setProgress(0);
                }
            }
            //?人参与 进度条 百分比 -->取消的时候不显示
            //0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
            //        1-进行中 2-待使用，3-已完成
            String status = plan_details.getStatus();
            binding.tvOrderPersonNum.setVisibility(status.equals("4") ? View.GONE : View.VISIBLE);
            binding.customProgressBar.setVisibility(status.equals("4") ? View.GONE : View.VISIBLE);
            binding.tvPlan.setVisibility(status.equals("4") ? View.GONE : View.VISIBLE);
            //剩余时间->已完成/已取消
//            binding.llTime.setVisibility(status.equals("3") ? View.GONE : View.VISIBLE);
//
//            if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL && status.equals("2")) {
//                binding.llTime.setVisibility(View.GONE);
//            }
            //剩余时间/状态/单价/按钮
            binding.llOther.setVisibility(status.equals("4") ? View.GONE : View.VISIBLE);
            //显示 按钮
            //① 0-待付款-->取消订单 立即应邀
            //② 1-未达成-->取消订单            医院 -->取消参与
            //  2-待使用-->取消订单            医院 -->确认使用
            if (status.equals("0") || status.equals("1") || status.equals("2")) {
                binding.llShowButton.setVisibility(View.VISIBLE);
                binding.tvCancel.setVisibility(View.VISIBLE);
                if (status.equals("0"))
                    binding.tvInvited.setVisibility(View.VISIBLE);
                else {
                    binding.tvInvited.setVisibility(View.GONE);
                    if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                        binding.tvCancel.setText("取消参与");
                        if (status.equals("2")) {
//                            binding.tvInvited.setVisibility(View.VISIBLE);
//                            binding.tvCancel.setVisibility(View.GONE);
//                            binding.tvInvited.setText("确认使用");
                            binding.llShowButton.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                binding.llShowButton.setVisibility(View.GONE);
            }

        }
    }

    public static String deleteString(String str, char delChar) {
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != delChar) {
                stringBuffer.append(str.charAt(i));
            }
        }
        return stringBuffer.toString();
    }
}
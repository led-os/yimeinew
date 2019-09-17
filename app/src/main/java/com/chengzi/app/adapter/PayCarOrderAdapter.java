package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemPayCarOrderLayoutBinding;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;

public class PayCarOrderAdapter extends BaseQuickAdapter<UserOrderListBean, BaseViewHolder> {

    private int paySuccess;

    public PayCarOrderAdapter() {
        super(R.layout.item_pay_car_order_layout);
    }

    public void setPaySuccess(int paySuccess) {
        this.paySuccess = paySuccess;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrderListBean item) {
        ItemPayCarOrderLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.layoutPayTime.setVisibility(paySuccess == 1?View.VISIBLE:View.GONE);
        binding.layoutFailReason.setVisibility(paySuccess == 2?View.VISIBLE:View.GONE);

        binding.layoutDoctor.setVisibility(TextUtils.isEmpty(item.getDoctor_id())|| TextUtils.equals(item.getDoctor_id(),"0")?View.GONE:View.VISIBLE);
        binding.layoutCounselor.setVisibility(TextUtils.isEmpty(item.getCounselling_id()) || TextUtils.equals(item.getCounselling_id(),"0")?View.GONE:View.VISIBLE);
        //是否显示VIP iv_doctor_vip
        if (item.getDoctor_info() != null) {
            binding.ivDoctorVip.setVisibility(item.getDoctor_info().getIs_VIP().equals("1") ? View.VISIBLE : View.GONE);
        }
        if (item.getCounselling_info() != null) {
            binding.ivCounselorVip.setVisibility(item.getCounselling_info().getIs_VIP().equals("1") ? View.VISIBLE : View.GONE);
        }
        binding.executePendingBindings();
    }
}

package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemVipDoctorLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.vip.bean.VipUserInfoBean;

public class VipDoctorAdapter extends BaseQuickAdapter<VipUserInfoBean, BaseViewHolder>
        implements ClickEventHandler<VipUserInfoBean> {

    public VipDoctorAdapter() {
        super(R.layout.item_vip_doctor_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipUserInfoBean item) {
        ItemVipDoctorLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setListener(this);
        binding.executePendingBindings();
    }

    @Override
    public void handleClick(View view, VipUserInfoBean bean) {
        if (bean.getType() == 2) {
            DoctorHomePageActivity.startDoctor(view.getContext(), bean.getId());

        } else if (bean.getType() == 3) {
            DoctorHomePageActivity.startCounselor(view.getContext(), bean.getId());
        }
    }
}

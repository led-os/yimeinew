package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemVipOrgLayoutBinding;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.vip.bean.VipUserInfoBean;

public class VipOrgAdapter extends BaseQuickAdapter<VipUserInfoBean, BaseViewHolder>
        implements ClickEventHandler<VipUserInfoBean> {

    public VipOrgAdapter() {
        super(R.layout.item_vip_org_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipUserInfoBean item) {
        ItemVipOrgLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setListener(this);
        binding.executePendingBindings();
    }

    @Override
    public void handleClick(View view, VipUserInfoBean bean) {
        HospitalHomePageActivity.start(view.getContext(), bean.getId());
    }
}

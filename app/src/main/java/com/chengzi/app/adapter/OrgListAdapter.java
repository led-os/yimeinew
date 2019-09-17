package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemOrganizationListLayoutBinding;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;

public class OrgListAdapter extends BaseQuickAdapter<HospitalBean,BaseViewHolder>
        implements ClickEventHandler<HospitalBean> {

    public OrgListAdapter() {
        super(R.layout.item_organization_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalBean item) {
        ItemOrganizationListLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setListener(this);
        binding.executePendingBindings();
    }

    @Override
    public void handleClick(View view, HospitalBean bean) {
        HospitalHomePageActivity.start(view.getContext(),bean.getId());
    }
}

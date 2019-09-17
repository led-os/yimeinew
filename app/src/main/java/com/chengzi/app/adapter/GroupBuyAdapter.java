package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemGroupBuyingLayoutBinding;
import com.chengzi.app.ui.goods.bean.SpellBean;

public class GroupBuyAdapter extends BaseQuickAdapter<SpellBean, BaseViewHolder> {

    public GroupBuyAdapter() {
        super(R.layout.item_group_buying_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SpellBean item) {
        ItemGroupBuyingLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        helper.addOnClickListener(R.id.btn_join_group_buy);
        binding.countDownTextView.setRemainTime(item.getRemain_time());
        binding.executePendingBindings();
    }

}

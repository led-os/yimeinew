package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemPeopleSaysLayoutBinding;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;

public class PeopleSayListAdapter extends BaseQuickAdapter<PeopleSayQuestionBean, BaseViewHolder> {

    public PeopleSayListAdapter() {
        super(R.layout.item_people_says_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleSayQuestionBean item) {

        ItemPeopleSaysLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
        layoutBinding.setBean(item);
        layoutBinding.executePendingBindings();

    }
}

package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemGoodPeopleSayLayoutBinding;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;

public class PeopleSayAdapter extends BaseQuickAdapter<PeopleSayQuestionBean, BaseViewHolder> {


    public PeopleSayAdapter() {
        super(R.layout.item_good_people_say_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleSayQuestionBean item) {
        ItemGoodPeopleSayLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
        layoutBinding.setBean(item);
        layoutBinding.executePendingBindings();
    }

}

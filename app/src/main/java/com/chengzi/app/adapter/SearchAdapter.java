package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemSearchLayoutBinding;
import com.chengzi.app.ui.search.bean.SearchBean;

public class SearchAdapter extends BaseQuickAdapter<SearchBean, BaseViewHolder> {

    public SearchAdapter() {
        super(R.layout.item_search_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean item) {
        ItemSearchLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
        layoutBinding.textView.setText(item.getKey());
    }
}

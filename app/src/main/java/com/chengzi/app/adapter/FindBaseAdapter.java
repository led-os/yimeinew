package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemFindBaseLayoutBinding;
import com.chengzi.app.ui.find.bean.FindBaseBean;

import java.util.List;

public class FindBaseAdapter<T> extends BaseQuickAdapter<FindBaseBean<T>, BaseViewHolder> {

    private boolean isVip;
    private ItemAdapterFactory factory;

    public void setFactory(ItemAdapterFactory factory) {
        this.factory = factory;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public FindBaseAdapter() {
        super(R.layout.item_find_base_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindBaseBean<T> item) {
        ItemFindBaseLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setIsVip(isVip);
        binding.textViewClassify.setText(item.getName());
        factory.setRecyclerPool(binding.recyclerView);
        binding.recyclerView.swapAdapter(factory.createAdapter(item.getData()), false);
    }

    public interface ItemAdapterFactory<T> {
        BaseQuickAdapter createAdapter(List<T> list);

        void setRecyclerPool(RecyclerView recyclerView);
    }

}

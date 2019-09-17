package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemFindBaseLayoutBinding;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;

import java.util.List;

public class RecommandAdapter extends BaseQuickAdapter<HomeRecommandBean,BaseViewHolder> {

    private ItemAdapterFactory factory;

    public void setFactory(ItemAdapterFactory factory) {
        this.factory = factory;
    }

    public RecommandAdapter() {
        super(R.layout.item_find_base_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeRecommandBean item) {
        ItemFindBaseLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

        binding.textViewClassify.setText(item.getName());

        factory.setRecyclerPool(binding.recyclerView);
        binding.recyclerView.swapAdapter(factory.createAdapter(item.getGoodsLists()), false);
    }

    public interface ItemAdapterFactory {
        BaseQuickAdapter createAdapter(List<GoodBean> list);

        void setRecyclerPool(RecyclerView recyclerView);
    }
}

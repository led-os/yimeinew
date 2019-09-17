package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemImageLayoutBinding;

/**
 * 9图adapter
 *
 * @ClassName:NineImageAdapter
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/4/11 0011   14:20
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class NineImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NineImageAdapter() {
        super(R.layout.item_image_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ItemImageLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setUrl(item);
        binding.executePendingBindings();
    }
}
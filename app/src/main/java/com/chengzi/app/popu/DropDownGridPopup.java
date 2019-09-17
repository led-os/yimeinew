package com.chengzi.app.popu;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemDropdownGridLayoutBinding;

public class DropDownGridPopup extends DropDownPopup {

    public DropDownGridPopup(Context context) {
        super(context);
    }

    @Override
    protected void initalizeRecyclerView() {
        binding.recyclerView.setPadding(SizeUtils.dp2px(10), binding.recyclerView.getPaddingTop(),
                binding.recyclerView.getPaddingRight(), binding.recyclerView.getPaddingBottom());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new DropDownGridAdapter();
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            PopupBean o = data.get(position);
            if (listener != null) {
                listener.onSelect(o);
            }
            dismiss();
        });
    }

    private class DropDownGridAdapter extends BaseQuickAdapter<PopupBean, BaseViewHolder> {

        public DropDownGridAdapter() {
            super(R.layout.item_dropdown_grid_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, PopupBean item) {
            ItemDropdownGridLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.textView.setText(item.getName());
        }
    }
}

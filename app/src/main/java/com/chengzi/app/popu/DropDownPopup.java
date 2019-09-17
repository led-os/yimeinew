package com.chengzi.app.popu;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DropDownPopupLayoutBinding;
import com.chengzi.app.databinding.ItemDropDownLayoutBinding;

import java.util.List;

public class DropDownPopup extends PopupWindow {

    protected Context context;

    protected List<? extends PopupBean> data;
    protected final DropDownPopupLayoutBinding binding;
    protected BaseQuickAdapter adapter;

    public DropDownPopup(Context context) {
        super(context);
        this.context = context;

        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x0000000));
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.drop_down_popup_layout, null, false);
        binding.getRoot().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(binding.getRoot());

        binding.getRoot().setOnTouchListener((v, event) -> {
            dismiss();
            return true;
        });

        initalizeRecyclerView();
    }

    protected void initalizeRecyclerView() {
        adapter = new DropDownAdapter();
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PopupBean o = data.get(position);
                if (listener != null) {
                    listener.onSelect(o);
                }
                dismiss();
            }
        });
    }

    protected OnSelectListener listener;

    public interface OnSelectListener {
        void onSelect(PopupBean bean);
    }

    public void setSureListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public void setData(List<? extends PopupBean> data) {
        this.data = data;
        adapter.setNewData(data);
    }

    @Override
    public void showAsDropDown(View anchor) {
        int[] loc = new int[2];
        anchor.getLocationOnScreen(loc);
        int screenHeight = ScreenUtils.getScreenHeight();
        int height = screenHeight - loc[1] - anchor.getHeight();
        setHeight(height);
        super.showAsDropDown(anchor);
    }

    private class DropDownAdapter extends BaseQuickAdapter<PopupBean, BaseViewHolder> {

        public DropDownAdapter() {
            super(R.layout.item_drop_down_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, PopupBean item) {
            ItemDropDownLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.textView.setText(item.getName());
        }
    }

    public static class Item implements PopupBean{

        private int id;
        private String name;
        private String param; //  接口参数 key
        private String value; //  接口参数 value

        public Item(int id, String name, String param, String value) {
            this.id = id;
            this.name = name;
            this.param = param;
            this.value = value;
        }

        public String getId() {
            return String.valueOf(id);
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public interface PopupBean {

        String getName();

        String getParam();

        String getValue();

        String getId();

    }
}

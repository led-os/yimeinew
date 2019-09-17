package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogSelectListBinding;
import com.handong.framework.dialog.BottomDialog;

import java.util.List;

public class SingleChoiceDialog extends BottomDialog {

    private DialogSelectListBinding binding;
    private final SingleChoiceDialogAdapter adapter = new SingleChoiceDialogAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        setShowCancle(true);
        setTvCancle("取消");

        adapter.setOnItemChildClickListener((adapter1, v, position) -> {
            Choice item = adapter.getItem(position);
            String str = item.getName();
            String value = item.getValue();
            adapter.setChooseString(str);
            adapter.notifyDataSetChanged();
            if (listener != null) {
                listener.onSelect(item, str + "", value, position);
            }
            dismiss();
        });

        binding.tvCancel.setOnClickListener(v -> dismiss());
    }


    public void setData(List<Choice> list, String choiceName) {
        adapter.setChooseString(choiceName);
        adapter.setNewData(list);
    }

    private OnSelectListener listener;

    public interface OnSelectListener {
        void onSelect(Choice choice, String name, String value, int position);
    }

    public SingleChoiceDialog setSureListener(OnSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public SingleChoiceDialog setTvCancle(String cancle) {
        binding.tvCancel.setText(cancle);
        binding.tvCancel.setVisibility(View.VISIBLE);
        return this;
    }

    //是否显示 默认显示  false隐藏
    public SingleChoiceDialog setShowCancle(boolean isHint) {
        binding.tvCancel.setVisibility(!isHint ? View.GONE : View.VISIBLE);
        return this;
    }


    //是否显示 title
    public SingleChoiceDialog setTitle(String title) {
        binding.tvTitle.setText(title);
        binding.tvTitle.setVisibility(View.VISIBLE);
        binding.view.setVisibility(View.VISIBLE);
        return this;
    }


    public class SingleChoiceDialogAdapter extends BaseQuickAdapter<Choice, BaseViewHolder> {

        public SingleChoiceDialogAdapter() {
            super(R.layout.item_dialog_selectlist_layout);
        }

        private String chooseString;

        public void setChooseString(String chooseString) {
            this.chooseString = chooseString;
        }

        @Override
        protected void convert(BaseViewHolder helper, Choice item) {

            helper.setText(R.id.tv_name, item.getName());
            helper.addOnClickListener(R.id.tv_name);
            TextView view = helper.getView(R.id.tv_name);
            if (TextUtils.equals(item.getName(), chooseString)) {
                view.setTextColor(Color.parseColor("#FF3A2D"));
            } else {
                view.setTextColor(Color.parseColor("#FF3D3D3D"));
            }
        }
    }

    public interface Choice {
        String getName();

        String getValue();
    }

    public static class ChoiceBean implements Choice {

        private String name;
        private String value;

        public ChoiceBean(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public ChoiceBean() {
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}

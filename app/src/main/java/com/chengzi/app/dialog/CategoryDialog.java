package com.chengzi.app.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogCategoryLayoutBinding;
import com.chengzi.app.databinding.ItemAppNavigatorLayoutBinding;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.AnswerQuestionViewModel;

public class CategoryDialog extends BottomDialog {

    private DialogCategoryLayoutBinding mBinding;
    private AnswerQuestionViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_category_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AnswerQuestionViewModel.class);

        mBinding.title.setText(R.string.select_question_type);

        CategoryAdapter adapter = new CategoryAdapter();
        mBinding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            CategoryItem item = adapter.getItem(position);
            if (listener != null) {
                listener.onCategorySelected(item);
            }
            dismissAllowingStateLoss();
        });

        viewModel.categoryLive1.observe(this, categoryItems -> {
            adapter.setNewData(categoryItems);
            mBinding.viewSwitcher.setDisplayedChild(1);
        });
        viewModel.categoryList1();
    }

    private static class CategoryAdapter extends BaseQuickAdapter<CategoryItem, BaseViewHolder> {

        public CategoryAdapter() {
            super(R.layout.item_app_navigator_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CategoryItem item) {
            ItemAppNavigatorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item.getTypeText());
            layoutBinding.executePendingBindings();
        }
    }

    private OnCategorySelectedListener listener;

    public void setListener(OnCategorySelectedListener listener) {
        this.listener = listener;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(CategoryItem categoryItem);
    }
}

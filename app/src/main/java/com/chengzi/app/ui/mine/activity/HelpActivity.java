package com.chengzi.app.ui.mine.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHelpBindingImpl;
import com.chengzi.app.databinding.ItemHelpLayoutBinding;
import com.chengzi.app.ui.mine.bean.RuleListBean;
import com.chengzi.app.ui.mine.viewmodel.HelpViewModel;

/**
 * 规则和帮助
 *
 * @ClassName:HelpActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/8 0008   14:14
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class HelpActivity extends BaseActivity<ActivityHelpBindingImpl, HelpViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_help;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        HelpAdapter adapter = new HelpAdapter();
        mBinding.mRecyclerView.setAdapter(adapter);
        //获取规则帮助
        showLoading(Sys.LOADING);

        mViewModel.ruleList();
        mViewModel.ruleListLiveData.observe(this, beans -> {
            dismissLoading();
            if (beans != null && beans.size() > 0) {
                mBinding.mRecyclerView.setVisibility(View.VISIBLE);
                mBinding.emptyView.setVisibility(View.GONE);
                adapter.setNewData(beans);
            } else {
                mBinding.mRecyclerView.setVisibility(View.GONE);
                mBinding.emptyView.setVisibility(View.VISIBLE);
            }
        });
        //点击进详情
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            RuleListBean ruleListBean = adapter.getData().get(position);
            HelpDetailsActivity.start(this, ruleListBean.getTitle(), ruleListBean.getContent());
        });
    }

    //帮助
    private class HelpAdapter extends BaseQuickAdapter<RuleListBean, BaseViewHolder> {

        public HelpAdapter() {
            super(R.layout.item_help_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, RuleListBean item) {
            ItemHelpLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.tvTitle.setText((helper.getAdapterPosition() + 1) + "." + item.getTitle());

            helper.addOnClickListener(R.id.tv_title);
        }
    }
}
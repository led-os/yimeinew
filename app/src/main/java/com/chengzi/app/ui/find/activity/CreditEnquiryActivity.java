package com.chengzi.app.ui.find.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCreditEnquiryBindingImpl;
import com.chengzi.app.databinding.ItemCreditEnquiryLayoutBinding;
import com.chengzi.app.ui.find.bean.CreditSearchDetailBean;
import com.chengzi.app.ui.find.viewmodel.CreditEnqiryViewModel;

/**
 * 信用查询
 *
 * @ClassName:CreditEnquiryActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/4/19 0019   16:31
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class CreditEnquiryActivity extends BaseActivity<ActivityCreditEnquiryBindingImpl, CreditEnqiryViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_credit_enquiry;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setViewModel(mViewModel);

        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CreditEnquiryAdapter adapter = new CreditEnquiryAdapter();
        mBinding.mRecyclerView.setAdapter(adapter);

        //搜索列表
        mViewModel.creditSearchListLiveData.observe(this, list -> {
            dismissLoading();
            if (list != null && list.size() > 0) {
                mBinding.mRecyclerView.setVisibility(View.VISIBLE);
                mBinding.emptyView.setVisibility(View.GONE);
                adapter.setNewData(list);
            } else {
                mBinding.mRecyclerView.setVisibility(View.GONE);
                mBinding.emptyView.setVisibility(View.VISIBLE);
            }
        });
        //用户信用信息
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            CreditSearchDetailBean creditSearchDetailBean = adapter.getData().get(position);
            int types = creditSearchDetailBean.getTypes();
            UserCreditInfoActivity.start(this, creditSearchDetailBean.getId(), types);
        });
    }

    //信用
    private class CreditEnquiryAdapter extends BaseQuickAdapter<CreditSearchDetailBean, BaseViewHolder> {
        public CreditEnquiryAdapter() {
            super(R.layout.item_credit_enquiry_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CreditSearchDetailBean item) {
            ItemCreditEnquiryLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
        }
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.btn_search:  //  搜索
                String search = mViewModel.searchContent.get();
                if (TextUtils.isEmpty(search) || search.trim().length() == 0) {
                    ToastUtils.showShort("输入用户ID");
                    return;
                } else {
                    showLoading(Sys.LOADING);
                    mViewModel.creditSearchList();
                }
                break;
        }
    };
}
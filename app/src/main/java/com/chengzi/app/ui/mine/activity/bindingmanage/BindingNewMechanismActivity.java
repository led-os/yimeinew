package com.chengzi.app.ui.mine.activity.bindingmanage;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityBindingNewMechanismBindingImpl;
import com.chengzi.app.databinding.ItemBindingNewMechanismLayoutBindingImpl;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.mine.bean.BindSearchBean;
import com.chengzi.app.ui.mine.viewmodel.BindingManageViewModel;

import java.util.ArrayList;

/**
 * 绑定新机构
 *
 * @ClassName:BindingNewMechanismActivity
 * @PackageName:com.yimei.app.ui.mine.activity.bindingmanage
 * @Create On 2019/4/10 0010   16:31
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */

public class BindingNewMechanismActivity extends BaseActivity<ActivityBindingNewMechanismBindingImpl, BindingManageViewModel> {


    private BindingNewMechanismAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_binding_new_mechanism;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setViewModel(mViewModel);
        adapter = new BindingNewMechanismAdapter();
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.mRecyclerView.setAdapter(adapter);
        mBinding.mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //医院主页
        adapter.setOnItemClickListener((adapter1, view, position) ->
                HospitalHomePageActivity.start(this, adapter.getData().get(position).getId())
        );
        //接口回调
        observe();
    }

    private void observe() {
        showLoading(Sys.LOADING);
        mViewModel.bindSearch();
        //搜索医院
        mViewModel.bindSearchLiveData.observe(this, objects -> {
            dismissLoading();
            if (objects != null && objects.size() > 0) {
                mBinding.mRecyclerView.setVisibility(View.VISIBLE);
                mBinding.emptyView.setVisibility(View.GONE);
                adapter.setNewData(objects);
            } else {
                adapter.setNewData(new ArrayList<>());
                mBinding.emptyView.setVisibility(View.VISIBLE);
                mBinding.mRecyclerView.setVisibility(View.GONE);
            }
        });

        //绑定
        mViewModel.bindLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("已申请绑定结构,请等待审核!");
                goActivity(BindingManageActivity.class);
            }
        });
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.btn_search:  //  搜索
                String search = mViewModel.searchContent.get();
//                if (TextUtils.isEmpty(search) || search.trim().length() == 0) {
//                    ToastUtils.showShort("输入用户ID");
//                    return;
//                }
                showLoading(Sys.LOADING);
                mViewModel.bindSearch();
                break;
        }
    };

    public class BindingNewMechanismAdapter extends BaseQuickAdapter<BindSearchBean, BaseViewHolder> {
        public BindingNewMechanismAdapter() {
            super(R.layout.item_binding_new_mechanism_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, BindSearchBean item) {
            ItemBindingNewMechanismLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
        }
    }

    private ClickEventHandler<BindSearchBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_bind:  //绑定
                showLoading(Sys.LOADING);
                mViewModel.bind(bean.getId());
                break;
        }
    };
}
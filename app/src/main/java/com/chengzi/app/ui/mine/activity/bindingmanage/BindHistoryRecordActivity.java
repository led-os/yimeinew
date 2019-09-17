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
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityBindHistoryRecordBindingImpl;
import com.chengzi.app.databinding.ItemBindingHistoryRecordLayoutBindingImpl;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.mine.bean.BindHistoryBean;
import com.chengzi.app.ui.mine.viewmodel.BindHistoryRecordViewModel;

import java.util.ArrayList;

/**
 * 绑定的历史记录
 *
 * @ClassName:BindHistoryRecordActivity
 * @PackageName:com.yimei.app.ui.mine.activity.bindingmanage
 * @Create On 2019/4/10 0010   17:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class BindHistoryRecordActivity extends BaseActivity<ActivityBindHistoryRecordBindingImpl, BindHistoryRecordViewModel> {

    private PagingLoadHelper helper;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_bind_history_record;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        BindHistoryRecordAdapter adapter = new BindHistoryRecordAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        helper.start();
        mViewModel.bindHistoryLiveData.observe(this, objects -> {
            dismissLoading();
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class BindHistoryRecordAdapter extends BaseQuickAdapter<BindHistoryBean, BaseViewHolder> {
        public BindHistoryRecordAdapter() {
            super(R.layout.item_binding_history_record_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, BindHistoryBean item) {
            ItemBindingHistoryRecordLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
            binding.tvBingTime.setVisibility(View.VISIBLE);
        }
    }

    private ClickEventHandler<BindHistoryBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_bind_hospital_info://  绑定的医院信息
                HospitalHomePageActivity.start(this, bean.getBinding_id());
                break;
        }
    };
}
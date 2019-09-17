package com.chengzi.app.ui.discover.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityAtOtherUserBinding;
import com.chengzi.app.databinding.ItemAtOtherUserLayoutBinding;
import com.chengzi.app.ui.discover.viewmodel.AtOtherUserViewModel;
import com.chengzi.app.ui.mine.bean.FollowBean;

public class AtOtherUserActivity extends BaseActivity<ActivityAtOtherUserBinding, AtOtherUserViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_at_other_user;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        AtAdapter adapter = new AtAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        mViewModel.followListLive.observe(this, followListBeans -> {
            helper.onComplete(followListBeans);
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            FollowBean item = adapter.getItem(position);
            Intent intent = new Intent();
            intent.putExtra("AtUser",item);
            setResult(Activity.RESULT_OK,intent);
            finish();
        });

        helper.start();

    }

    private static class AtAdapter extends BaseQuickAdapter<FollowBean, BaseViewHolder> {

        public AtAdapter() {
            super(R.layout.item_at_other_user_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, FollowBean item) {
            ItemAtOtherUserLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setUrl(item.getImage());
            layoutBinding.executePendingBindings();
        }
    }
}

package com.chengzi.app.ui.rankinglist.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.handong.framework.base.LazyloadFragment;
import com.chengzi.app.ui.rankinglist.viewmodel.RankingListBaseViewModel;
import com.chengzi.app.ui.rankinglist.viewmodel.RankingListViewModel;

public abstract class RankingListBaseFragment<T extends ViewDataBinding, VM extends RankingListBaseViewModel>
        extends LazyloadFragment<T, VM> {

    private boolean needRefresh;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RankingListViewModel rankingListViewModel = ViewModelProviders
                .of(getActivity())
                .get(RankingListViewModel.class);
        rankingListViewModel.categoryIdLive.observe(this, categoryItem -> {
            viewModel.setCategoryId(categoryItem.getId());
            viewModel.setCategoryName(categoryItem.getName());
            if (getUserVisibleHint()) {
                onLazyload();
            }else{
                needRefresh = true;
            }
        });
    }

    @Override
    public void onLazyload() {
        if (!TextUtils.isEmpty(viewModel.getCategoryId())) {
            needRefresh = false;
            loadData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && needRefresh) {
            needRefresh = false;
            onLazyload();
        }
    }

    protected abstract void loadData();
}

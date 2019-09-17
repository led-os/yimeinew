package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.databinding.FragmentRelatedGoodsBinding;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.List;

public class RelatedGoodsFragment extends BaseFragment<FragmentRelatedGoodsBinding, GoodsDetailViewModel> {

    private MainGoodsAdapter relativeGoodsAdapter;

    public RelatedGoodsFragment() {
    }

    public static RelatedGoodsFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(RelatedGoodsFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new RelatedGoodsFragment();
            fragment.setArguments(args);
        }
        return (RelatedGoodsFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_related_goods;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        initRelativeGoods();

        viewModel.goodDetailLive.observe(this, goodDetailBean -> {
            List<GoodBean> recommend_data = goodDetailBean.getRecommend_data();
            relativeGoodsAdapter.setNewData(recommend_data);
            binding.getRoot().setVisibility(!relativeGoodsAdapter.getData().isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void initRelativeGoods() {
        relativeGoodsAdapter = new MainGoodsAdapter();
        binding.recyclerRecommand.setAdapter(relativeGoodsAdapter);
    }
}

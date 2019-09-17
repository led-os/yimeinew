package com.chengzi.app.ui.homepage.hospital.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.GoodsGridAdapter;
import com.chengzi.app.databinding.FragmentFeatureGoodsBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;

import java.util.List;

/**
 * 医院特色商品
 *
 * @ClassName:FeatureGoodsFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   10:34
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class FeatureGoodsFragment extends RefreshableFragment<FragmentFeatureGoodsBinding,
        HospitalHomePageViewModel> {

    public FeatureGoodsFragment() {
    }

    public static FeatureGoodsFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(FeatureGoodsFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new FeatureGoodsFragment();
        }
        android.os.Bundle args = new Bundle();
        fragment.setArguments(args);
        return (FeatureGoodsFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_feature_goods;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        GoodsGridAdapter adapter = new GoodsGridAdapter();
        adapter.setShowHospitalName(false);
        binding.recyclerFeatureGoods.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            GoodsDetailActivity.start(getActivity(), adapter.getItem(position).getId());
        });

        viewModel.hospitalDetailLive.observe(this, bean -> {
            if (bean == null || bean.getFeaturedProduct().isEmpty()) {
                binding.getRoot().setVisibility(View.GONE);
                return;
            }
            binding.getRoot().setVisibility(View.VISIBLE);
            List<GoodBean> featuredProduct = bean.getFeaturedProduct();
            adapter.setNewData(featuredProduct);
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }
}

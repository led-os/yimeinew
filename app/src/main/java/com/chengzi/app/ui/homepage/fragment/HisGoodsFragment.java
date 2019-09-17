package com.chengzi.app.ui.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.adapter.GoodsGridAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentHisGoodsBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.RelationCategoryBean;
import com.chengzi.app.ui.homepage.viewmodel.HisGoodsViewModel;

/**
 * TA的商品
 *
 * @ClassName:HisGoodsFragment
 * @PackageName:com.yimei.app.ui.doctorhomepage.fragment
 * @Create On 2019/4/18 0018   11:00
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class HisGoodsFragment extends RefreshableFragment<FragmentHisGoodsBinding,
        HisGoodsViewModel> {

    private static final String EXTRA_USER_TYPE = "extra_user_type";

    private GoodsGridAdapter adapter;
    //他的商品是否显示机构
    //①医生/咨询师的 显示
    //②机构的 不显示
    private boolean isShowHospitalName;

    public HisGoodsFragment() {
    }

    public static HisGoodsFragment newInstance(FragmentManager fm, String userId, int userType, boolean isShowHospitalName) {
        HisGoodsFragment fragment = (HisGoodsFragment) fm.findFragmentByTag(HisGoodsFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new HisGoodsFragment();
        }
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA, userId);
        args.putInt(EXTRA_USER_TYPE, userType);
        args.putBoolean("isShowHospitalName", isShowHospitalName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_his_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String userId = getArguments().getString(Sys.EXTRA);
        viewModel.setUserId(userId);
        int userType = getArguments().getInt(EXTRA_USER_TYPE, 2);
        viewModel.setUserType(userType);

        isShowHospitalName = getArguments().getBoolean("isShowHospitalName", false);
        binding.tvHisGoods.setText(userType == 4 ? getString(R.string.other_goods) : getString(R.string.his_goods));

        adapter = new GoodsGridAdapter();
        adapter.setShowHospitalName(isShowHospitalName);
        binding.recyclerHisGoods.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            GoodBean item = adapter.getItem(position);
            GoodsDetailActivity.start(getActivity(), item.getId());
        });

        binding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                RelationCategoryBean categoryBean = (RelationCategoryBean) tab.getTag();
                String categoryId = categoryBean.getId();
                viewModel.relationGoods(categoryId);
            }
        });

        observe();

    }

    private void observe() {

        viewModel.relationCategoryLive.observe(this, relationCategoryBeans -> {
            if (binding.tabLayout.getTabCount() > 0)
                binding.tabLayout.removeAllTabs();
            refreshComplete = true;
            finishRefresh();
            boolean empty = relationCategoryBeans == null || relationCategoryBeans.isEmpty();
            binding.getRoot().setVisibility(empty ? View.GONE : View.VISIBLE);
            if (empty) {
                return;
            }

            for (RelationCategoryBean categoryBean : relationCategoryBeans) {
                CustomTabLayout.Tab tab = binding.tabLayout.newTab()
                        .setText(categoryBean.getName())
                        .setTag(categoryBean);
                binding.tabLayout.addTab(tab);
            }
            String categoryId = relationCategoryBeans.get(0).getId();
            viewModel.relationGoods(categoryId);
        });

        viewModel.relativeGoodsLive.observe(this, goodBeans -> {
            adapter.setNewData(goodBeans);
        });

    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.relationGoodsCategorys();
    }

    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }
}

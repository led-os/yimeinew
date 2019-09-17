package com.chengzi.app.ui.discover.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentDiscoverBinding;
import com.chengzi.app.ui.discover.activity.PostActivity;
import com.chengzi.app.ui.discover.viewmodel.DiscoverViewModel;

/**
 * 发现
 *
 * @ClassName:FindFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/3/25 0025   16:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/25 0025 handongkeji All rights reserved.
 */

public class DiscoverFragment extends BaseFragment<FragmentDiscoverBinding, DiscoverViewModel> {


    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance() {
        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_discover;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.setAdapter(new ForumPagerAdapter(getChildFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.topBar.setOnRightClickListener(v -> {
            if (AccountHelper.shouldLogin(getActivity())) {
                return;
            }
            if (AccountHelper.getIdentity() > 1) {

                if (viewModel.getAuthStatus() < 0) {
                    ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                    return;
                }
                if (viewModel.getAuthStatus() != 1) {
                    ToastUtils.showShort("您尚未通过审核，无法操作！");
                    return;
                }
            }

            startActivity(new Intent(getActivity(), PostActivity.class));
        });

        viewModel.userInfo();
        viewModel.userInfoLiveData.observe(this, userInfoBean -> {
            if (userInfoBean == null) {
                return;
            }
            String auth_status = userInfoBean.getAuth_status();
            viewModel.setAuthStatus(Integer.valueOf(auth_status));
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            viewModel.userInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.userInfo();
    }

    private static class ForumPagerAdapter extends FragmentPagerAdapter {

        final String[] TITLES = {"用户", "医生", "咨询师", "机构"};

        public ForumPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return ForumFragment.newInstance(i + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}

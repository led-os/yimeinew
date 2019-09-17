package com.chengzi.app.ui.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseFragment;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentMyOrderBindingImpl;

/**
 * 我的订单
 *
 * @ClassName:MyOrderFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/4 0004   10:14
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class MyOrderFragment extends BaseFragment<FragmentMyOrderBindingImpl, BaseViewModel> {

    //订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）
    private int position;

    public static MyOrderFragment newInstance(int position) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_my_order;
    }

    //订单状态 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消（非必须）
    private final String[] TITLES0 = {"全部", "待付款", "待使用", "待评价", "已完成"};
    private final String[] TITLES1 = {"全部", "待付款", "未达成", "待使用", "待评价", "已完成"};

    private final Integer[] TITLES_STATUS0 = {0, 1, 3, 4, 5};
    private final Integer[] TITLES_STATUS1 = {0, 1, 2, 3, 4, 5};

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position", 0);
        }
        if (position == 1) {   //普通订单
//            binding.viewPager.setOffscreenPageLimit(TITLES0.length);
            binding.viewPager.setAdapter(new MyOrderAdapter(getChildFragmentManager()));
            binding.tabLayout.setupWithViewPager(binding.viewPager);
            binding.tabLayout.setTabMode(CustomTabLayout.MODE_FIXED);
        } else {  //拼购订单
//            binding.viewPager.setOffscreenPageLimit(TITLES1.length);
            binding.viewPager.setAdapter(new MyOrderAdapter(getChildFragmentManager()));
            binding.tabLayout.setupWithViewPager(binding.viewPager);
//            binding.tabLayout.setTabMode(CustomTabLayout.MODE_SCROLLABLE);
            binding.tabLayout.setTabMode(CustomTabLayout.MODE_FIXED);
        }
    }

    private class MyOrderAdapter extends FragmentPagerAdapter {
        public MyOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (position == 1) {   //普通订单
                return MyOrderListFragment.newInstance(position, TITLES_STATUS0[i]);
            } else {  //拼购订单
                return MyOrderListFragment.newInstance(position, TITLES_STATUS1[i]);
            }
        }

        @Override
        public int getCount() {
            if (position == 1) {   //普通订单
                return TITLES0.length;
            } else {  //拼购订单
                return TITLES1.length;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position1) {
            if (position == 1) {   //普通订单
                return TITLES0[position1];
            } else {  //拼购订单
                return TITLES1[position1];
            }
        }
    }
}
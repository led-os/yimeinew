package com.chengzi.app.ui.mine.activity.myorder;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.adapters.LazyFPagerAdapter;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityMyOrderBindingImpl;
import com.chengzi.app.ui.mine.fragment.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单-->(普通用户)    普通订单/拼购订单
 *
 * @ClassName:MyOrderActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/3 0003   18:44
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class MyOrderActivity extends BaseActivity<ActivityMyOrderBindingImpl, BaseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(orderTypeClickListener);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MyOrderFragment.newInstance(1));      //普通订单
        fragments.add(MyOrderFragment.newInstance(2));      //拼购订单
        LazyFPagerAdapter adapter = new LazyFPagerAdapter(getSupportFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(adapter);

        mBinding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mBinding.rbRegularOrders.setChecked(true);
                } else {
                    mBinding.rbGroupBuyOrders.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private ClickEventHandler<Object> orderTypeClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rb_regular_orders:        //普通订单
                mBinding.viewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_group_buy_orders:     //拼购订单
                mBinding.viewPager.setCurrentItem(1, false);
                break;
        }
    };
}
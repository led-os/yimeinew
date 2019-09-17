package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentDetailBinding;
import com.chengzi.app.ui.goods.viewmodel.DetailViewModel;

/**
 * 详情
 * @ClassName:DetailFragment

 * @PackageName:com.yimei.app.ui.goods.fragment

 * @Create On 2019/4/16 0016   10:17

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class DetailFragment extends BaseFragment<FragmentDetailBinding,
        DetailViewModel> implements View.OnClickListener {

    public DetailFragment() {
    }

    public static DetailFragment newInstance() {
        android.os.Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);
        binding.setSelectedPos(viewModel.selectedPos);
        binding.viewPager1.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        binding.viewPager1.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                viewModel.selectedPos.set(position);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goods_detail:
                binding.viewPager1.setCurrentItem(0);
                break;
            case R.id.btn_buy_notice:
                binding.viewPager1.setCurrentItem(1);
                break;
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return GoodsDetailFragment.newInstance();
                case 1:
                    return BuyNoticeFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

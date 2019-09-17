package com.chengzi.app.ui.mine.activity.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityInviteFriendsBindingImpl;
import com.chengzi.app.ui.mine.fragment.InviteFriendsFragment;
import com.chengzi.app.ui.mine.fragment.PromoteRankFragment;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;

/**
 * 邀请好友
 *
 * @ClassName:InviteFriendsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/2 0002   17:20
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */

public class InviteFriendsActivity extends BaseActivity<ActivityInviteFriendsBindingImpl, MyVipViewModel> {
    private final String[] TITLES = {"邀请好友", "推广排行"};

    @Override
    public int getLayoutRes() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.viewPager.setOffscreenPageLimit(TITLES.length);
        mBinding.viewPager.setAdapter(new IWantRecommendAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
        }
    };

    private class IWantRecommendAdapter extends FragmentPagerAdapter {
        public IWantRecommendAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
//                return IWantRecommendFragment.newInstance(bean);
                return new InviteFriendsFragment();
            } else {
                return new PromoteRankFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
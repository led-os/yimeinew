package com.chengzi.app.ui.peopleraise.activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityPeopleRaiseBinding;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.peopleraise.OnTopicChangeRegistry;
import com.chengzi.app.ui.peopleraise.fragment.PeopleRaiseFragment;
import com.chengzi.app.ui.peopleraise.viewmodel.PeopleRaiseActivityViewModel;


/**
 * 美人筹
 *
 * @ClassName:CollectTogetherActivity
 * @PackageName:com.yimei.app.ui.collecttogether.activity
 * @Create On 2019/4/4 0004   10:17
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class PeopleRaiseActivity extends BaseActivity<ActivityPeopleRaiseBinding, PeopleRaiseActivityViewModel>
        implements OnTopicChangeRegistry<String>, View.OnClickListener {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_people_raise;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(this);

        //  当前登录的用户不是普通用户，则隐藏 我要筹 按钮
        mBinding.setIsShow(AccountHelper.isLogin() && AccountHelper.getIdentity() == 1);
        mBinding.setViewModel(mViewModel);

        mBinding.viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.tabLayoutStatus.setupWithViewPager(mBinding.viewPager);

        mBinding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                CategoryItem categoryItem = (CategoryItem) tab.getTag();
                mViewModel.categoryIdLive.postValue(categoryItem.getId());
            }
        });

        observe();
        mViewModel.categoryList();

        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(mViewModel.keyword.get())) {
                    ToastUtils.showShort("请输入搜索关键字");
                    return true;
                }
                KeyboardUtils.hideSoftInput(this);
                mViewModel.searchLive.setValue(mViewModel.keyword.get());
            }
            return true;
        });
    }

    private void observe() {
        mViewModel.categoryLive.observe(this, categoryItems -> {
            if (categoryItems == null) {
                return;
            }
            mBinding.tabLayout.removeAllTabs();
            for (CategoryItem categoryItem : categoryItems) {
                CustomTabLayout.Tab tab = mBinding.tabLayout.newTab()
                        .setText(categoryItem.getName())
                        .setTag(categoryItem);
                mBinding.tabLayout.addTab(tab);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                if (TextUtils.isEmpty(mViewModel.keyword.get())) {
                    ToastUtils.showShort("请输入搜索关键字");
                    return;
                }
                KeyboardUtils.hideSoftInput(this);
                mViewModel.searchLive.setValue(mViewModel.keyword.get());
                break;
            case R.id.btn_raise:
                startActivity(new Intent(this, PostPeopleRaiseActivity.class));
                break;
        }
    }

    @Override
    public void observeSearch(Observer<String> observer) {
        mViewModel.searchLive.observe(this, observer);
    }

    @Override
    public void observeCategory(Observer<String> observer) {
        mViewModel.categoryIdLive.observe(this, observer);
    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        String[] status = {"进行中", "已成功", "未达成"};

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return PeopleRaiseFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return status[position];
        }
    }
}

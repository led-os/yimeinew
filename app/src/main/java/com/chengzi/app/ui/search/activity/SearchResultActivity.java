package com.chengzi.app.ui.search.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySearchResultBinding;
import com.chengzi.app.databinding.SearchTabViewBinding;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.search.DisplayChangeRegistry;
import com.chengzi.app.ui.search.OnDisplayChangeObserver;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.fragment.SearchCaseFragment;
import com.chengzi.app.ui.search.fragment.SearchCounselorFragment;
import com.chengzi.app.ui.search.fragment.SearchDoctorFragment;
import com.chengzi.app.ui.search.fragment.SearchGoodsFragment;
import com.chengzi.app.ui.search.fragment.SearchOrganizationFragment;
import com.chengzi.app.ui.search.viewmodel.SearchResultViewModel;
import com.handong.framework.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果页
 *
 * @ClassName:SearchResultActivity
 * @PackageName:com.yimei.app.ui.search.activity
 * @Create On 2019/4/3 0003   17:44
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class SearchResultActivity extends BaseActivity<ActivitySearchResultBinding,
        SearchResultViewModel> implements View.OnClickListener, DisplayChangeRegistry {

    public static final String EXTRA_SEARCH_TEXT = "extra_search_text";

    public static void start(Context context, String searchText) {
        Intent intent = new Intent(context, SearchResultActivity.class)
                .putExtra(EXTRA_SEARCH_TEXT, searchText);
        context.startActivity(intent);
    }

    //从找来的-->

    /**
     * @param context
     * @param tab     0-商品 1-机构 2-医生 3-案例 4-咨询师
     */
    public static void start(Context context, int tab) {
        Intent intent = new Intent(context, SearchResultActivity.class)
                .putExtra(Sys.EXTRA, tab);
        context.startActivity(intent);
    }

    private List<OnDisplayChangeObserver> observers = new ArrayList<>();
    private List<SearchListener> listeners = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setViewModel(mViewModel);
        mBinding.setListener(this);

        mBinding.tabLayout.setCustomTabFactory((text) -> {
            SearchTabViewBinding tabViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.search_tab_view, mBinding.tabLayout, false);
            tabViewBinding.textView.setText(text);
            tabViewBinding.executePendingBindings();
            CustomTabLayout.Tab tab = mBinding.tabLayout.newTab()
                    .setCustomView(tabViewBinding.getRoot());
            return tab;
        });

        mBinding.viewPager.setOffscreenPageLimit(5);
        mBinding.viewPager.setAdapter(new SearchPageAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        //从找->页面进来的
        int tab_select_position = getIntent().getIntExtra(Sys.EXTRA, 0);
        if (tab_select_position != 0) {
            mBinding.tabLayout.getTabAt(tab_select_position).select();
            mBinding.viewPager.setCurrentItem(tab_select_position);
        }

        mBinding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                mBinding.btnDisplayChange.setVisibility(tab.getPosition() == 3 ? View.GONE : View.VISIBLE);
            }
        });

        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = mViewModel.keyword.get();
                if (TextUtils.isEmpty(keyword) || keyword.trim().length() == 0) {
                    ToastUtils.showShort("请输入搜索关键字");
                    return true;
                }
                KeyboardUtils.hideSoftInput(this);
                mViewModel.saveHistory();
                for (SearchListener listener : listeners) {
                    listener.search(mViewModel.keyword.get());
                }
                return true;
            }
            return false;
        });

    }

    private void parseIntent() {
        Intent intent = getIntent();
        String extra = intent.getStringExtra(EXTRA_SEARCH_TEXT);
        if (!TextUtils.isEmpty(extra)) {
            mViewModel.keyword.set(extra);
            mViewModel.saveHistory();
            mBinding.editText.post(() -> mBinding.editText.setSelection(extra.length()));
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if ((fragment instanceof SearchListener) && !listeners.contains(fragment)) {
            listeners.add((SearchListener) fragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_display_change:
                Display display = mViewModel.display.get();
                if (display == Display.LIST) {
                    mViewModel.display.set(Display.GRID);
                } else {
                    mViewModel.display.set(Display.LIST);
                }
                for (OnDisplayChangeObserver observer : observers) {
                    observer.onDisplayChange(mViewModel.display.get());
                }
                break;
            case R.id.btn_clear:  //  清空输入框
                mViewModel.keyword.set("");
                KeyboardUtils.showSoftInput(this);
                break;
            case R.id.btn_refer:  //  私享咨询
                PrivateReferActivity.start(this);
                break;
            case R.id.btn_people_say:  //  大家说
                finish();
                EventBus.getDefault().post(new SwitchMainTabEvent(3));
//                RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 3);
                break;
        }
    }

    @Override
    public void register(OnDisplayChangeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(OnDisplayChangeObserver observer) {
        observers.remove(observer);
    }

    private class SearchPageAdapter extends FragmentPagerAdapter {

        String[] title = {"商品", "机构", "医生", "案例", "咨询师"};

        public SearchPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return SearchGoodsFragment.newInstance(mViewModel.keyword.get());
                case 1:
                    return SearchOrganizationFragment.newInstance(mViewModel.keyword.get());
                case 2:
                    return SearchDoctorFragment.newInstance(mViewModel.keyword.get());
                case 3:
                    return SearchCaseFragment.newInstance(mViewModel.keyword.get());
                case 4:
                    return SearchCounselorFragment.newInstance(mViewModel.keyword.get());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
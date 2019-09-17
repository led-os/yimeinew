package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityQuestionSearchResultBinding;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.ui.onlinequestionandanswer.fragment.QuestionListFragment;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.QuestionSearchResultViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 提问搜索结果
 *
 * @ClassName:QuestionSearchResultActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/13 0013   16:43
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class QuestionSearchResultActivity extends BaseActivity<ActivityQuestionSearchResultBinding,
        QuestionSearchResultViewModel> implements View.OnClickListener {

    /**
     * 在待解决问题界面点搜索时，在搜索结果页面直接显示到待解决tab下，
     * 在已解决问题界面点搜索时，在搜索结果页面，直接显示到已解决tab下，
     * 默认显示到全部tab下
     */
    private int requestStatus;
    private List<SearchListener> listeners = new ArrayList<>();

    public static void start(Context context, String keyword, int status) {
        Intent intent = new Intent(context, QuestionSearchResultActivity.class);
        intent.putExtra(Sys.EXTRA, keyword);
        intent.putExtra(Sys.EXTRA_QUESTION_STATUS, status);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_question_search_result;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.viewPager.setAdapter(new QuestionPagerAdapter(getSupportFragmentManager(), mViewModel.keyword.get()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.viewPager.setCurrentItem(requestStatus);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        String keyword = intent.getStringExtra(Sys.EXTRA);
        requestStatus = intent.getIntExtra(Sys.EXTRA_QUESTION_STATUS, Sys.EXTRA_STATUS_ALL);

        mViewModel.keyword.set(keyword);
        mBinding.editText.post(() -> mBinding.editText.setSelection(keyword.length()));
        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (TextUtils.isEmpty(mViewModel.keyword.get()) || mViewModel.keyword.get().trim().length() == 0) {
                ToastUtils.showShort("请输入搜索关键字");
                return false;
            }
            KeyboardUtils.hideSoftInput(this);
            for (SearchListener listener : listeners) {
                listener.search(mViewModel.keyword.get());
            }
            return true;
        });
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
            case R.id.btn_clear:
                mViewModel.keyword.set("");
                break;
        }
    }

    private static class QuestionPagerAdapter extends FragmentPagerAdapter {

        private String keyword;

        String[] status = {"全部", "已解决", "未解决"};

        public QuestionPagerAdapter(FragmentManager fm, String keyword) {
            super(fm);
            this.keyword = keyword;
        }

        @Override
        public Fragment getItem(int i) {
            return QuestionListFragment.newInstance(i, keyword);
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

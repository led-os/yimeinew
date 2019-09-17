package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.adapter.SearchAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityQuestionSearchBinding;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.QuestionSearchViewModel;
import com.chengzi.app.ui.search.bean.SearchBean;

import java.util.List;

/**
 * 提问搜索
 *
 * @ClassName:QuestionSearchActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/13 0013   16:42
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class QuestionSearchActivity extends BaseActivity<ActivityQuestionSearchBinding,
        QuestionSearchViewModel> implements View.OnClickListener {

    private SearchAdapter historySearchAdapter;
    private SearchAdapter hotAdapter;

    public static void start(Context context, int status) {
        Intent intent = new Intent(context, QuestionSearchActivity.class);
        intent.putExtra(Sys.EXTRA_QUESTION_STATUS, status);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_question_search;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setViewModel(mViewModel);
        mBinding.setListener(this);

        initHistoryRecycler();
        initHotRecycler();

        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });

        mViewModel.searchKeywords();

        mViewModel.onlineQAlive.observe(this, onlineQAbean -> {
            List<SearchBean> history = onlineQAbean.getHistory();
            List<SearchBean> hot = onlineQAbean.getHot();

            mBinding.layoutHistory.setVisibility(history == null || history.isEmpty() ? View.GONE : View.VISIBLE);
            if (history != null) {
                historySearchAdapter.setNewData(history);
            }

            hotAdapter.setNewData(hot);

        });

    }


    private void initHistoryRecycler() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        mBinding.historySearch.setLayoutManager(layoutManager);
        historySearchAdapter = new SearchAdapter();
        mBinding.historySearch.setAdapter(historySearchAdapter);

        historySearchAdapter.setOnItemClickListener((adapter1, view, position) -> {
            SearchBean item = historySearchAdapter.getItem(position);
            mViewModel.keyword.set(item.getKey());
            search();
        });
    }

    private void initHotRecycler() {

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        mBinding.hotSearch.setLayoutManager(layoutManager);
        hotAdapter = new SearchAdapter();
        mBinding.hotSearch.setAdapter(hotAdapter);

        hotAdapter.setOnItemClickListener((adapter1, view, position) -> {
            SearchBean item = hotAdapter.getItem(position);
            mViewModel.keyword.set(item.getKey());
            search();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_search:
                search();
                break;
//            case R.id.btn_delete:
//                BaseQuickAdapter adapter = (BaseQuickAdapter) mBinding.historySearch.getAdapter();
//                adapter.getData().clear();
//                adapter.notifyDataSetChanged();
//                mBinding.layoutHistory.setVisibility(adapter.getData().isEmpty() ? View.GONE : View.VISIBLE);
//                break;
        }
    }

    private void search() {
        String keyword = mViewModel.keyword.get();
        if (TextUtils.isEmpty(keyword) || keyword.trim().length() == 0) {
            ToastUtils.showShort("请输入搜索关键字");
            return;
        }
        int status = getIntent().getIntExtra(Sys.EXTRA_QUESTION_STATUS, Sys.EXTRA_STATUS_ALL);
        QuestionSearchResultActivity.start(this, mViewModel.keyword.get(), status);
        finish();
    }
}

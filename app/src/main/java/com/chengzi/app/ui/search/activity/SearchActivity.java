package com.chengzi.app.ui.search.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.adapter.SearchAdapter;
import com.chengzi.app.databinding.ActivitySearchBinding;
import com.chengzi.app.ui.search.bean.SearchBean;
import com.chengzi.app.ui.search.viewmodel.SearchViewModel;

/**
 * 搜索
 *
 * @ClassName:SearchActivity
 * @PackageName:com.yimei.app.ui.search.activity
 * @Create On 2019/4/3 0003   17:41
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class SearchActivity extends BaseActivity<ActivitySearchBinding, SearchViewModel>
        implements View.OnClickListener {

    private SearchAdapter historySearchAdapter;
    private SearchAdapter searchHotAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setViewModel(mViewModel);
        mBinding.setListener(this);

        initHistoryRecycler();
        initHotRecycler();
        observe();

        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });

        mViewModel.searchHistory();
        mViewModel.searchHot();
    }

    private void observe() {
        mViewModel.searchHistoryLive.observe(this, searchHistoryBeans -> {
            mBinding.viewSwitcher.setDisplayedChild(1);
            historySearchAdapter.setNewData(searchHistoryBeans);
            mBinding.layoutHistory.setVisibility(historySearchAdapter.getData().isEmpty() ? View.GONE : View.VISIBLE);

        });

        mViewModel.searchHotLive.observe(this, hotSearchBeans -> {
            mBinding.viewSwitcher.setDisplayedChild(1);
            searchHotAdapter.setNewData(hotSearchBeans);
        });

        mViewModel.delHistoryLive.observe(this,aBoolean -> {
            mViewModel.searchHistory();
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
        searchHotAdapter = new SearchAdapter();
        mBinding.hotSearch.setAdapter(searchHotAdapter);

        searchHotAdapter.setOnItemClickListener((adapter1, view, position) -> {
            SearchBean item = searchHotAdapter.getItem(position);
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
            case R.id.btn_delete:
                mViewModel.delAllHistory();
                BaseQuickAdapter adapter = (BaseQuickAdapter) mBinding.historySearch.getAdapter();
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                mBinding.layoutHistory.setVisibility(adapter.getData().isEmpty() ? View.GONE : View.VISIBLE);
                break;
        }
    }

    private void search() {
        String keyword = mViewModel.keyword.get();
        if (TextUtils.isEmpty(keyword) || keyword.trim().length() == 0) {
            ToastUtils.showShort("请输入搜索关键字");
            return;
        }
//        mViewModel.saveHistory();
        SearchResultActivity.start(this, mViewModel.keyword.get());
        finish();
    }

}

package com.chengzi.app.ui.onlinequestionandanswer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.OnlineQuestionAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentQuestionListBinding;
import com.chengzi.app.interfaces.SearchListener;
import com.chengzi.app.ui.onlinequestionandanswer.activity.QuestionDetailActivity;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.QuestionListViewModel;

/**
 * 问题搜索结果列表
 *
 * @ClassName:QuestionListFragment
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.fragment
 * @Create On 2019/4/15 0015   11:49
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class QuestionListFragment extends BaseFragment<FragmentQuestionListBinding, QuestionListViewModel>
        implements SearchListener {

    private PagingLoadHelper helper;

    public QuestionListFragment() {
    }

    public static QuestionListFragment newInstance(int status, String keyword) {
        android.os.Bundle args = new Bundle();
        args.putInt(Sys.EXTRA_QUESTION_STATUS, status);
        args.putString(Sys.EXTRA_SEARCH_KEYWORD, keyword);
        QuestionListFragment fragment = new QuestionListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_question_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        int status = getArguments().getInt(Sys.EXTRA_QUESTION_STATUS, Sys.EXTRA_STATUS_ALL);
        String keyword = getArguments().getString(Sys.EXTRA_SEARCH_KEYWORD);
        viewModel.setStatus(String.valueOf(status));
        viewModel.setKeyword(keyword);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        OnlineQuestionAdapter adapter = new OnlineQuestionAdapter();

        binding.swipeRefreshView.setAdapter(adapter);

        viewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });
        helper.start();

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Object item = adapter.getItem(position);
            if (item instanceof QAbean) {
                QuestionDetailActivity.start(getActivity(), ((QAbean)item).getId());
            }
        });
    }

    @Override
    public void search(String keyword) {
        viewModel.setKeyword(keyword);
        helper.start();
    }
}

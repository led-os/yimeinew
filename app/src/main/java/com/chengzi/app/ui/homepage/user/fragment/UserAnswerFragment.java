package com.chengzi.app.ui.homepage.user.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentUserAnswerBinding;
import com.chengzi.app.ui.goods.activity.PeopleSayDetailActivity;
import com.chengzi.app.ui.homepage.bean.GetAnswersBean;
import com.chengzi.app.ui.homepage.user.viewmodel.UserHomePageViewModel;
import com.chengzi.app.ui.homepage.viewmodel.UserAnswerViewModel;

import java.util.ArrayList;

/**
 * 用户回答
 *
 * @ClassName:UserAnswerFragment
 * @PackageName:com.yimei.app.ui.homepage.user.fragment
 * @Create On 2019/4/19 0019   16:33
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class UserAnswerFragment extends LazyloadFragment<FragmentUserAnswerBinding, UserAnswerViewModel> {

    private PagingLoadHelper helper;

    //被查看者id
    private String click_id;

    public static UserAnswerFragment newInstance(String click_id) {
        UserAnswerFragment fragment = new UserAnswerFragment();
        android.os.Bundle args = new Bundle();
        args.putString("click_id", click_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user_answer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            click_id = arguments.getString("click_id");
        }
        initRecyclerView();
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.click_id.set(click_id);
        viewModel.userAnswersLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });

        UserHomePageViewModel homePageViewModel = ViewModelProviders.of(getActivity())
                .get(UserHomePageViewModel.class);
        homePageViewModel.refreshLive.observe(this, aBoolean -> {
            if (getView() != null && getUserVisibleHint()) {
                helper.start();
            }
        });
    }

    private void initRecyclerView() {

        PeopleSayListAdapter adapter = new PeopleSayListAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPulldownEnable(false);
        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            GetAnswersBean getAnswersBean = adapter.getData().get(position);
            PeopleSayDetailActivity.start(getContext(), getAnswersBean.getTo_question_id(), viewModel.click_id.get(), AccountHelper.getIdentity());
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    public class PeopleSayListAdapter extends BaseQuickAdapter<GetAnswersBean, BaseViewHolder> {

        public PeopleSayListAdapter() {
            super(R.layout.item_people_says_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, GetAnswersBean item) {
            helper.setText(R.id.tv_content, item.getQuestion_content())
                    .setText(R.id.tv_questions, item.getAnswer_content())
                    .setText(R.id.tv_toutal, item.getAnswer_num() + "个回答")
                    .setText(R.id.tv_update, "更新于" + item.getUpdate_time())
            ;
        }
    }
}
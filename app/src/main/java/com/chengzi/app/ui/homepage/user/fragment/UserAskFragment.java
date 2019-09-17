package com.chengzi.app.ui.homepage.user.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentUserAskBinding;
import com.chengzi.app.databinding.ItemOnlineQuestionLayoutBinding;
import com.chengzi.app.event.AnswerOnlineQuestionEvent;
import com.chengzi.app.event.DeleteOnlineQuestionEvent;
import com.chengzi.app.ui.homepage.bean.UserQuestionsBean;
import com.chengzi.app.ui.homepage.user.viewmodel.UserHomePageViewModel;
import com.chengzi.app.ui.homepage.viewmodel.UserAskViewModel;
import com.chengzi.app.ui.onlinequestionandanswer.activity.QuestionDetailActivity;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 用户提问
 *
 * @ClassName:UserAskFragment
 * @PackageName:com.yimei.app.ui.homepage.user.fragment
 * @Create On 2019/4/19 0019   16:33
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class UserAskFragment extends LazyloadFragment<FragmentUserAskBinding, UserAskViewModel> {

    private PagingLoadHelper helper;
    private OnlineQuestionAdapter adapter;

    //被查看者id
//    private String click_id;

    public static UserAskFragment newInstance(String targetUserId) {
        UserAskFragment fragment = new UserAskFragment();
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA_TARGET_ID, targetUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user_ask;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        Bundle arguments = getArguments();
        String targetUserId = arguments.getString(Sys.EXTRA_TARGET_ID);
        viewModel.setTargetUserId(targetUserId);

        initRecyclerView();

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        observe();
    }

    private void observe() {
        viewModel.userQuestionsLiveData.observe(this, objects -> {
            helper.onComplete(objects);
        });

        viewModel.questionLive.observe(this, userQuestionsBeans -> {
            if (userQuestionsBeans != null) {
                adapter.addData(userQuestionsBeans);
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
        adapter = new OnlineQuestionAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPulldownEnable(false);
//        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (TextUtils.equals(viewModel.getTargetUserId(), AccountHelper.getUserId())) {
                QuestionDetailActivity.startFromSelf(getContext(), adapter.getData().get(position).getQuestion_id());
            } else {
                QuestionDetailActivity.start(getContext(), adapter.getData().get(position).getQuestion_id());
            }
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Subscribe
    public void onQuestionDelete(DeleteOnlineQuestionEvent event) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            UserQuestionsBean questionsBean = adapter.getData().get(i);
            if (TextUtils.equals(questionsBean.getQuestion_id(), event.getQuestionId())) {
                adapter.getData().remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();

        if (adapter.getData().size() + 1 >= helper.getCurrentPage() * helper.getPageSize()) {
            viewModel.getQuestion(String.valueOf(adapter.getData().size() + 1));
        }
    }

    @Subscribe
    public void onNewAnswer(AnswerOnlineQuestionEvent event) {
        for (UserQuestionsBean questionsBean : adapter.getData()) {
            if (TextUtils.equals(event.getQuestionId(), questionsBean.getQuestion_id())) {
                questionsBean.setToutal(event.getAnswerNum() + "");
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public class OnlineQuestionAdapter extends BaseQuickAdapter<UserQuestionsBean, BaseViewHolder> {
        public OnlineQuestionAdapter() {
            super(R.layout.item_online_question_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, UserQuestionsBean item) {
            ItemOnlineQuestionLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

            QAbean qAbean = new QAbean(item.getTitle(),
                    Integer.valueOf(item.getStatus()), Integer.valueOf(item.getToutal()),
                    item.getSelect_name(), item.getSelect_image());

            Context context = helper.itemView.getContext();

            binding.setResolved(item.getStatus().equals("1"));

            List<UserQuestionsBean.AnswerEntity> answer = item.getAnswer();
            /*if (answer != null && !answer.isEmpty()) {
                UserQuestionsBean.AnswerEntity answerEntity = answer.get(0);
                String answerContent = answerEntity.getContent();
                answerContent = TextUtils.isEmpty(answerContent) ? "" : answerContent;
                SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
                ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.color_3));
                spanBuilder.append(context.getString(R.string.accept_answer_hint), fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spanBuilder.append(answerContent);
                binding.tvAnswerContent.setText(spanBuilder);
            }*/
            String answer_id = item.getAnswer_id();
            if (answer != null && !answer.isEmpty() && !TextUtils.isEmpty(answer_id) && item.getStatus().equals("1")) {
                for (int i = 0; i < answer.size(); i++) {
                    UserQuestionsBean.AnswerEntity answerEntity = answer.get(i);
                    if (item.getAnswer_id().equals(answerEntity.getId())) {
                        String answerContent = answerEntity.getContent();
                        answerContent = TextUtils.isEmpty(answerContent) ? "" : answerContent;
                        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
                        ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.color_3));
                        spanBuilder.append(context.getString(R.string.accept_answer_hint), fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        spanBuilder.append(answerContent);
                        binding.tvAnswerContent.setText(spanBuilder);
                        break;
                    }
                }
            }
            binding.setBean(qAbean);
            binding.executePendingBindings();
        }
    }
}
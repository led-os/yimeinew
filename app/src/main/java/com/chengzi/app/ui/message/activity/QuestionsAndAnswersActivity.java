package com.chengzi.app.ui.message.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityQuestionsAndAnswersBinding;
import com.chengzi.app.databinding.ItemQuestionOnlineLayoutBinding;
import com.chengzi.app.ui.goods.activity.PeopleSayDetailActivity;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.message.bean.QuestionMsgBean;
import com.chengzi.app.ui.message.viewmodel.QuestionAndAnswerViewModel;
import com.chengzi.app.ui.onlinequestionandanswer.activity.QuestionDetailActivity;

/**
 * 消息---在线问答
 * @ClassName:QuestionsAndAnswersActivity

 * @PackageName:com.yimei.app.ui.message.activity

 * @Create On 2019/4/11 0011   17:19

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class QuestionsAndAnswersActivity extends BaseActivity<ActivityQuestionsAndAnswersBinding,
        QuestionAndAnswerViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_questions_and_answers;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        mBinding.swipeRefreshView.setAdapter(new QuestionAnswerAdapter());
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });
        helper.start();
    }

    private class QuestionAnswerAdapter extends BaseQuickAdapter<QuestionMsgBean,BaseViewHolder>
            implements ClickEventHandler<QuestionMsgBean> {

        public QuestionAnswerAdapter() {
            super(R.layout.item_question_online_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, QuestionMsgBean item) {
            ItemQuestionOnlineLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setListener(this);

            // question ask_others
            String type = item.getType();
            if (TextUtils.equals(type,"question")) {
                layoutBinding.tvQuestionType.setText("在线回答");
            }else if (TextUtils.equals(type,"ask_others")){
                layoutBinding.tvQuestionType.setText("大家说");
            }

            layoutBinding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, QuestionMsgBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            switch (view.getId()) {
                case R.id.image_head:
                case R.id.tv_name:

                    int userType = bean.getUser_type();
                    if (userType == 1) {
                        if (view.getContext().getClass() == UserHomePageActivity.class) {
                            return;
                        }
                        UserHomePageActivity.start(view.getContext(), bean.getUser_id());
                    } else if (userType == 2) {
                        DoctorHomePageActivity.startDoctor(view.getContext(), bean.getUser_id());
                    } else if (userType == 3) {
                        DoctorHomePageActivity.startCounselor(view.getContext(), bean.getUser_id());
                    } else {
                        HospitalHomePageActivity.start(view.getContext(), bean.getUser_id());
                    }
                    break;
                case R.id.layout_question:
                    // question ask_others
                    String type = bean.getType();
                    if (TextUtils.equals(type,"question")) {
                        QuestionDetailActivity.start(view.getContext(),bean.getId());
                    }else if (TextUtils.equals(type,"ask_others")){
                        PeopleSayDetailActivity.start(view.getContext(),bean.getId(),bean.getUser_id(),bean.getUser_type());
                    }
                    break;
            }
        }
    }
}

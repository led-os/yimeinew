package com.chengzi.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemOnlineQuestionLayoutBinding;
import com.chengzi.app.databinding.ItemOnlineQuestionSubTitleLayoutBinding;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;

public class OnlineQuestionAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    public static final int TYPE_SUB_TITLE = 0x01;
    public static final int TYPE_ITEM = 0x00;

    public OnlineQuestionAdapter() {
        super(0);
        setMultiTypeDelegate(new MultiTypeDelegate<Object>() {

            @Override
            protected int getItemType(Object o) {
                if ((o instanceof FindBaseBean)) {
                    return TYPE_SUB_TITLE;
                } else {
                    return TYPE_ITEM;
                }
            }
        });
        getMultiTypeDelegate().registerItemType(TYPE_ITEM, R.layout.item_online_question_layout);
        getMultiTypeDelegate().registerItemType(TYPE_SUB_TITLE, R.layout.item_online_question_sub_title_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case TYPE_ITEM:
                bindItem(helper, (QAbean) item);
                break;
            case TYPE_SUB_TITLE:
                bindSubTitle(helper, (FindBaseBean) item);
                break;
        }

    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    private void bindItem(BaseViewHolder helper, QAbean qAbean) {

        ItemOnlineQuestionLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

        Context context = helper.itemView.getContext();

        String answerContent = qAbean.getAwser_content();
        answerContent = TextUtils.isEmpty(answerContent) ? "" : answerContent;
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        ForegroundColorSpan fcs = new ForegroundColorSpan(context.getResources().getColor(R.color.color_3));
        spanBuilder.append(context.getString(R.string.accept_answer_hint), fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanBuilder.append(answerContent);
        binding.tvAnswerContent.setText(spanBuilder);

        binding.setBean(qAbean);
        binding.setResolved(qAbean.getStatus() == 1);

        binding.executePendingBindings();

    }

    private void bindSubTitle(BaseViewHolder helper, FindBaseBean findBaseBean) {
        ItemOnlineQuestionSubTitleLayoutBinding titleLayoutBinding = DataBindingUtil.bind(helper.itemView);
        titleLayoutBinding.tvSubTitle.setText(findBaseBean.getName() + "热门问答");
        titleLayoutBinding.executePendingBindings();
    }
}

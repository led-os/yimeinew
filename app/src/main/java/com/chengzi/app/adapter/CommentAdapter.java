package com.chengzi.app.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemCommentLayoutBinding;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.activity.CommentDetailActivity;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;

/**
 * 评论列表 adapter
 *
 * @ClassName:CommentAdapter
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/3/30 0030   16:30
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/3/30 0030 handongkeji All rights reserved.
 */
public class CommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    private int commentType;     //  1 案例 2 美人筹 3 发现圈
    private String targetId;

    private OnCommentClickListener listener;

    public void setListener(OnCommentClickListener listener) {
        this.listener = listener;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    private final ClickEventHandler<CommentBean> clickEventHandler = (view, bean) -> {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.image_head:

                int userType = bean.getUser_type();
                if (userType == 1) {
                    UserHomePageActivity.start(view.getContext(), bean.getFrom_uid());
                } else if (userType == 2) {
                    DoctorHomePageActivity.startDoctor(view.getContext(), bean.getFrom_uid());
                } else if (userType == 3) {
                    DoctorHomePageActivity.startCounselor(view.getContext(), bean.getFrom_uid());
                } else {
                    HospitalHomePageActivity.start(view.getContext(), bean.getFrom_uid());
                }
                break;
            case R.id.tv_name:
            case R.id.tv_comment_content:
                if (listener != null) {
                    listener.onContent(view, bean);
                }
                break;
            case R.id.btn_delete:
                new QMUIDialog.MessageDialogBuilder(view.getContext())
                        .setMessage("确定删除该条评论?")
                        .addAction(0, R.string.cancel, QMUIDialogAction.ACTION_PROP_NEGATIVE, (dialog, index) -> {
                            dialog.dismiss();
                        })
                        .addAction(0, R.string.confirm, QMUIDialogAction.ACTION_PROP_POSITIVE, (dialog, index) -> {
                            if (listener != null) {
                                listener.onDelete(view, bean);
                            }
                            dialog.dismiss();
                        })
                        .show();

                break;
            case R.id.layout_child_comment:
                CommentDetailActivity.start(view.getContext(), targetId, bean.getId(), commentType);
                break;
        }
    };

    public CommentAdapter(int commentType) {
        super(R.layout.item_comment_layout);
        this.commentType = commentType;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {

        Context context = helper.itemView.getContext();

        ItemCommentLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setListener(clickEventHandler);
        binding.setBean(item);

        boolean isSelf = TextUtils.equals(AccountHelper.getUserId(), item.getFrom_uid());
        binding.setIsSelef(isSelf);

        if (TextUtils.isEmpty(item.getTo_uid()) || TextUtils.equals(item.getTo_uid(), "0")) {
            binding.tvName.setText(item.getFrom_name());
        } else {
            String commentrepliernick = TextUtils.isEmpty(item.getFrom_name()) ? "" : item.getFrom_name();
            String usernick = TextUtils.isEmpty(item.getTo_name()) ? "" : item.getTo_name();
//            String text = context.getString(R.string.hand_reply_blank, usernick, commentrepliernick);
            String text = context.getString(R.string.hand_reply_blank, commentrepliernick, usernick);
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan fcs = new ForegroundColorSpan(0xFF333333);
            ss.setSpan(fcs, commentrepliernick.length(), commentrepliernick.length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            binding.tvName.setText(ss);
        }

        item.setOnChildCommentChange(bean -> {
            setChildComment(context, binding, bean);
        });

        setChildComment(context, binding, item);

        binding.executePendingBindings();
    }

    private void setChildComment(Context context, ItemCommentLayoutBinding binding, CommentBean item) {
        ChildCommentAdapter childCommentAdapter = (ChildCommentAdapter) binding.childComment.getAdapter();
        if (childCommentAdapter == null) {
            childCommentAdapter = new ChildCommentAdapter();
            binding.childComment.swapAdapter(childCommentAdapter, false);
        }
        childCommentAdapter.setNewData(item.getSubChildComment());
        binding.tvChildNumber.setVisibility(item.getChild_comment().size() <= 3 ? View.GONE : View.VISIBLE);
        binding.tvChildNumber.setText(context
                .getString(R.string.view_child_comment, item.getChild_comment().size()));
    }
}

/**
 * 2、3级评论   CommentBean.ChildCommentLastBean
 *
 * @ClassName:CommentAdapter
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/3/30 0030   16:31
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/3/30 0030 handongkeji All rights reserved.
 */
class ChildCommentAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public ChildCommentAdapter() {
        super(R.layout.item_child_comment_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {
        Context context = helper.itemView.getContext();
        if (TextUtils.isEmpty(item.getTo_uid()) || TextUtils.equals(item.getTo_uid(), "0")) {

            helper.setText(R.id.tv_user_name, item.getFrom_name() + ":");

        } else {
            String commentrepliernick = TextUtils.isEmpty(item.getFrom_name()) ? "" : item.getFrom_name();
            String usernick = TextUtils.isEmpty(item.getTo_name()) ? "" : item.getTo_name();
//            String text = context.getString(R.string.hand_reply_blank, usernick, commentrepliernick);
            String text = context.getString(R.string.hand_reply_blank, commentrepliernick, usernick);
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan fcs = new ForegroundColorSpan(0xFF333333);
            ss.setSpan(fcs, commentrepliernick.length(), commentrepliernick.length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_user_name, ss);
        }

        helper.setText(R.id.tv_comment_content, item.getContent());

    }
}
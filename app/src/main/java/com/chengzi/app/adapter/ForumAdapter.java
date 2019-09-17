package com.chengzi.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.BuildConfig;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemForumListLayoutBinding;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.utils.HDLinkMovementMethod;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;

public class ForumAdapter extends BaseQuickAdapter<ForumBean, BaseViewHolder> implements ClickEventHandler<ForumBean> {

    private BaseForumViewModel viewModel;
    //主页的发表和收藏 ,及 消息@我中不显示 关注按钮
    private boolean isShowBtn = true;
    //是不是从发表过来的 发过过来的不管是不是关注 都可以点击
    private boolean fromPublish;
    private boolean fromCollect;
    private String targetUserId;

    public void setViewModel(BaseForumViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setShowBtn(boolean showBtn) {
        isShowBtn = showBtn;
    }

    public void setFromPublish(boolean fromPublish) {
        this.fromPublish = fromPublish;
    }

    public void setFromCollect(boolean fromCollect) {
        this.fromCollect = fromCollect;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public ForumAdapter() {
        super(R.layout.item_forum_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, ForumBean item) {
        Context context = helper.itemView.getContext();
        ItemForumListLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setBaseForumViewModel(viewModel);
        binding.setIsSelf(TextUtils.equals(item.getUser_id(), AccountHelper.getUserId()));
        binding.setListener(this);

        binding.tvContent.setMovementMethod(HDLinkMovementMethod.getInstance());
        binding.tvContent.setText(item.getContentBuilder());

        if (fromCollect) {
            item.setCreate_time(-1);
            binding.tvTime.setText(item.getTime());
        }

        binding.executePendingBindings();
        //如果 不是自己的主页、则判断是不是发表和收藏
        if (!TextUtils.equals(item.getUser_id(), AccountHelper.getUserId()))
            binding.tvAttention.setVisibility(isShowBtn ? View.VISIBLE : View.GONE);

    }

    @Override
    public void handleClick(View view, ForumBean bean) {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        if (bean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.root:
            case R.id.tv_content:
            case R.id.tv_comment:

                if (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                CommentContainerActivity.start(view.getContext(), bean.getId(), CommentContainerActivity.COMMENT_FORUM);
                break;
            case R.id.image_head:
            case R.id.tv_user_name_level:
            case R.id.tv_user_level:
                if (fromPublish) {
                    return;
                }

                if (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                int userType = bean.getUser_type();
                if (userType == 1) {
                    if (view.getContext().getClass() == UserHomePageActivity.class && TextUtils.equals(targetUserId, bean.getUser_id())) {
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
            case R.id.tv_attention:
                //如果是发表 可点击 点击返回上一个页面 enabled
                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }
                if (viewModel == null) {
                    return;
                }

                if (AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                int is_follow = bean.getIs_follow();
                //   //    是否关注   0 否  1 是
                if (is_follow == 0) { //未关注的情况下可以操作
                    viewModel.attention(bean, bean.getIs_follow() == 1);
                }
                break;
            case R.id.tv_favor:
                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }
                if (viewModel == null) {
                    return;
                }

                if (AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                if (AccountHelper.getIdentity() != 1) {
                    ToastUtils.showShort("只有普通用户才可以收藏");
                    return;
                }

                viewModel.forumCollect(bean, bean.getIs_collect() == 1);
                break;
            case R.id.tv_share:     //  分享

                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }

                if (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                ShareDialog shareDialog = new ShareDialog();
                shareDialog.setTargetId(bean.getId());
                shareDialog.setType(ShareDialog.SHARE_TYPPE_FORUM);

                shareDialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "ShareDialog");
                shareDialog
                        .setUrl(String.format("%1$sfind/%2$s",
                                TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl,
                                bean.getId()))
//                        .setUrl("http://api.inchengzi.com/find/" + bean.getId())
                        .setTitle(bean.getUsername() + "的帖子")
                        .setContent(bean.getContentBuilder().toString());
                if (TextUtils.equals(bean.getResource_type(), "1") && !bean.getPics().isEmpty()) {
                    shareDialog.setShareImage(bean.getPics().get(0));
                } else {
                    shareDialog.setShareImage(null);
                }
                break;
            case R.id.tv_thumbup:
                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }
                if (viewModel == null) {
                    return;
                }

                if (AccountHelper.getIdentity() > 1) {
                    int authStatus = viewModel.curUserAuthStatus.get();
                    if (authStatus < 0) {
                        ToastUtils.showShort("您尚未提交资质审核，无法操作！");
                        return;
                    }
                    if (authStatus != 1) {
                        ToastUtils.showShort("您尚未通过审核，无法操作！");
                        return;
                    }
                }

                viewModel.forumThumbUp(bean, bean.getIs_like() == 1);
                break;
        }
    }

}
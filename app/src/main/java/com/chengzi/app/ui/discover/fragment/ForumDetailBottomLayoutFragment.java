package com.chengzi.app.ui.discover.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.BuildConfig;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentForumDetailBottomLayoutBinding;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.ui.comment.viewmodel.CommentContainerViewModel;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.ForumDetailViewModel;

public class ForumDetailBottomLayoutFragment extends BaseFragment<FragmentForumDetailBottomLayoutBinding,
        ForumDetailViewModel> implements ClickEventHandler<ForumBean> {

    private CommentContainerViewModel commentContainerViewModel;

    public ForumDetailBottomLayoutFragment() {
    }

    public static ForumDetailBottomLayoutFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(ForumDetailBottomLayoutFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new ForumDetailBottomLayoutFragment();
            fragment.setArguments(args);
        }
        return (ForumDetailBottomLayoutFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_forum_detail_bottom_layout;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        commentContainerViewModel = ViewModelProviders.of(getActivity())
                .get(CommentContainerViewModel.class);

        binding.setViewModel(commentContainerViewModel);
        binding.setListener(this);
        binding.setDetailViewModel(viewModel);

        observe();
    }

    private void observe() {

        viewModel.setLifecycleOwner(this);

        viewModel.forumDetailLive.observe(this, forumBean -> {
            binding.setBean(forumBean);
        });
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
            case R.id.tv_favor:  //  收藏
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (AccountHelper.getIdentity() != 1) {
                    ToastUtils.showShort("只有普通用户才可以收藏");
                    return;
                }
                viewModel.forumCollect(bean, bean.getIs_collect() == 1);
                break;
            case R.id.tv_comment:  //  对帖子进行评论
                commentContainerViewModel.isComment.set(true);
                commentContainerViewModel.topComment();
                break;
            case R.id.tv_share:   //  分享

                if (AccountHelper.shouldLogin(view.getContext())) {
                    return;
                }

                ShareDialog shareDialog = new ShareDialog();
                shareDialog.setTargetId(bean.getId());
                shareDialog.setType(ShareDialog.SHARE_TYPPE_FORUM);
                shareDialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "ShareDialog");
                shareDialog
                        .setUrl(String.format("%1$sfind/%2$s",
                                TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl,
                                bean.getId()))
//                        .setUrl("http://api.inchengzi.com/find/"+bean.getId())
                        .setTitle(bean.getUsername() + "的帖子")
                        .setContent(bean.getContentBuilder().toString());
                if (TextUtils.equals(bean.getResource_type(), "1") && !bean.getPics().isEmpty()) {
                    shareDialog.setShareImage(bean.getPics().get(0));
                }else{
                    shareDialog.setShareImage(null);
                }
                break;
            case R.id.tv_thumbup:  //  点赞
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                viewModel.forumThumbUp(bean, bean.getIs_like() == 1);
                break;
        }
    }
}

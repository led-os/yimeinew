package com.chengzi.app.ui.discover.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentForumDetailBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.ForumDelEvent;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.common.activity.VideoPlayActivity;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.ForumDetailViewModel;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 帖子详情
 *
 * @ClassName:ForumDetailFragment
 * @PackageName:com.yimei.app.ui.discover.fragment
 * @Create On 2019/4/22 0022   11:46
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/22 0022 handongkeji All rights reserved.
 */
public class ForumDetailFragment extends BaseFragment<FragmentForumDetailBinding, ForumDetailViewModel>
        implements ClickEventHandler<ForumBean>, OnRefreshListener {


    public static final String EXTRA_DETAIL_ID = "extra_detail_id";

    public ForumDetailFragment() {
    }

    public static ForumDetailFragment newInstance(FragmentManager fm, String forumId) {
        Fragment fragment = fm.findFragmentByTag(ForumDetailFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putString(EXTRA_DETAIL_ID, forumId);
            fragment = new ForumDetailFragment();
            fragment.setArguments(args);
        }
        return (ForumDetailFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_forum_detail;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        String forumId = getArguments().getString(EXTRA_DETAIL_ID);
        viewModel.setForumId(forumId);

        binding.setViewModel(viewModel);
        binding.setListener(this);
        binding.setIsSelf(true);

        observe();

        viewModel.getForumDetail(viewModel.getForumId());
    }

    private void observe() {

        viewModel.forumDetailLive.observe(this, forumBean -> {
            binding.setBean(forumBean);

            boolean isSelf = TextUtils.equals(AccountHelper.getUserId(), forumBean.getUser_id());
            binding.setIsSelf(isSelf);
            viewModel.setForumBean(forumBean);

            if (isSelf) {
                ((CommentContainerActivity) getActivity()).setUpTopBar(R.drawable.shanchu, () -> {
                    new MessageDialogBuilder(getActivity())
                            .setMessage("确定要删除吗？")
                            .setSureListener(v -> {
                                showLoading("");
                                viewModel.deleteForum();
                            }).show();
                });
            }

            setUpImageAndVideo(forumBean);

            binding.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            binding.tvContent.setText(forumBean.getContentBuilder());

            ((CommentContainerActivity) getActivity()).setUpCommentNumber(Integer.valueOf(forumBean.getComment_count()));

        });

        viewModel.delForumLive.observe(this, aBoolean -> {
            dismissLoading();
            ToastUtils.showShort("删除成功");
            EventBus.getDefault().post(new ForumDelEvent(viewModel.getForumId()));
            getActivity().finish();
        });
    }

    private void setUpImageAndVideo(ForumBean bean) {
        String resourceType = bean.getResource_type();
        if (TextUtils.equals(resourceType, "2")) {
            binding.btnPlay.setVisibility(View.VISIBLE);
            binding.videoImage.setVisibility(View.VISIBLE);
            binding.banner.setVisibility(View.GONE);
            com.nevermore.oceans.uits.ImageLoader
                    .loadImage(binding.videoImage, bean.getVideo(), 0, R.color.gray);
        } else {
            binding.btnPlay.setVisibility(View.GONE);
            binding.videoImage.setVisibility(View.GONE);
            binding.banner.setVisibility(View.VISIBLE);
            binding.banner.isAutoPlay(false)
                    .setBannerStyle(BannerConfig.NUM_INDICATOR)
                    .setImageLoader(new ImageLoader() {
                        @Override
                        public void displayImage(Context context, Object path, ImageView imageView) {
                            com.nevermore.oceans.uits.ImageLoader
                                    .loadImage(imageView, (String) path, 0, R.color.gray);
                        }
                    })
                    .setImages(bean.getPics())
                    .start();
            binding.banner.setOnBannerListener(position -> {
                PictureSwitcherActivity.start(getActivity(), (ArrayList<String>) bean.getPics(),position);
            });
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 3) {
            return;
        }
        if (viewModel.getForumBean() != null && TextUtils.equals(viewModel.getForumBean().getId(), event.getTarget())) {
            viewModel.getForumDetail(viewModel.getForumId());
        }
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 3) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        if (viewModel.getForumBean() != null && TextUtils.equals(viewModel.getForumBean().getId(), event.getTarget())) {
            viewModel.getForumDetail(viewModel.getForumId());
        }

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
            case R.id.image_head:
            case R.id.tv_name:      //  点击进入 主页 医生/咨询师/医院/普通用户

                int userType = bean.getUser_type();
                if (userType == 1) {
                    UserHomePageActivity.start(getActivity(), bean.getUser_id());
                } else if (userType == 2) {
                    DoctorHomePageActivity.startDoctor(getActivity(), bean.getUser_id());
                } else if (userType == 3) {
                    DoctorHomePageActivity.startCounselor(getActivity(), bean.getUser_id());
                } else {
                    HospitalHomePageActivity.start(getActivity(), bean.getUser_id());
                }
                break;
            case R.id.btn_attention:
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                viewModel.attention(bean, bean.getIs_follow() == 1);
                break;
            case R.id.video_image:
            case R.id.btn_play:
                VideoPlayActivity.start(view.getContext(), bean.getVideo(), "");
                break;
        }
    }

    @Override
    public void onRefresh() {
        viewModel.getForumDetail(viewModel.getForumId());
    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((RefreshListenerRegistry) getActivity()).register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((RefreshListenerRegistry) getActivity()).unRegister(this);
    }
}

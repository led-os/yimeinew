package com.chengzi.app.ui.message.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityCommentAndThumbupBinding;
import com.chengzi.app.databinding.ItemCommentThumbupLayoutBinding;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.message.bean.CommentMsgBean;
import com.chengzi.app.ui.message.viewmodel.CommentAndThumbupViewModel;
import com.chengzi.app.ui.peopleraise.activity.PeopleRaiseDetailActivity;

/**
 * 评论和赞
 *
 * @ClassName:ComentAndThumbupActivity
 * @PackageName:com.yimei.app.ui.message.activity
 * @Create On 2019/4/11 0011   17:11
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class CommentAndThumbupActivity extends BaseActivity<ActivityCommentAndThumbupBinding,
        CommentAndThumbupViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_comment_and_thumbup;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        MyAdapter adapter = new MyAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        mViewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });
        helper.start();
    }

    private static class MyAdapter extends BaseQuickAdapter<CommentMsgBean, BaseViewHolder> implements ClickEventHandler<CommentMsgBean> {

        public MyAdapter() {
            super(R.layout.item_comment_thumbup_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CommentMsgBean item) {
            ItemCommentThumbupLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setListener(this);
            layoutBinding.executePendingBindings();

            String data_resoure = item.getData_resoure();
            // find_comment  find_likes  find_reply  plan_comment plan_reply
            if (TextUtils.equals(data_resoure, "find_comment")) {

                layoutBinding.tvTypeText.setText("评论我：");
                CommentMsgBean.PublishContent publish_content = item.getPublish_content();
                if (publish_content != null) {
                    ImageLoader.loadImage(layoutBinding.image, publish_content.getImage(), 0, R.color.colorGray);
                    layoutBinding.tvTitle.setText(publish_content.getContentBuilder());
                }
                CommentMsgBean.Publisher publisher = item.getPublisher();
                if (publisher != null) {
                    layoutBinding.tvInitiator.setText("发布人：" + publisher.getName());
                }


            } else if (TextUtils.equals(data_resoure, "find_reply")) {

                layoutBinding.tvTypeText.setText("回复我：");
                CommentMsgBean.PublishContent publish_content = item.getPublish_content();
                if (publish_content != null) {
                    ImageLoader.loadImage(layoutBinding.image, publish_content.getImage(), 0, R.color.colorGray);
                    layoutBinding.tvTitle.setText(publish_content.getContentBuilder());
                }
                CommentMsgBean.Publisher publisher = item.getPublisher();
                if (publisher != null) {
                    layoutBinding.tvInitiator.setText("发布人：" + publisher.getName());
                }

            } else if (TextUtils.equals(data_resoure, "find_likes")) {

                layoutBinding.tvTypeText.setText("点赞");
                CommentMsgBean.PublishContent publish_content = item.getPublish_content();
                if (publish_content != null) {
                    ImageLoader.loadImage(layoutBinding.image, publish_content.getImage(), 0, R.color.colorGray);
                    layoutBinding.tvTitle.setText(publish_content.getContentBuilder());
                }
                CommentMsgBean.Publisher publisher = item.getPublisher();
                if (publisher != null) {
                    layoutBinding.tvInitiator.setText("发布人：" + publisher.getName());
                }

            } else if (TextUtils.equals(data_resoure, "plan_comment")) {

                layoutBinding.tvTypeText.setText("评论我：");
                layoutBinding.tvCommentContent.setText(item.getReply());

                layoutBinding.image.setImageResource(R.drawable.meirenchou);
                CommentMsgBean.PublishContent publish_content = item.getPublish_content();
                if (publish_content != null) {
                    layoutBinding.tvTitle.setText("美人筹类型：" + publish_content.getCategory_name());
                }
                CommentMsgBean.Publisher publisher = item.getPublisher();
                if (publisher != null) {
                    layoutBinding.tvInitiator.setText("发起人：" + publisher.getName());
                }

            } else if (TextUtils.equals(data_resoure, "plan_reply")) {

                layoutBinding.tvTypeText.setText("回复我：");
                layoutBinding.tvCommentContent.setText(item.getReply());

                layoutBinding.image.setImageResource(R.drawable.meirenchou);
                CommentMsgBean.PublishContent publish_content = item.getPublish_content();
                if (publish_content != null) {
                    layoutBinding.tvTitle.setText("美人筹类型：" + publish_content.getCategory_name());
                }
                CommentMsgBean.Publisher publisher = item.getPublisher();
                if (publisher != null) {
                    layoutBinding.tvInitiator.setText("发起人：" + publisher.getName());
                }
            }
        }

        @Override
        public void handleClick(View view, CommentMsgBean bean) {
            if (!ClickEvent.shouldClick(view)) {
                return;
            }
            switch (view.getId()) {
                case R.id.layout_user:
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
                case R.id.layout_target:
                    String data_resoure = bean.getData_resoure();
                    if (TextUtils.equals(data_resoure, "plan_comment") || TextUtils.equals(data_resoure, "plan_reply")) {
                        CommentMsgBean.PublishContent publish_content = bean.getPublish_content();
                        if (publish_content != null) {
                            PeopleRaiseDetailActivity.start(view.getContext(), publish_content.getId(), publish_content.getCategory_id());
                        }
                    } else {
                        CommentMsgBean.PublishContent publish_content = bean.getPublish_content();
                        if (publish_content != null) {
                            CommentContainerActivity.start(view.getContext(), publish_content.getId(), CommentContainerActivity.COMMENT_FORUM);
                        }
                    }
                    break;
            }
        }
    }
}

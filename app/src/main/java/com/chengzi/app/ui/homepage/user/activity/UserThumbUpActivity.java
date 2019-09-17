package com.chengzi.app.ui.homepage.user.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityUserThumbUpBinding;
import com.chengzi.app.databinding.ItemUserThumbupLayoutBinding;
import com.chengzi.app.ui.comment.activity.CommentContainerActivity;
import com.chengzi.app.ui.homepage.bean.ClickLikeListBean;
import com.chengzi.app.ui.homepage.viewmodel.UserThumbUpViewModel;

import java.util.ArrayList;

/**
 * 别人给用户点的赞
 *
 * @ClassName:UserThumbUpActivity
 * @PackageName:com.yimei.app.ui.homepage.user.activity
 * @Create On 2019/4/19 0019   17:33
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class UserThumbUpActivity extends BaseActivity<ActivityUserThumbUpBinding, UserThumbUpViewModel> {

    public static void start(Context context, String click_id) {
        Intent intent = new Intent(context, UserThumbUpActivity.class);
        intent.putExtra("click_id", click_id);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_thumb_up;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        PagingLoadHelper helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        UserThumbUpAdapter adapter = new UserThumbUpAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        String click_id = getIntent().getStringExtra("click_id");
        mViewModel.click_id.set(click_id);

        mViewModel.clickLikeListLiveData.observe(this, list -> {
            if (list != null && list.size() > 0) {
                helper.onComplete(list);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        helper.start();
        //帖子详情
        adapter.setOnItemClickListener((adapter1, view, position) ->
                CommentContainerActivity.start(this, adapter.getData().get(position).getFind_id(), CommentContainerActivity.COMMENT_FORUM)
        );
    }

    private static class UserThumbUpAdapter extends BaseQuickAdapter<ClickLikeListBean, BaseViewHolder> {

        public UserThumbUpAdapter() {
            super(R.layout.item_user_thumbup_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ClickLikeListBean item) {
            ItemUserThumbupLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            ClickLikeListBean.UserInfoEntity user_info = item.getUser_info();
            if (user_info != null) {
                //头像
                binding.setUrl(item.getUser_info().getImage());
            }
            //封面图
            ImageLoader.loadImage(binding.ivFind, item.getFind_image(), R.color.colorGray);
            binding.executePendingBindings();
        }
    }
}
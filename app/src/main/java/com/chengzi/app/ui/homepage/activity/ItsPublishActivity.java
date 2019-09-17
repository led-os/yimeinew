package com.chengzi.app.ui.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityItsPublishBinding;
import com.chengzi.app.ui.homepage.user.fragment.UserPublishFragment;

/**
 * 医生/咨询师/医院 发表的帖子
 *
 * @ClassName:ItsPublishActivity
 * @PackageName:com.yimei.app.ui.homepage.activity
 * @Create On 2019/4/19 0019   10:03
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class ItsPublishActivity extends BaseActivity<ActivityItsPublishBinding, BaseViewModel> {

    public static void start(Context context, String targetUserId) {
        Intent intent = new Intent(context, ItsPublishActivity.class);
        intent.putExtra("targetUserId", targetUserId);
        context.startActivity(intent);
    }

    private UserPublishFragment userPublishFragment;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_its_publish;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        String targetUserId = getIntent().getStringExtra("targetUserId");
        userPublishFragment = UserPublishFragment.newInstance(targetUserId, true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, userPublishFragment)
                .commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        userPublishFragment.onRefresh();
    }
}

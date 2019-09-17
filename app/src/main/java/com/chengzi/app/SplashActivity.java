package com.chengzi.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.chengzi.app.utils.NimUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;

/**
 * @ClassName:SplashActivity
 * @PackageName:com.yimei.app
 * @Create On 2019/4/28 0028   14:49
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/28 0028 handongkeji All rights reserved.
 */
public class SplashActivity extends BaseActivity<ViewDataBinding, BaseViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        String account = AccountHelper.getYunxinAccid();
        String token = AccountHelper.getYunxinToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            NimUtils.login(account,token,null);
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mBinding.getRoot(), View.ALPHA, 0, 1)
                .setDuration(1500);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                jump();
            }
        });
        objectAnimator.start();
    }

    private void jump(){
        String string = SPUtils.getInstance().getString(getClass().getName());
        if (TextUtils.isEmpty(string)) {
            startActivity(new Intent(this,GuidanceActivity.class));
            SPUtils.getInstance().put(getClass().getName(),"11");
        }else{
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.lzy.imagepicker.util.StatusBarUtil;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityLoginChooseBindingImpl;

/**
 * 登录前的选择身份
 *
 * @ClassName:LoginChooseActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   09:19
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */
public class LoginChooseActivity extends BaseActivity<ActivityLoginChooseBindingImpl, BaseViewModel> implements View.OnClickListener {

    /**
     * 默认是选择注册
     * 这个方法用户区分是三方
     *
     * @param context
     */
    private boolean isThirdBind = false;


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginChooseActivity.class);
        intent.putExtra("isThirdBind", true);

        context.startActivity(intent);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_login_choose;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this::onClick);
        isThirdBind = getIntent().getBooleanExtra("isThirdBind", false);
        //设置状态栏全透明
        StatusBarUtil.transparencyBar(this);
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = getResources().getDrawable(R.drawable.xuanzeshenfen_yixuan);
        switch (v.getId()) {
            case R.id.iv_finish:    //返回-->未选择用户身份状态
                AccountHelper.setIdentity(0);
                finish();
                break;
            case R.id.tv_user:      //普通用户
                mBinding.tvUser.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                mBinding.tvDoctor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvCounselor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvHospital.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                AccountHelper.setIdentity(Sys.TYPE_USER);
                break;
            case R.id.tv_doctor:    //医生
                mBinding.tvUser.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvDoctor.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                mBinding.tvCounselor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvHospital.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                AccountHelper.setIdentity(Sys.TYPE_DOCTOR);
                break;
            case R.id.tv_counselor: //咨询师
                mBinding.tvUser.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvDoctor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvCounselor.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                mBinding.tvHospital.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                AccountHelper.setIdentity(Sys.TYPE_COUNSELOR);
                break;
            case R.id.tv_hospital:  //医院
                mBinding.tvUser.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvDoctor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvCounselor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mBinding.tvHospital.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                AccountHelper.setIdentity(Sys.TYPE_HOSPITAL);
                break;
        }
        if (v.getId() != R.id.iv_finish) {
            if (!isThirdBind) { //注册
                RegisterActivity.start(this, 1);
                finish();
            } else {  //-->三方绑定 需要 綁定三方手機號
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    RegisterActivity.start(this, 2);
                } else {
                    goActivity(ThridBindPhoneActivity.class);
                }
            }
        }
    }
}
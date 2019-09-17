package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivitySignOrIntroduceBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

/**
 * 普通用户-->设置个性签名(20)
 * 医生/咨询师-->医生介绍(20)
 * 医院-->机构介绍(500)1
 *
 * @ClassName:SignOrIntroduceActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   16:35
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */

public class SignOrIntroduceActivity extends BaseActivity<ActivitySignOrIntroduceBindingImpl, EditInfoUserViewModel> {
    //普通用户->个签
    private String autographName = "";
    //医生/咨询师->介绍
    private String synopsis = "";

    private int identity = AccountHelper.getIdentity();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_sign_or_introduce;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        autographName = getIntent().getStringExtra("autographName");
        synopsis = getIntent().getStringExtra("synopsis");
        setInit();
        identity = AccountHelper.getIdentity();
    }


    private void setInit() {

        //除了设置个性签名 都显示btn外上间距40dp
        mBinding.view.setVisibility(identity == Sys.TYPE_USER ? View.GONE : View.VISIBLE);
        if (identity == Sys.TYPE_HOSPITAL) {
            // 机构介绍
            mBinding.topBar.setCenterText("机构介绍");
            mBinding.etContent.setHint("请用不超过500字介绍机构");

            mBinding.etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)}); //最大输入长度
            mBinding.tvSave.setText("确定");
        } else if (identity == Sys.TYPE_USER) {   //普通用户 设置个性签名
            mBinding.topBar.setCenterText("设置个性签名");
           /* mBinding.etContent.setHint("请填写个性签名，不超过20字");
            mBinding.etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
            mBinding.tvSave.setText("保存");*/
        } else if (identity == Sys.TYPE_DOCTOR || identity == Sys.TYPE_COUNSELOR) {   //医生/咨询师 医生介绍
            if (identity == Sys.TYPE_DOCTOR) {
                mBinding.topBar.setCenterText("医生介绍");
            } else {
                mBinding.topBar.setCenterText("咨询师介绍");
            }
            mBinding.etContent.setHint("请用不超过500字介绍你自己");
            mBinding.etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)}); //最大输入长度
            mBinding.tvSave.setText("确定");
        }
        //普通用户
        if (identity == Sys.TYPE_USER) {
            if (!TextUtils.isEmpty(autographName)) {
                mBinding.etContent.setText(autographName);
            }
        } else {//医生/咨询师/医院 用户
            if (!TextUtils.isEmpty(synopsis)) {
                mBinding.etContent.setText(synopsis);
            }
        }

        //修改数据成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                Intent intent = new Intent();
                intent.putExtra("sign_or_introduce", userInfoBean.getData().getAutographName());
                intent.putExtra("synopsis", userInfoBean.getData().getSynopsis());
                setResult(200, intent);
                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_save:      //保存
                String sign_or_introduce = mBinding.etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(sign_or_introduce)) {
                    showLoading(Sys.LOADING);
                    if (identity == Sys.TYPE_USER)
                        mViewModel.key_param.set("autograph");
                    else
                        mViewModel.key_param.set("synopsis");
                    mViewModel.updateInfo(sign_or_introduce);
                } else {
                    toast("请输入内容!");
                }
                break;
        }
    };
}
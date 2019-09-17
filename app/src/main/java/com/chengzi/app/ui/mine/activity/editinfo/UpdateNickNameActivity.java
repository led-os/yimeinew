package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityUpdateNickNameBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

/**
 * ①修改昵称
 * ②请输入运营负责人名称
 *
 * @ClassName:UpdateNickNameActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   15:23
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class UpdateNickNameActivity extends BaseActivity<ActivityUpdateNickNameBindingImpl, EditInfoUserViewModel> {
    @Override
    public int getLayoutRes() {
        return R.layout.activity_update_nick_name;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        //用户昵称 医院运营负责人
        String name = getIntent().getStringExtra("name");
        String hospital_opreation_name = getIntent().getStringExtra("hospital_opreation_name");
        if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
            mBinding.topBar.setCenterText("修改名称");
            mBinding.etNickName.setHint("请输入运营负责人名称");
            if (!TextUtils.isEmpty(hospital_opreation_name)) {
                mBinding.etNickName.setText(hospital_opreation_name);
            }
        } else {
            if (!TextUtils.isEmpty(name)) {
                mBinding.etNickName.setText(name);
            }
        }

        //修改数据成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                Intent intent = new Intent();
                intent.putExtra("nickname", userInfoBean.getData().getName());
                intent.putExtra("hospital_opreation_name", userInfoBean.getData().getHospital_opreation_name());
                setResult(200, intent);
                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_save:      //保存昵称  / 运营负责人
                String nickname = mBinding.etNickName.getText().toString().trim();
                if (!TextUtils.isEmpty(nickname)) {
                    showLoading(Sys.LOADING);
                    if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                        mViewModel.key_param.set("name");
                    } else {    //医院
                        mViewModel.key_param.set("hospital_opreation_name");
                    }
                    mViewModel.updateInfo(nickname);
                } else {
                    if (AccountHelper.getIdentity() == Sys.TYPE_USER)
                        toast("请输入昵称!");
                    else
                        toast("请输入运营负责人名称!");
                }
                break;
        }
    };
}
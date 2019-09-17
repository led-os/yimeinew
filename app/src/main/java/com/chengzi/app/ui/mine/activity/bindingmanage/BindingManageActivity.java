package com.chengzi.app.ui.mine.activity.bindingmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityBindingManageBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.mine.bean.BindHistoryBean;
import com.chengzi.app.ui.mine.viewmodel.BindingManageViewModel;
import com.chengzi.app.utils.NimUtils;

/**
 * 绑定管理
 *
 * @ClassName:BindingManageActivity
 * @PackageName:com.yimei.app.ui.mine.activity.bindingmanage
 * @Create On 2019/4/10 0010   16:10
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */

public class BindingManageActivity extends BaseActivity<ActivityBindingManageBindingImpl, BindingManageViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_binding_manage;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        //当前绑定
        showLoading(Sys.LOADING);
        mViewModel.bindHospital();
        mViewModel.bindHospitalLiveData.observe(this, bindHistoryBeans -> {
            dismissLoading();
            if (bindHistoryBeans != null && bindHistoryBeans.size() > 0) {
                mBinding.rlLayout.setVisibility(View.VISIBLE);
                BindHistoryBean bindHistoryBean = bindHistoryBeans.get(0);

                mBinding.include.setBean(bindHistoryBean);
                mBinding.include.setListener(clickListener);
                mBinding.include.setUrl(bindHistoryBean.getImage());

                mBinding.setBean(bindHistoryBean);
                mBinding.setListener(clickListener);
            } else {
                //隐藏绑定医院
                mBinding.rlLayout.setVisibility(View.GONE);
            }
        });
        //解绑成功
        mViewModel.bindClearLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("解绑成功!");
                NimUtils.doctorUnBind();
                //刷新机构
                mViewModel.bindHospital();
            } else {
                dismissLoading();
            }
        });
    }

    private ClickEventHandler<BindHistoryBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_unbind:  //  解绑
                if (bean != null && !TextUtils.isEmpty(bean.getBinding_id())) {
                    new MessageDialogBuilder(this)
                            .setMessage("确认解绑该机构？")
                            .setSureListener(v -> {

                                showLoading(Sys.LOADING);
                                mViewModel.bindClear(bean.getBinding_id());
                            })
                            .show();
                }
                break;
            case R.id.ll_bind_hospital_info:  //  绑定的医院信息
            case R.id.include://  绑定的医院信息
                HospitalHomePageActivity.start(this, bean.getBinding_id());
                break;
            case R.id.tv_binding_new_mechanism:  // 绑定新机构
                goActivity(BindingNewMechanismActivity.class);
                break;
            case R.id.tv_historical_record:  // 历史记录
                goActivity(BindHistoryRecordActivity.class);
                break;
        }
    };
}
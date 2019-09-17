package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityJobYearBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

/**
 * 从业年限
 *
 * @ClassName:JobYearActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   18:01
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class JobYearActivity extends BaseActivity<ActivityJobYearBindingImpl, EditInfoUserViewModel> {
    private String work_year = "0";

    @Override
    public int getLayoutRes() {
        return R.layout.activity_job_year;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        work_year = getIntent().getStringExtra("work_year");
        mBinding.etJobYear.setText(work_year);

        //修改数据成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                Intent intent = new Intent();
                intent.putExtra("work_year", userInfoBean.getData().getWork_year());
                setResult(200, intent);
                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_save:      //保存从业年限
                String work_year = mBinding.etJobYear.getText().toString().trim();
                if (!TextUtils.isEmpty(work_year)) {
                    showLoading(Sys.LOADING);
                    mViewModel.key_param.set("work_year");
                    mViewModel.updateInfo(work_year);
                } else {
                    toast("请输入从业年限!");
                }
                break;
        }
    };
}

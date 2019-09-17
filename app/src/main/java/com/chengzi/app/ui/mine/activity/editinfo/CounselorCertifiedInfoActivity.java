package com.chengzi.app.ui.mine.activity.editinfo;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCounselorCertifiedInfoBindingImpl;
import com.chengzi.app.ui.account.activity.CounselorCertifiedActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

/**
 * 咨询师认证信息
 *
 * @ClassName:CounselorCertifiedInfoActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/19 0019   10:41
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class CounselorCertifiedInfoActivity extends BaseActivity<ActivityCounselorCertifiedInfoBindingImpl, EditInfoUserViewModel> {

    private AuthenticationBean data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_counselor_certified_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showLoading(Sys.LOADING);
        mViewModel.authentication();


        mViewModel.authenticationLiveData.observe(this, authenticationBean -> {
            dismissLoading();
            if (authenticationBean != null && authenticationBean.getData() != null) {
                data = authenticationBean.getData();
                mBinding.setBean(data);
                //身份证正面 反面 手持
                ImageLoader.loadImage(mBinding.ivCardFront, data.getCard_front(), R.color.white);
                ImageLoader.loadImage(mBinding.ivCardReverse, data.getCard_reverse(), R.color.white);
                ImageLoader.loadImage(mBinding.ivCard, data.getCard(), R.color.white);
                //资质 其他资质
                ImageLoader.loadImage(mBinding.ivAptitudeImage, data.getAptitude_image(), R.color.white);
                ImageLoader.loadImage(mBinding.ivAptitudeOther, data.getAptitude_orther(), R.color.white);
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_recertification:      //重新认证
                CounselorCertifiedActivity.start(this, data, data.getInfo_id());
                break;
        }
    };
}
package com.chengzi.app.ui.mine.activity.editinfo;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDoctorCertifiedInfoBindingImpl;
import com.chengzi.app.ui.account.activity.DoctorCertifiedActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;

/**
 * 医生认证信息
 *
 * @ClassName:DoctorCertifiedInfoActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/19 0019   10:37
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class DoctorCertifiedInfoActivity extends BaseActivity<ActivityDoctorCertifiedInfoBindingImpl, EditInfoUserViewModel> {

    private AuthenticationBean data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_certified_info;
    }

    @Override
    protected void onResume() {
        super.onResume();

        showLoading(Sys.LOADING);
        mViewModel.authentication();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        mViewModel.authenticationLiveData.observe(this, authenticationBean -> {
            dismissLoading();
            if (authenticationBean != null && authenticationBean.getData() != null) {
                data = authenticationBean.getData();
                mBinding.setBean(data);
                //医生职业证书编号 医生职业证书编号地点
                ImageLoader.loadImage(mBinding.ivDoccertificateImage, data.getDoccertificate_image(), R.color.white);
                ImageLoader.loadImage(mBinding.ivDoccertificateImagea, data.getDoccertificate_imagea(), R.color.white);
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
//                goActivity(DoctorCertifiedActivity.class);
                DoctorCertifiedActivity.start(this, data,data.getInfo_id());
                break;
        }
    };
}

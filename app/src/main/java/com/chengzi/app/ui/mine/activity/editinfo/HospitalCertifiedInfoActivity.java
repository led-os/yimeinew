package com.chengzi.app.ui.mine.activity.editinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalCertifiedInfoBindingImpl;
import com.chengzi.app.ui.account.activity.HospitalCertifiedActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;

/**
 * 医院认证信息
 *
 * @ClassName:HospitalCertifiedInfoActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/19 0019   11:10
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class HospitalCertifiedInfoActivity extends BaseActivity<ActivityHospitalCertifiedInfoBindingImpl, EditInfoUserViewModel> {

    private AuthenticationBean data;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_certified_info;
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
                //医院名称 所属城市： 医院类型
                mBinding.tvName.setText("医院名称：" + data.getName());  //getTrue_name
                mBinding.tvCity.setText("所属城市：" + data.getHospital_city());
                mBinding.tvType.setText("医院类型：" + data.getHosipital_type_name());
                //医院营业执照 医院医疗结构许可证
                ImageLoader.loadImage(mBinding.ivBussinessAqtitudeImg, data.getBussiness_aqtitude_img(), R.color.white);
                ImageLoader.loadImage(mBinding.ivPermission, data.getPermission(), R.color.white);
                //身份证正面 反面 手持
                ImageLoader.loadImage(mBinding.ivCardFront, data.getCard_front(), R.color.white);
                ImageLoader.loadImage(mBinding.ivCardReverse, data.getCard_reverse(), R.color.white);
                ImageLoader.loadImage(mBinding.ivCard, data.getCard(), R.color.white);
                //医院医疗结构许可证
                ImageLoader.loadImage(mBinding.ivGuangshenAqtitudeImg, data.getGuangshen_aqtitude_img(), R.color.white);
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_recertification:      //重新认证
                goActivity(HospitalCertifiedActivity.class);
//                HospitalCertifiedActivity2.start(this,true);
                break;
        }
    };
}

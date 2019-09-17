package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityEditInfoHospitalBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.bean.GoodAtBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑信息-->医院
 *
 * @ClassName:EditInfoHospitalActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/19 0019   11:02
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class EditInfoHospitalActivity extends BaseActivity<ActivityEditInfoHospitalBindingImpl, EditInfoUserViewModel> {

    //擅长
    private String good_at_id;
    private String good_at_name;
    // 运营负责人 简介
    private String hospital_opreation_name, synopsis;
    //所属城市 地址
    private String city_name, address;
    private String latitude;
    private String longitude;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_info_hospital;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        observe();
    }

    private void observe() {
        //普通用户信息
        showLoading(Sys.LOADING);
        mViewModel.userInfo();
        mViewModel.userInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                UserInfoBean data = userInfoBean.getData();
                setData(data);
            }
        });

        //修改数据成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                UserInfoBean data = userInfoBean.getData();
                //存昵称 头像 性别
                AccountHelper.setNickname(data.getName());
                AccountHelper.setAvatar(data.getImage());
//                AccountHelper.setGender(data.getGender());
                AccountHelper.setCity(data.getCity_id(), data.getCity_name(), data.getLatitude(), data.getLongitude());
                setData(data);
                mViewModel.userInfo();
            }
        });
    }

    private void setData(UserInfoBean data) {
        mBinding.setBean(data);
        mBinding.setUrl(data.getImage());
        //运营负责人 机构名称  所属城市
        hospital_opreation_name = data.getHospital_opreation_name();
        String name = data.getName();   //getTrue_name
        mBinding.elNickName.setContent(hospital_opreation_name);
        mBinding.elHospiatlName.setContent(name);
        city_name = data.getCity_name();
        mBinding.elCity.setContent(city_name);

        latitude = data.getLatitude();
        longitude = data.getLongitude();
        //擅长
        good_at_id = data.getBegoodat();
        good_at_name = data.getBegoodat_name();
        mBinding.elGoodAt.setContent(data.getBegoodat_name());
        //机构介绍 机构地址
        synopsis = data.getSynopsis();
        mBinding.elIndividualitySignature.setContent(synopsis);
        address = data.getAddress();
        mBinding.elHospiatlAddress.setContent(address);

    }

    //选择的擅长list
    private List<GoodAtBean> chooseList = new ArrayList<>();
    public static final int REQUEST_NICK_NAME = 0x03;     //昵称
    public static final int REQUEST_INDIVIDUALITY_SIGN = 0x04;     //个性签名
    public static final int REQUEST_GOOD_AT = 0x05;     //擅长
    public static final int REQUEST_HOSPITAL_ADDTESS = 0x06;     //机构地址

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_head:      //头像
                CertifiedHelp.showDialog(this, false);
                break;
            case R.id.el_nick_name: //昵称-->运营负责人
                startActivityForResult(
                        new Intent(this, UpdateNickNameActivity.class)
                                .putExtra("hospital_opreation_name", hospital_opreation_name)
                        , REQUEST_NICK_NAME);
                break;
            case R.id.el_good_at: //擅长
                chooseList = new ArrayList<>();
                String[] splitid = good_at_id.split(",");
                String[] splitname = good_at_name.split(",");
                for (int i = 0; i < splitid.length; i++) {
                    chooseList.add(new GoodAtBean(splitid[i], splitname[i], true));
                }
                startActivityForResult(new Intent(this, GoodAtActivity.class)
                                .putExtra("chooseList", (Serializable) chooseList)
                        , REQUEST_GOOD_AT);
                break;
            case R.id.el_individuality_signature:  //医生介绍/咨询师介绍
                startActivityForResult(new Intent(this, SignOrIntroduceActivity.class)
                                .putExtra("synopsis", synopsis)
                        , REQUEST_INDIVIDUALITY_SIGN);
                break;
            case R.id.el_hospiatl_address:  //机构地址
                HospitalAddressActivity.startForResult(this, city_name, address,
                        Double.valueOf(TextUtils.isEmpty(latitude) ? "0" : latitude),
                        Double.valueOf(TextUtils.isEmpty(longitude) ? "0" : longitude), REQUEST_HOSPITAL_ADDTESS);

                break;
            case R.id.el_authentication_info:  //认证信息
                goActivity(HospitalCertifiedInfoActivity.class);
//                HospitalCertifiedActivity2.start(this, true);
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case Sys.REQUEST_CODE_PIC:
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                upload(picPath);
                break;
            case REQUEST_NICK_NAME: //运营负责人
                mViewModel.userInfo();
                break;
            case REQUEST_INDIVIDUALITY_SIGN:    //医院介绍
                synopsis = data.getStringExtra("synopsis");
                mBinding.elIndividualitySignature.setContent(synopsis);
                break;
            case REQUEST_GOOD_AT:    //擅长
                mViewModel.userInfo();
                break;
            case REQUEST_HOSPITAL_ADDTESS:    //机构地址
                String address = data.getStringExtra("address");
                mBinding.elHospiatlAddress.setContent(address);


//                double latitude = data.getDoubleExtra("latitude", 0);
//                double longitude = data.getDoubleExtra("longitude", 0);
//                Params params = Params.newParams()
//                        .put("hospital_address", address)
//                        .put("latitude", latitude + "")
//                        .put("longitude", longitude + "");
//                mViewModel.updateInfo(params.params());
                break;
        }
    }

    private void upload(String images) {
        showLoading(Sys.LOADING);
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String paths) {
                //修改头像
                mViewModel.key_param.set("image");
                mViewModel.updateInfo(paths);

                Map<UserInfoFieldEnum, Object> map = new HashMap<>();
                map.put(UserInfoFieldEnum.AVATAR, paths);
                NimUtils.updateUserInfo(map);
            }

            @Override
            public void onError(AliError errors) {
                dismissLoading();
                Log.d("ALI_UP", "onError: " + errors.toString());
                toast("上传失败");
            }
        });
    }
}

package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalCertifiedBindingImpl;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.account.viewmodel.HospitalCertifiedViewModel;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.home.activity.SelectCityActivity;
import com.chengzi.app.ui.home.bean.LocationBean;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 医院认证
 *
 * @ClassName:HospitalCertifiedActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   18:27
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */

public class HospitalCertifiedActivity extends BaseActivity<ActivityHospitalCertifiedBindingImpl, HospitalCertifiedViewModel> {

    private int ivPicType;
    private String choose_type;
    private LocationBean locationBean;

    public static final int REQUEST_CODE_SELECT_CITY = 0x05;


    //1表示修改
//    private String info_id = "";
    private AuthenticationBean data;

    //重新认证时 调用
    public static void start(Context context, AuthenticationBean data, String info_id) {
        context.startActivity(new Intent(context, HospitalCertifiedActivity.class)
                .putExtra("data", data)
                .putExtra("info_id", info_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_certified;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickEventHandler);
        mBinding.setViewModel(mViewModel);


        data = (AuthenticationBean) getIntent().getSerializableExtra("data");
//        info_id = getIntent().getStringExtra("info_id");
        if (data != null) {    // 重新认证 展示原来的数据
            //医院名称 医院人姓名
            mViewModel.real_name.set(data.getName());   //getTrue_name
            mBinding.etName.setText(data.getName());   //getTrue_name
            //医院名称不可修改
            mBinding.etName.setFocusable(false);
            mBinding.etName.setFocusableInTouchMode(false);
            //城市不能改


            mViewModel.hospital_opreation_name.set(data.getHospital_opreation_name());
            mBinding.etNamePeople.setText(data.getHospital_opreation_name());
            //城市 类型
            mViewModel.city_id.set(data.getCity_id());
            mBinding.elChooseCity.setContent(data.getHospital_city());
            mViewModel.hosipital_type.set(data.getHosipital_type());
            mBinding.elChooseType.setContent(data.getHosipital_type_name());
            //营业执照 医疗机构许可证
            String bussiness_aqtitude_img = data.getBussiness_aqtitude_img();
            String permission = data.getPermission();
            mViewModel.bussiness_aqtitude_img.set(bussiness_aqtitude_img);
            mViewModel.permission.set(permission);
            mBinding.ivBusiness1.setIv_image(bussiness_aqtitude_img);
            mBinding.ivBusiness2.setIv_image(bussiness_aqtitude_img);
            //身份证(正 反 手持)
            String card_front = data.getCard_front();
            String card_reverse = data.getCard_reverse();
            String card = data.getCard();
            mViewModel.card_front.set(card_front);
            mViewModel.card_reverse.set(card_reverse);
            mViewModel.cardUrl.set(card);
            mBinding.ivIdNumber1.setIv_image(card_front);
            mBinding.ivIdNumber2.setIv_image(card_reverse);
            mBinding.ivIdNumber3.setIv_image(card);
            //上传广审资质照片
            String guangshen_aqtitude_img = data.getGuangshen_aqtitude_img();
            mViewModel.guangshen_aqtitude_img.set(guangshen_aqtitude_img);
            mBinding.ivHonor.setIv_image(guangshen_aqtitude_img);
        }

        //认证成功
        mViewModel.responseLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                if (data == null) {
                    onBackPressed();
                } else {
//                    EditInfoDoctorOrCounselorActivity.types = 1;
//                    EditInfoDoctorOrCounselorActivity.trueName = mViewModel.real_name.get();
                }

                Map<UserInfoFieldEnum, Object> map = new HashMap<>();
                map.put(UserInfoFieldEnum.Name, mViewModel.real_name.get());
                NimUtils.updateUserInfo(map);

                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_business1:   //上传经营资质照片-->营业执照
                ivPicType = 1;
                break;
            case R.id.iv_business2:   //上传经营资质照片 -->医疗机构许可证
                ivPicType = 2;
                break;
            case R.id.iv_id_number1:   //上传身份证照片 -->身份证正面
                ivPicType = 3;
                break;
            case R.id.iv_id_number2:   //上传身份证照片 -->身份证反面
                ivPicType = 4;
                break;
            case R.id.iv_id_number3:   //上传身份证照片 -->手持身份证照片
                ivPicType = 5;
                break;
            case R.id.iv_honor:   //上传广审资质照片
                ivPicType = 6;
                break;
            case R.id.el_choose_city:   //医院所属城市
                if (data == null) {
                    startActivityForResult(new Intent(this, SelectCityActivity.class)
                                    .putExtra("location", locationBean),
                            REQUEST_CODE_SELECT_CITY);
                }
                break;
            case R.id.el_choose_type:   //医院所属类型
                new SelectListDialog(this, getChooseType(), choose_type)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                choose_type = cause;
                                mViewModel.hosipital_type.set(String.valueOf(causeid));
                                mBinding.elChooseType.setContent(cause);
                            }
                        })
                        .setShowCancle(true)
                        .setTvCancle("取消")
                        .show();
                break;
            case R.id.tv_submit:   //提交
                if (TextUtils.isEmpty(mViewModel.real_name.get())) {
                    toast("请输入医院名称");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.hospital_opreation_name.get())) {
                    toast("请输入医院运营人姓名");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.city_id.get())) {
                    toast("请选择医院所属城市");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.hosipital_type.get())) {
                    toast("请选择医院所属类型");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.bussiness_aqtitude_img.get())) {
                    toast("请上传医院营业执照");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.permission.get())) {
                    toast("请上传医疗结构许可证");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.card_front.get())) {
                    toast("请上传身份证正面照片");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.card_reverse.get())) {
                    toast("请上传身份证反面照片");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.cardUrl.get())) {
                    toast("请上传手持身份证照片");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.guangshen_aqtitude_img.get())) {
                    toast("请上传广审资质照片");
                    return;
                }
                showLoading(Sys.LOADING);
//                if (!TextUtils.isEmpty(info_id)) {
//                    mViewModel.certified(info_id);
//                } else {
//                    mViewModel.certified();
//                }
                mViewModel.user_audit();
                break;

        }
        if (view.getId() != R.id.tv_submit && view.getId() != R.id.el_choose_city && view.getId() != R.id.el_choose_type) {
            //相机相册dilog 和拍照或选照片处理
            CertifiedHelp.showDialog(HospitalCertifiedActivity.this, true);
        }
    };

    private List<SelectListBean> getChooseType() {
        //1.民营机构 2.公司机构 3.品牌连锁 4.生活美容机构
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "民营机构"));
        strings.add(new SelectListBean(2, "公司机构"));
        strings.add(new SelectListBean(3, "品牌连锁"));
        strings.add(new SelectListBean(4, "生活美容机构"));
        return strings;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String picPath = "";
        switch (requestCode) {
            case Sys.REQUEST_CODE_PIC:
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                picPath = items.get(0).path;
                break;
            case Sys.REQUEST_CODE_CAMERA:
                picPath = data.getStringExtra(PHOTO_PATH);
                break;
            case REQUEST_CODE_SELECT_CITY:  //城市id
                CityBean cityBean = (CityBean) data.getSerializableExtra("city");
                String city_id = cityBean.getCity_id();
                String city_name = cityBean.getCity_name();
                mViewModel.city_id.set(city_id);
                mBinding.elChooseCity.setContent(city_name);
                locationBean = new LocationBean(city_id, city_name);
                break;
        }
        if (requestCode != REQUEST_CODE_SELECT_CITY) {
            if (ivPicType == 1) {
                mBinding.ivBusiness1.setIv_image(picPath);
//            mBinding.ivDoctor1.getIvImageTag().toString();
            } else if (ivPicType == 2) {
                mBinding.ivBusiness2.setIv_image(picPath);
//            mBinding.ivDoctor2.getIvImageTag().toString();
            } else if (ivPicType == 3) {
                mBinding.ivIdNumber1.setIv_image(picPath);
//            mBinding.ivIdNumber1.getIvImageTag().toString();
            } else if (ivPicType == 4) {
                mBinding.ivIdNumber2.setIv_image(picPath);
//            mBinding.ivIdNumber2.setTag(picPath);
            } else if (ivPicType == 5) {
                mBinding.ivIdNumber3.setIv_image(picPath);
//            mBinding.ivIdNumber3.setTag(picPath);
            } else if (ivPicType == 6) {
                mBinding.ivHonor.setIv_image(picPath);
//            mBinding.ivHonor1.setTag(picPath);
            }
            showLoading(Sys.LOADING);
            AliUpload.upload(System.currentTimeMillis() + "", picPath, new AliUpload.CallBack() {
                @Override
                public void onSuccess(String url) {
                    dismissLoading();
                    Log.d("AliUpload", "onSuccess: " + url);
                    switch (ivPicType) {
                        case 1: //医院营业执照
                            mViewModel.bussiness_aqtitude_img.set(url);
                            break;
                        case 2://医院医疗机构许可证
                            mViewModel.permission.set(url);
                            break;
                        case 3://身份证正面
                            mViewModel.card_front.set(url);
                            break;
                        case 4://身份证反面
                            mViewModel.card_reverse.set(url);
                            break;
                        case 5://手持身份证照片
                            mViewModel.cardUrl.set(url);
                            break;
                        case 6://广审表
                            mViewModel.guangshen_aqtitude_img.set(url);
                            break;
                    }
                }

                @Override
                public void onError(AliError aliError) {
                    dismissLoading();
                    Log.d("AliUpload", "onError: " + aliError.toString());
                }

            });
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
        goActivity(MainActivity.class);
    }


}
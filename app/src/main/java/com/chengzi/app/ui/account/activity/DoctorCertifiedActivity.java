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
import com.chengzi.app.databinding.ActivityDoctorCertifiedBindingImpl;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.account.viewmodel.CertifiedViewModel;
import com.chengzi.app.ui.mine.activity.editinfo.EditInfoDoctorOrCounselorActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 医生认证 /重新认证
 *
 * @ClassName:DoctorCertifiedActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   17:01
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */
public class DoctorCertifiedActivity extends BaseActivity<ActivityDoctorCertifiedBindingImpl, CertifiedViewModel> {

    private int ivPicType;
    //1表示修改
//    private String info_id = "";
    private AuthenticationBean data;

    private String choose_rank = "", choose_rank_id = "";


    //重新认证时 调用
    public static void start(Context context, AuthenticationBean data, String info_id) {
        context.startActivity(new Intent(context, DoctorCertifiedActivity.class)
                .putExtra("data", data)
                .putExtra("info_id", info_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_certified;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickEventHandler);
        mBinding.setViewModel(mViewModel);

        data = (AuthenticationBean) getIntent().getSerializableExtra("data");
//        info_id = getIntent().getStringExtra("info_id");
//        if (data != null && !TextUtils.isEmpty(info_id)) {    // 重新认证 展示原来的数据
        if (data != null) {    // 重新认证 展示原来的数据
            //真实姓名 资质编号 职级
            mViewModel.real_name.set(data.getName());   //getTrue_name
            mViewModel.qualnumber.set(data.getQualnumber());
            mViewModel.occupation_class.set(data.getOccupation_class());
            mBinding.elChooseRank.setContent(data.getOccupation_class_edit());
            //资质认证1 2
            String doccertificate_image = data.getDoccertificate_image();
            String doccertificate_imagea = data.getDoccertificate_imagea();
            mViewModel.doccertificate_image.set(doccertificate_image);
            mViewModel.doccertificate_imagea.set(doccertificate_imagea);
            mBinding.ivDoctor1.setIv_image(doccertificate_image);
            mBinding.ivDoctor2.setIv_image(doccertificate_imagea);
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
            //资质照片 其他资质
            String aptitude_image = data.getAptitude_image();
            String aptitude_orther = data.getAptitude_orther();
            mViewModel.aptitude_image.set(aptitude_image);
            mViewModel.aptitude_orther.set(aptitude_orther);
            mBinding.ivHonor1.setIv_image(aptitude_image);
            mBinding.ivHonor2.setIv_image(aptitude_orther);
        }

        //认证成功
        mViewModel.responseLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                if (TextUtils.isEmpty(info_id)) {
                if (data == null) {
                    onBackPressed();
                } else {
                    EditInfoDoctorOrCounselorActivity.types = 1;
                    EditInfoDoctorOrCounselorActivity.trueName = mViewModel.real_name.get();
                }

                Map<UserInfoFieldEnum, Object> map = new HashMap<>();
                map.put(UserInfoFieldEnum.Name, mViewModel.real_name.get());
                NimUtils.updateUserInfo(map);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
        goActivity(MainActivity.class);
    }

    private ClickEventHandler<Object> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_doctor1:   //上传医生执业证书
                ivPicType = 1;
                break;
            case R.id.iv_doctor2:   //上传医生执业证书 -->需包含执业地点信息
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
            case R.id.iv_honor1:   //上传荣誉资质照片 -->资质照片
                ivPicType = 6;
                break;
            case R.id.iv_honor2:   //上传荣誉资质照片 -->其他资质
                ivPicType = 7;
                break;
            case R.id.el_choose_rank:   //选择职级
                new SelectListDialog(this, getRank(), choose_rank)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                choose_rank = cause;
                                choose_rank_id = String.valueOf(causeid);
                                mViewModel.occupation_class.set(String.valueOf(choose_rank_id));
                                mBinding.elChooseRank.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.tv_submit:   //提交
                if (TextUtils.isEmpty(mViewModel.real_name.get())) {
                    toast("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.qualnumber.get())) {
                    toast("请输入资质编号");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.occupation_class.get())) {
                    toast("请选择医生职级");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.doccertificate_image.get())) {
                    toast("请上传医生执业资格证正面");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.doccertificate_imagea.get())) {
                    toast("请上传医生执业资格证副页");
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
                showLoading(Sys.LOADING);
//                if (!TextUtils.isEmpty(info_id)) {  //重新认证
//                    mViewModel.doctorRestart(info_id);
//                } else {                            //医生首次认证
//                    mViewModel.docotorCertified();
//                }
                mViewModel.user_audit();
                break;

        }
        if (view.getId() != R.id.tv_submit && view.getId() != R.id.el_choose_rank) {
            //相机相册dilog 和拍照或选照片处理
            CertifiedHelp.showDialog(DoctorCertifiedActivity.this, true);
        }
    };

    private List<SelectListBean> getRank() {
        List<SelectListBean> strings = new ArrayList<>();
        // 1主任医师 2副主任医师 3医师 4主治医师 5国外医师
        strings.add(new SelectListBean(1, "主任医师"));
        strings.add(new SelectListBean(2, "副主任医师"));
        strings.add(new SelectListBean(3, "医师"));
        strings.add(new SelectListBean(4, "主治医师"));
        strings.add(new SelectListBean(5, "国外医师"));
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
        }
        if (ivPicType == 1) {
            mBinding.ivDoctor1.setIv_image(picPath);
        } else if (ivPicType == 2) {
            mBinding.ivDoctor2.setIv_image(picPath);
        }
        if (ivPicType == 3) {
            mBinding.ivIdNumber1.setIv_image(picPath);
        } else if (ivPicType == 4) {
            mBinding.ivIdNumber2.setIv_image(picPath);
        } else if (ivPicType == 5) {
            mBinding.ivIdNumber3.setIv_image(picPath);
        } else if (ivPicType == 6) {
            mBinding.ivHonor1.setIv_image(picPath);
        } else if (ivPicType == 7) {
            mBinding.ivHonor2.setIv_image(picPath);
        }
        showLoading(Sys.LOADING);
        AliUpload.upload(System.currentTimeMillis() + "", picPath, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String url) {
                dismissLoading();
                Log.d("AliUpload", "onSuccess: " + url);
                switch (ivPicType) {
                    case 1://上传医生执业证书
                        mViewModel.doccertificate_image.set(url);
                        break;
                    case 2://上传医生执业证书
                        mViewModel.doccertificate_imagea.set(url);
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
                    case 6://资质照片
                        mViewModel.aptitude_image.set(url);
                        break;
                    case 7://其他资质
                        mViewModel.aptitude_orther.set(url);
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
package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCounselorCertifiedBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.account.viewmodel.CounselorCertifiedViewModel;
import com.chengzi.app.ui.mine.activity.editinfo.EditInfoDoctorOrCounselorActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 咨询师认证
 *
 * @ClassName:CounselorCertifiedActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/4/3 0003   14:30
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */

public class CounselorCertifiedActivity extends BaseActivity<ActivityCounselorCertifiedBindingImpl, CounselorCertifiedViewModel> {

    private int ivPicType;

    //修改
//    private String info_id = "";
    private AuthenticationBean data;


    //重新认证时 调用
    public static void start(Context context, AuthenticationBean data, String info_id) {
        context.startActivity(new Intent(context, CounselorCertifiedActivity.class)
                .putExtra("data", data)
                .putExtra("info_id", info_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_counselor_certified;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickEventHandler);
        mBinding.setViewModel(mViewModel);
        resultData();

        data = (AuthenticationBean) getIntent().getSerializableExtra("data");
//        info_id = getIntent().getStringExtra("info_id");
//        if (data != null && !TextUtils.isEmpty(info_id)) {    // 重新认证 展示原来的数据
        if (data != null) {    // 重新认证 展示原来的数据
            //真实姓名 资质编号 职级
            mViewModel.real_name.set(data.getName());   //getTrue_name
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
    }

    private void resultData() {

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


    private ClickEventHandler<Object> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
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
            case R.id.tv_submit:   //提交 ;
                if (TextUtils.isEmpty(mViewModel.real_name.get())) {
                    toast("请输入真实姓名");
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
        if (view.getId() != R.id.tv_submit) {
            //相机相册dilog 和拍照或选照片处理
            CertifiedHelp.showDialog(CounselorCertifiedActivity.this, true);
        }
    };

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
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), picPath, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String url) {
                dismissLoading();
                Log.d("ALI_UP", "onSuccess: " + url);
                switch (ivPicType) {
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
                Log.d("ALI_UP", "onError: " + aliError.toString());
                toast("上传失败");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finishAffinity();
        goActivity(MainActivity.class);
    }

}

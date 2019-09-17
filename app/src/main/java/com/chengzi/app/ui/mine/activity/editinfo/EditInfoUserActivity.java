package com.chengzi.app.ui.mine.activity.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bigkoo.pickerview.view.TimePickerView;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityEditInfoUserBindingImpl;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;
import com.chengzi.app.utils.TimePickerViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑信息-->普通用户
 *
 * @ClassName:EditInfoUserActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   14:42
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */

public class EditInfoUserActivity extends BaseActivity<ActivityEditInfoUserBindingImpl, EditInfoUserViewModel> {

    private TimePickerView timePickerView;
    private int year, month, day;
    private String name;
    private String autographName;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_info_user;
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
                //生日
                mBinding.elBirthday.setContent(data.getBirthday());
            }
        });

        //修改数据成功
        mViewModel.updateInfoLiveData.observe(this, userInfoBean -> {
            dismissLoading();
            if (userInfoBean != null && userInfoBean.getData() != null) {
                UserInfoBean data = userInfoBean.getData();
                setData(data);
                //存昵称 头像 性别
                AccountHelper.setNickname(data.getName());
                AccountHelper.setAvatar(data.getImage());
                AccountHelper.setGender(data.getGender());
                //生日
                mBinding.elBirthday.setContent(data.getBirthday());
            }
        });
    }

    private void setData(UserInfoBean data) {
        mBinding.setBean(data);
        mBinding.setUrl(data.getImage());
        //昵称 性别
        name = data.getName();
        mBinding.elNickName.setContent(name);
        String gender = data.getGender();
        if (gender.equals("1")) {
            mBinding.elSex.setContent("男");
        } else if (gender.equals("0")) {
            mBinding.elSex.setContent("女");
        } else {
            mBinding.elSex.setContent("未选择");
        }
        // 个性签名
        autographName = data.getAutographName();
        mBinding.elIndividualitySignature.setContent(autographName);
    }

    public static final int REQUEST_NICK_NAME = 0x03;     //昵称
    public static final int REQUEST_INDIVIDUALITY_SIGN = 0x04;     //个性签名

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_head:      //头像
                CertifiedHelp.showDialog(this, false);
                break;
            case R.id.el_nick_name: //昵称
                startActivityForResult(
                        new Intent(this, UpdateNickNameActivity.class)
                                .putExtra("name", name)
                        , REQUEST_NICK_NAME);
                break;
            case R.id.el_sex:       //性别//修改头像
                mViewModel.key_param.set("gender");
                new SelectDialog(this)
                        .setTvText1("男")
                        .setTvText2("女")
                        .setText1Listener(v -> {
                            showLoading(Sys.LOADING);
                            mViewModel.updateInfo("1");
                        })
                        .setText2Listener(v -> {
                            showLoading(Sys.LOADING);
                            mViewModel.updateInfo("0");
                        })
                        .setTvCancleVisibility(false)
                        .show();
                break;
            case R.id.el_birthday:  //生日
                if (timePickerView != null) {
                    timePickerView.show();
                } else {
                    timePickerView = TimePickerViewUtils.initTimePicker(this, new TimePickerViewUtils.OnUtilsTimeSelectListener() {
                        @Override
                        public void onTimeSelect(String time, int year1, int month1, int day1, String timestamp) {   //timestamp 10位数时间戳
                            mBinding.elBirthday.setContent(time);
                            year = year1;
                            month = month1;
                            day = day1;

                            showLoading(Sys.LOADING);
                            mViewModel.key_param.set("birthday");
                            mViewModel.updateInfo(year + "-" + month + "-" + day);
                        }
                    });
                    timePickerView.show();
                }
                break;
            case R.id.el_individuality_signature:  //个性签名
                startActivityForResult(new Intent(this, SignOrIntroduceActivity.class)
                                .putExtra("autographName", autographName)
                        , REQUEST_INDIVIDUALITY_SIGN);
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
            case Sys.REQUEST_CODE_PIC:  //头像
//                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                String picPath = items.get(0).path;
//                ImageLoader.loadImage(mBinding.ivHead, picPath);
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                upload(picPath);
                break;
            case REQUEST_NICK_NAME:     //昵称
                name = data.getStringExtra("nickname");
                mBinding.elNickName.setContent(name);

                Map<UserInfoFieldEnum, Object> map = new HashMap<>();
                map.put(UserInfoFieldEnum.Name, name);
                NimUtils.updateUserInfo(map);
                break;
            case REQUEST_INDIVIDUALITY_SIGN:    //简介
                autographName = data.getStringExtra("sign_or_introduce");
                mBinding.elIndividualitySignature.setContent(autographName);
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
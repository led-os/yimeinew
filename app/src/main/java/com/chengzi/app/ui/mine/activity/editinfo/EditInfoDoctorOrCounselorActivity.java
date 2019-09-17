package com.chengzi.app.ui.mine.activity.editinfo;

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
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityEditInfoDoctorOrCounselorBindingImpl;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.bean.GoodAtBean;
import com.chengzi.app.ui.mine.viewmodel.EditInfoUserViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑信息-->医生/咨询师
 *
 * @ClassName:EditInfoDoctorOrCounselorActivity
 * @PackageName:com.yimei.app.ui.mine.activity.editinfo
 * @Create On 2019/4/18 0018   17:29
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class EditInfoDoctorOrCounselorActivity extends BaseActivity<ActivityEditInfoDoctorOrCounselorBindingImpl, EditInfoUserViewModel> {

    private int identity = 0;
    private String name, synopsis;
    private String work_year;
    //擅长
    private String good_at_id;
    private String good_at_name;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_edit_info_doctor_or_counselor;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        //医生介绍/咨询师介绍el_individuality_signature
        identity = AccountHelper.getIdentity();
        mBinding.elIndividualitySignature.setSubject(identity == Sys.TYPE_DOCTOR ? "医生介绍" : "咨询师介绍");

        observe();
    }

    public static int types = 0;
    public static String trueName = "";

    @Override
    protected void onResume() {
        super.onResume();
        if (types == 1) {
            types = 0;
            name = trueName;
            mBinding.elNickName.setContent(name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                AccountHelper.setGender(data.getGender());
                setData(data);
            }
        });
    }

    private void setData(UserInfoBean data) {
        mBinding.setBean(data);
        mBinding.setUrl(data.getImage());
        //昵称 性别
        name = data.getName();     //getTrue_name
        mBinding.elNickName.setContent(name);
        String gender = data.getGender();
        if (gender.equals("1")) {
            mBinding.elSex.setContent("男");
        } else if (gender.equals("0")) {
            mBinding.elSex.setContent("女");
        } else {
            mBinding.elSex.setContent("未选择");
        }
        //从业年限 擅长
        work_year = data.getWork_year();
        mBinding.elYearAge.setContent(work_year + "年");
        good_at_id = data.getBegoodat();
        good_at_name = data.getBegoodat_name();
        mBinding.elGoodAt.setContent(data.getBegoodat_name());
        // 个性签名
        synopsis = data.getSynopsis();
        mBinding.elIndividualitySignature.setContent(synopsis);
    }


    //选择的擅长list
    private List<GoodAtBean> chooseList = new ArrayList<>();
    public static final int REQUEST_INDIVIDUALITY_SIGN = 0x04;     //个性签名
    public static final int REQUEST_YEAR_AGE = 0x05;     //从业年限
    public static final int REQUEST_GOOD_AT = 0x06;     //擅长

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_head:      //头像
                CertifiedHelp.showDialog(this, false);
                break;
            case R.id.el_sex:       //性别
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
            case R.id.el_year_age: //从业年限
                startActivityForResult(
                        new Intent(this, JobYearActivity.class)
                                .putExtra("work_year", work_year)
                        , REQUEST_YEAR_AGE);
                break;
            case R.id.el_good_at: //擅长
                chooseList = new ArrayList<>();
//                if (chooseList != null && chooseList.size() > 0) {
                String[] splitid = good_at_id.split(",");
                String[] splitname = good_at_name.split(",");
                for (int i = 0; i < splitid.length; i++) {
                    chooseList.add(new GoodAtBean(splitid[i], splitname[i], true));
                }
//                }
                startActivityForResult(new Intent(this, GoodAtActivity.class)
                                .putExtra("chooseList", (Serializable) chooseList)
                        , REQUEST_GOOD_AT);

                break;
            case R.id.el_individuality_signature:  //医生介绍/咨询师介绍
                startActivityForResult(new Intent(this, SignOrIntroduceActivity.class)
                                .putExtra("synopsis", synopsis)
                        , REQUEST_INDIVIDUALITY_SIGN);
                break;
            case R.id.el_authentication_info:  //认证信息
                goActivity(identity == Sys.TYPE_DOCTOR ? DoctorCertifiedInfoActivity.class : CounselorCertifiedInfoActivity.class);
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
                /*List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                ImageLoader.loadImage(mBinding.ivHead, picPath);*/
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String picPath = items.get(0).path;
                upload(picPath);
                break;
            case REQUEST_YEAR_AGE:  //从业年限
                work_year = data.getStringExtra("work_year");
                mBinding.elYearAge.setContent(work_year + "年");
                break;
            case REQUEST_INDIVIDUALITY_SIGN:    //医生/咨询师介绍
                synopsis = data.getStringExtra("synopsis");
                mBinding.elIndividualitySignature.setContent(synopsis);
                break;
            case REQUEST_GOOD_AT:    //擅长
                chooseList = (List<GoodAtBean>) data.getSerializableExtra("chooseList");
                String goodat = "";
                for (int i = 0; i < chooseList.size(); i++) {
                    if (chooseList.get(i).isChoose()) {
                        goodat = goodat + chooseList.get(i).getName() + ",";
                    }
                }
                if (!TextUtils.isEmpty(goodat)) {
                    goodat = goodat.substring(0, goodat.length() - 1);
                }
                mBinding.elGoodAt.setContent(goodat);
                mViewModel.userInfo();
                break;
        }
    }

    private void upload(String images) {
        showLoading(Sys.LOADING);
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String paths) {
//                dismissLoading();
//                ImageLoader.loadImage(mBinding.ivHead, paths);
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
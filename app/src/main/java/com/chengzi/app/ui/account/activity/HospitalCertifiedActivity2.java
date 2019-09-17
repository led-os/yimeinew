package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalCertifiedNewBinding;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.dialog.SingleChoiceDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.account.bean.BankCityBean;
import com.chengzi.app.ui.account.bean.BankNameBean;
import com.chengzi.app.ui.account.bean.HospitalCertifiedBean;
import com.chengzi.app.ui.account.bean.RebindProvince;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.account.viewmodel.HospitalCertifiedViewModel2;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.home.activity.SelectCityActivity;
import com.chengzi.app.ui.home.bean.LocationBean;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.NimUtils;
import com.chengzi.app.utils.TimePickerViewUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.handong.framework.base.BaseActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.nevermore.oceans.uits.EmailCondition;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.IDCardNoCondition;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;

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

public class HospitalCertifiedActivity2 extends BaseActivity<ActivityHospitalCertifiedNewBinding,
        HospitalCertifiedViewModel2> implements View.OnClickListener {

    private int ivPicType;
    private String choose_type;
    private LocationBean locationBean;
    private final RebindProvince rebindProvince = new RebindProvince();

    public static final int REQUEST_CODE_SELECT_CITY = 0x05;

    //重新认证时 调用
    public static void start(Context context, boolean recertification) {
        context.startActivity(new Intent(context, HospitalCertifiedActivity2.class)
                .putExtra("recertification", recertification));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_certified_new;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        observe();
        mViewModel.getAuditInfo();
        mViewModel.authentication();

    }

    private void observe() {
        //认证成功
        mViewModel.responseLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                if (data == null) {
//                    onBackPressed();
//                } else {
////                    EditInfoDoctorOrCounselorActivity.types = 1;
////                    EditInfoDoctorOrCounselorActivity.trueName = mViewModel.real_name.get();
//                }
                ToastUtils.showShort("提交成功，请等待审核");
                Map<UserInfoFieldEnum, Object> map = new HashMap<>();
                map.put(UserInfoFieldEnum.Name, mViewModel.real_name.get());
                NimUtils.updateUserInfo(map);

                finish();
            }
        });

        mViewModel.authenticationLiveData.observe(this, bean -> {
            showData(bean);
        });

        mViewModel.auditInfoLive.observe(this, auditInfoBean -> {
            List<BankCityBean> bank_citys = auditInfoBean.getBank_citys();
            rebindProvince.setList(bank_citys);
            rebindProvince.match();
            mViewModel.province = rebindProvince.getProvince();
            mViewModel.city = rebindProvince.getCity();
        });
    }


    private void showData(HospitalCertifiedBean data) {
        if (data != null) {    // 重新认证 展示原来的数据
            //医院名称 医院人姓名
            mViewModel.real_name.set(data.getName());   //getTrue_name
            mBinding.etName.setText(data.getName());   //getTrue_name
            //医院名称不可修改
            mBinding.etName.setFocusable(false);
            mBinding.etName.setFocusableInTouchMode(false);

            //城市不能改
            mBinding.elChooseCity.setEnabled(false);

            mViewModel.hospital_opreation_name.set(data.getHospital_opreation_name());
            mBinding.etNamePeople.setText(data.getHospital_opreation_name());

            //城市 类型
            mViewModel.city_id.set(data.getCity_id());
            mBinding.elChooseCity.setContent(data.getHospital_city());

            mViewModel.hosipital_type.set(data.getHosipital_type() + "");
            String[] hospitalTypeStr = {"民营机构", "公司机构", "品牌连锁", "生活美容机构"};
            int hospitalType = data.getHosipital_type() - 1;
            hospitalType = Math.max(0,Math.min(hospitalType,3));
            mBinding.elChooseType.setContent(hospitalTypeStr[hospitalType]);

            mViewModel.companyName.set(data.getEnterprise_name());
            mViewModel.creditCode.set(data.getEnterprise_origin_code());
            mViewModel.companyEmail.set(data.getCompany_email());

            String registerAmountName = data.getRisk_doc().getRegister_amount().getOption_name();
            String registerAmountValue = data.getRisk_doc().getRegister_amount().getOption_value();
            mViewModel.registerAmount = new Pair<>(registerAmountName, registerAmountValue);
            mBinding.tvRegisterAmount.setText(registerAmountName);

            String staffSizeName = data.getRisk_doc().getStaff_size().getOption_name();
            String staffSizeValue = data.getRisk_doc().getStaff_size().getOption_value();
            mViewModel.staffSize = new Pair<>(staffSizeName, staffSizeValue);
            mBinding.tvStaffSize.setText(staffSizeName);

            String officeSizeName = data.getRisk_doc().getOffice_space().getOption_name();
            String officeSizeValue = data.getRisk_doc().getOffice_space().getOption_value();
            mViewModel.officeSpace = new Pair<>(officeSizeName, officeSizeValue);
            mBinding.tvOfficeSpace.setText(officeSizeName);

            //营业执照 医疗机构许可证
            String bussiness_aqtitude_img = data.getBussiness_aqtitude_img();
            mViewModel.bussiness_aqtitude_img.set(bussiness_aqtitude_img);
            mBinding.ivBusiness1.setIv_image(bussiness_aqtitude_img);

            String permission = data.getPermission();
            mViewModel.permission.set(permission);
            mBinding.ivBusiness2.setIv_image(permission);

            mViewModel.legalPersonName.set(data.getLegal_name());
            mViewModel.legalPersonIdcardNumber.set(data.getLegal_id_card_num());
            mViewModel.idcardStartDate = data.getLegal_id_card_effective_date_start();
            mBinding.btnIdcardNumberStarttime.setText(data.getLegal_id_card_effective_date_start());
            mViewModel.idcardEndDate = data.getLegal_id_card_effective_date_end();
            mBinding.btnIdcardNumberEndtime.setText(data.getLegal_id_card_effective_date_end());


            //身份证(正 反 手持)
            String card_front = data.getCard_front();
            mViewModel.card_front.set(card_front);
            mBinding.ivIdNumber1.setIv_image(card_front);

            String card_reverse = data.getCard_reverse();
            mViewModel.card_reverse.set(card_reverse);
            mBinding.ivIdNumber2.setIv_image(card_reverse);

            String card = data.getCard();
            mViewModel.cardUrl.set(card);
//            mBinding.ivIdNumber3.setIv_image(card);

            mViewModel.bankCardNumber.set(data.getCompany_bank_card_num());
            mViewModel.bankData = new BankNameBean(data.getCompany_bank_name(), data.getCompany_bank_code());
            mBinding.btnSelectBank.setText(data.getCompany_bank_name());

            rebindProvince.setProvinceCode(data.getCompany_bank_province_code());
            rebindProvince.setCityCode(data.getCompany_bank_city_code());
            rebindProvince.match();
            mViewModel.province = rebindProvince.getProvince();
            mViewModel.city = rebindProvince.getCity();

            mBinding.btnSelectProvince.setText(data.getCompany_bank_province_name());
            mBinding.btnSelectCity.setText(data.getCompany_bank_city_name());

            mViewModel.bankBranchInfo.set(data.getCompany_branch_bank_name());
            mViewModel.reservedMobile.set(data.getCompany_bank_mobile());

            mViewModel.bankPermit.set(data.getCompany_open_account());
            mBinding.ivBankcard1.setIv_image(data.getCompany_open_account());

            mViewModel.bankCardFront.set(data.getCompany_bank_card());
            mBinding.ivBankcard2.setIv_image(data.getCompany_bank_card());


            //上传广审资质照片
            String guangshen_aqtitude_img = data.getGuangshen_aqtitude_img();
            mViewModel.guangshen_aqtitude_img.set(guangshen_aqtitude_img);
            mBinding.ivHonor.setIv_image(guangshen_aqtitude_img);
        }
    }


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
//                mBinding.ivIdNumber3.setIv_image(picPath);
//            mBinding.ivIdNumber3.setTag(picPath);
            } else if (ivPicType == 6) {
                mBinding.ivHonor.setIv_image(picPath);
//            mBinding.ivHonor1.setTag(picPath);
            } else if (ivPicType == 7) {
                mBinding.ivBankcard1.setIv_image(picPath);
            } else if (ivPicType == 8) {
                mBinding.ivBankcard2.setIv_image(picPath);
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
                        case 7: //  开户行许可证
                            mViewModel.bankPermit.set(url);
                            break;
                        case 8: //  银行卡正面
                            mViewModel.bankCardFront.set(url);
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
        if (getIntent().getBooleanExtra("recertification", false)) {
            super.onBackPressed();
        } else {
            finishAffinity();
            goActivity(MainActivity.class);
        }
    }


    @Override
    public void onClick(View view) {

        takePicture(view);

        switch (view.getId()) {

            case R.id.tv_register_amount:
                SingleChoiceDialog dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                mViewModel.auditInfoLive.observe(dialog, auditInfoBean -> {
                    List<SingleChoiceDialog.Choice> list = new ArrayList<>();
                    JsonObject options = auditInfoBean.getRisk_doc().getRegister_amount().getOptions();
                    for (Map.Entry<String, JsonElement> entry : options.entrySet()) {
                        list.add(new SingleChoiceDialog.ChoiceBean(entry.getValue().getAsString(), entry.getKey()));
                    }
                    String select = mViewModel.registerAmount != null ? mViewModel.registerAmount.first : null;
                    dialog.setData(list, select);
                });
                dialog.setSureListener((choice, name, value, position) -> {
                    mViewModel.registerAmount = new Pair<>(name, value);
                    mBinding.tvRegisterAmount.setText(name);
                });

                break;
            case R.id.tv_staff_size:
                dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                mViewModel.auditInfoLive.observe(dialog, auditInfoBean -> {
                    List<SingleChoiceDialog.Choice> list = new ArrayList<>();
                    JsonObject options = auditInfoBean.getRisk_doc().getStaff_size().getOptions();
                    for (Map.Entry<String, JsonElement> entry : options.entrySet()) {
                        list.add(new SingleChoiceDialog.ChoiceBean(entry.getValue().getAsString(), entry.getKey()));
                    }
                    String select = mViewModel.staffSize != null ? mViewModel.staffSize.first : null;
                    dialog.setData(list, select);
                });
                dialog.setSureListener((choice, name, value, position) -> {
                    mViewModel.staffSize = new Pair<>(name, value);
                    mBinding.tvStaffSize.setText(name);
                });

                break;
            case R.id.tv_office_space:
                dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                mViewModel.auditInfoLive.observe(dialog, auditInfoBean -> {
                    List<SingleChoiceDialog.Choice> list = new ArrayList<>();
                    JsonObject options = auditInfoBean.getRisk_doc().getOffice_space().getOptions();
                    for (Map.Entry<String, JsonElement> entry : options.entrySet()) {
                        list.add(new SingleChoiceDialog.ChoiceBean(entry.getValue().getAsString(), entry.getKey()));
                    }
                    String select = mViewModel.officeSpace != null ? mViewModel.officeSpace.first : null;
                    dialog.setData(list, select);
                });
                dialog.setSureListener((choice, name, value, position) -> {
                    mViewModel.officeSpace = new Pair<>(name, value);
                    mBinding.tvOfficeSpace.setText(name);
                });
                break;
            case R.id.btn_idcard_number_starttime:

                TimePickerView timePickerView = TimePickerViewUtils.showTimePicker(this, true, new TimePickerViewUtils.OnUtilsTimeSelectListener() {
                    @Override
                    public void onTimeSelect(String time, int year1, int month1, int day1, String timestamp) {   //timestamp 10位数时间戳
                        mViewModel.idcardStartDate = time;
                        mBinding.btnIdcardNumberStarttime.setText(time);
                    }
                });
                timePickerView.show();
                break;
            case R.id.btn_idcard_number_endtime:
                timePickerView = TimePickerViewUtils.showTimePicker(this, false, new TimePickerViewUtils.OnUtilsTimeSelectListener() {
                    @Override
                    public void onTimeSelect(String time, int year1, int month1, int day1, String timestamp) {   //timestamp 10位数时间戳
                        mViewModel.idcardEndDate = time;
                        mBinding.btnIdcardNumberEndtime.setText(time);
                    }
                });
                timePickerView.show();
                break;
            case R.id.btn_select_bank:
                dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                mViewModel.auditInfoLive.observe(dialog, auditInfoBean -> {
                    List<SingleChoiceDialog.Choice> list = new ArrayList<>(auditInfoBean.getBank_name_data());

                    String select = mViewModel.bankData != null ? mViewModel.bankData.getName() : null;
                    dialog.setData(list, select);
                });
                dialog.setSureListener((choice, name, value, position) -> {
                    mViewModel.bankData = (BankNameBean) choice;
                    mBinding.btnSelectBank.setText(name);
                });
                break;
            case R.id.btn_select_province:
                dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                mViewModel.auditInfoLive.observe(dialog, auditInfoBean -> {
                    List<SingleChoiceDialog.Choice> list = new ArrayList<>(auditInfoBean.getBank_citys());

                    String select = mViewModel.province != null ? mViewModel.province.getName() : null;
                    dialog.setData(list, select);
                });
                dialog.setSureListener((choice, name, value, position) -> {

                    if (mViewModel.province != null) {
                        if (!TextUtils.equals(mViewModel.province.getCode(), choice.getValue())) {
                            mViewModel.city = null;
                            mBinding.btnSelectCity.setText("");
                        }
                    }

                    mViewModel.province = (BankCityBean) choice;
                    mBinding.btnSelectProvince.setText(name);

                });
                break;
            case R.id.btn_select_city:
                if (mViewModel.province == null) {
                    ToastUtils.showShort("请先选择省份！");
                    return;
                }
                dialog = new SingleChoiceDialog();
                dialog.show(getSupportFragmentManager(), "SingleChoiceDialog");
                List<SingleChoiceDialog.Choice> list = new ArrayList<>(mViewModel.province.getChildrens());
                String select = mViewModel.city != null ? mViewModel.city.getName() : null;
                dialog.setData(list, select);

                dialog.setSureListener((choice, name, value, position) -> {
                    mViewModel.city = (BankCityBean) choice;
                    mBinding.btnSelectCity.setText(name);
                });
                break;
            case R.id.el_choose_city:   //医院所属城市
                startActivityForResult(new Intent(this, SelectCityActivity.class)
                                .putExtra("location", locationBean),
                        REQUEST_CODE_SELECT_CITY);
                break;
            case R.id.el_choose_type:   //医院所属类型
                new SelectListDialog(this, getChooseType(), choose_type)
                        .setSureListener((String cause, int causeid, int position) -> {
                            choose_type = cause;
                            mViewModel.hosipital_type.set(String.valueOf(causeid));
                            mBinding.elChooseType.setContent(cause);
                        })
                        .setShowCancle(true)
                        .setTvCancle("取消")
                        .show();
                break;
            case R.id.tv_submit:   //提交

                if (!checkCondition()) {
                    return;
                }

                showLoading(Sys.LOADING);
                mViewModel.user_audit();
                break;
            case R.id.iv_announcement:
            case R.id.tv_announcement:
                mViewModel.agreement.set(!mViewModel.agreement.get());
                break;
            case R.id.tv_user_agreement:
                LookVipIntroduceActivity.start(this, "17");
                break;

        }

    }

    private boolean checkCondition() {

        ContentChecker.Machine machine = ContentChecker.getCheckMachine();


        ContentChecker checker = ContentChecker.getChecker(new ContentBody("",
                mViewModel.real_name.get()))
                .addCondition(new NotEmptyCondition(this, "请输入医院名称"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.hospital_opreation_name.get()))
                .addCondition(new NotEmptyCondition(this, "请输入医院运营人姓名"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.city_id.get()))
                .addCondition(new NotEmptyCondition(this, "请选择医院所属城市"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.hosipital_type.get()))
                .addCondition(new NotEmptyCondition(this, "请选择医院所属类型"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.companyName.get()))
                .addCondition(new NotEmptyCondition(this, "请输入企业工商注册名称"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.creditCode.get()))
                .addCondition(new NotEmptyCondition(this, "请输入企业统一社会信用代码"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.companyEmail.get()))
                .addCondition(new NotEmptyCondition(this, "请输入企业相关邮箱"))
                .addCondition(new EmailCondition());
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.registerAmount != null ? mViewModel.registerAmount.second : null))
                .addCondition(new NotEmptyCondition(this, "请选择注册资金"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.staffSize != null ? mViewModel.staffSize.second : null))
                .addCondition(new NotEmptyCondition(this, "请选择员工规模"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.officeSpace != null ? mViewModel.officeSpace.second : null))
                .addCondition(new NotEmptyCondition(this, "请选择办公面积"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bussiness_aqtitude_img.get()))
                .addCondition(new NotEmptyCondition(this, "请上传医院营业执照"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.permission.get()))
                .addCondition(new NotEmptyCondition(this, "请上传医疗机构许可证"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.legalPersonName.get()))
                .addCondition(new NotEmptyCondition(this, "请输入法人姓名"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.legalPersonIdcardNumber.get()))
                .addCondition(new NotEmptyCondition(this, "请输入法人身份证号"))
                .addCondition(new IDCardNoCondition());
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.idcardStartDate))
                .addCondition(new NotEmptyCondition(this, "请选择法人身份证限期开始"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.idcardEndDate))
                .addCondition(new NotEmptyCondition(this, "请选择法人身份证限期结束"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.card_front.get()))
                .addCondition(new NotEmptyCondition(this, "请上传身份证正面照片"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.card_reverse.get()))
                .addCondition(new NotEmptyCondition(this, "请上传身份证反面照片"));
        machine.putChecker(checker);

//        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.cardUrl.get()))
//                .addCondition(new NotEmptyCondition(this, "请上传手持身份证照片"));
//        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bankCardNumber.get()))
                .addCondition(new NotEmptyCondition(this, "请输入开户行账号"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bankData != null ? mViewModel.bankData.getCode() : null))
                .addCondition(new NotEmptyCondition(this, "请选择开户行"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.province != null ? mViewModel.province.getCode() : null))
                .addCondition(new NotEmptyCondition(this, "请选择开户行所在省份"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.city != null ? mViewModel.city.getCode() : null))
                .addCondition(new NotEmptyCondition(this, "请选择开户行所在城市"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bankBranchInfo.get()))
                .addCondition(new NotEmptyCondition(this, "请输入开户行支行信息"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.reservedMobile.get()))
                .addCondition(new NotEmptyCondition(this, "请输入开户银行预留手机号"))
                .addCondition(new PhoneNumCondition());
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bankPermit.get()))
                .addCondition(new NotEmptyCondition(this, "请上传开户行许可证照片"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.bankCardFront.get()))
                .addCondition(new NotEmptyCondition(this, "请上传银行卡正面照片"));
        machine.putChecker(checker);

        checker = ContentChecker.getChecker(new ContentBody("", mViewModel.guangshen_aqtitude_img.get()))
                .addCondition(new NotEmptyCondition(this, "请上传广审资质照片"));
        machine.putChecker(checker);

        return machine.checkAll();
    }

    private void takePicture(View view) {
        boolean matched = false;
        switch (view.getId()) {
            case R.id.iv_business1:   //上传经营资质照片-->营业执照
                ivPicType = 1;
                matched = true;
                break;
            case R.id.iv_business2:   //上传经营资质照片 -->医疗机构许可证
                ivPicType = 2;
                matched = true;
                break;
            case R.id.iv_id_number1:   //上传身份证照片 -->身份证正面
                ivPicType = 3;
                matched = true;
                break;
            case R.id.iv_id_number2:   //上传身份证照片 -->身份证反面
                ivPicType = 4;
                matched = true;
                break;
            case R.id.iv_id_number3:   //上传身份证照片 -->手持身份证照片
                ivPicType = 5;
                matched = true;
                break;
            case R.id.iv_honor:   //上传广审资质照片
                ivPicType = 6;
                matched = true;
                break;
            case R.id.iv_bankcard1:     //  开户行许可证
                ivPicType = 7;
                matched = true;
                break;
            case R.id.iv_bankcard2:    //  银行卡正面(结算卡)
                ivPicType = 8;
                matched = true;
                break;
        }
        if (matched) {
            //相机相册dilog 和拍照或选照片处理
            CertifiedHelp.showDialog(HospitalCertifiedActivity2.this, true);
        }
    }

}
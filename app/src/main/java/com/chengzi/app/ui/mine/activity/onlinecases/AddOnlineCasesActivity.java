package com.chengzi.app.ui.mine.activity.onlinecases;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;

import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAddOnlineCasesBindingImpl;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.dialog.SelectListDialog;
import com.chengzi.app.dialog.SelectMediaDialog;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.account.bean.SelectListBean;
import com.chengzi.app.ui.common.VideoItem;
import com.chengzi.app.ui.common.activity.VideoGridActivity;
import com.chengzi.app.ui.discover.activity.PublishCameraActivity;
import com.chengzi.app.ui.mine.bean.CaseInfoBean;
import com.chengzi.app.ui.mine.viewmodel.OnlineCasesViewModel;
import com.chengzi.app.utils.CommonUtilis;
import com.chengzi.app.utils.TimePickerViewUtils;
import com.chengzi.app.widget.NineGridView;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.common.activity.VideoGridActivity.VIDEO_ITEM;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.RECORD_TYPE;
import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.VIDEO_PATH;

/**
 * 添加/编辑病例(填写病例信息)-->只有普通用户
 *
 * @ClassName:AddOnlineCasesActivity
 * @PackageName:com.yimei.app.ui.mine.activity.onlinecases
 * @Create On 2019/4/3 0003   14:21
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class AddOnlineCasesActivity extends BaseActivity<ActivityAddOnlineCasesBindingImpl, OnlineCasesViewModel> {


    private NineGridView.NineGridAdapter adapter;

    private TimePickerView timePickerView;

    private CaseInfoBean data;

    public static void start(Context context, CaseInfoBean caseInfoBean) {
        context.startActivity(new Intent(context, AddOnlineCasesActivity.class)
                .putExtra("data", caseInfoBean)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_online_cases;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(addOnlineCasesClickListener);
        mBinding.setModel(mViewModel);
        data = (CaseInfoBean) getIntent().getSerializableExtra("data");

        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.fankui_guanbi, mViewModel.items);

        mBinding.nineGridView.setAdapter(adapter);

        adapter.setListener(view -> {
//            openPicture();
            showSelectMediaDialog();
        });

        //如果data不为空 则为修改 否则为添加
        if (data != null && !TextUtils.isEmpty(data.getId())) {
            mBinding.setBean(data);
            mViewModel.user_name.set(data.getUser_name());
            mViewModel.user_sex.set(data.getUser_sex());
            mBinding.elSex.setContent(data.getUser_sex_name());
            mViewModel.user_codes.set(data.getUser_codes());
            mViewModel.phone.set(data.getPhone());
            mViewModel.user_birthday.set(data.getUser_birthday());
            mBinding.elBirthday.setContent(data.getUser_birthday_year_month_day());
            mViewModel.address.set(data.getAddress());
            mViewModel.height.set(data.getHeight());
            mViewModel.weight.set(data.getWeight());
            mViewModel.blood_type.set(data.getBlood_type());
            mBinding.elBloodType.setContent(data.getBlood_type_name());
            mViewModel.heart_rate.set(data.getHeart_rate());
            mViewModel.blood_pressure_low.set(data.getBlood_pressure_low());
            mViewModel.blood_pressure_high.set(data.getBlood_pressure_high());
            mViewModel.operation.set(data.getOperation());
            int operation_size = 0;
            try {
                operation_size = data.getOperation().length();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mBinding.elPreviousSurgicalExperience.setContent("已填写" + operation_size + "字");
            mViewModel.trauma.set(data.getTrauma());
            int trauma_size = 0;
            try {
                trauma_size = data.getTrauma().length();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mBinding.elTraumaticExperience.setContent("已填写" + trauma_size + "字");
            mViewModel.beauty.set(data.getBeauty());
            int beauty_size = 0;
            try {
                beauty_size = data.getBeauty().length();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mBinding.elBecomeBeautiful.setContent("已填写" + beauty_size + "字");
            mViewModel.allergy_drugs.set(data.getAllergy_drugs());
            mViewModel.sidease.set(data.getSidease());
            mViewModel.family_diseases.set(data.getFamily_diseases());
            mBinding.elHaveFamilialDisease.setContent(data.getFamily_diseases_name());
            mViewModel.smoke.set(data.getSmoke());
            mBinding.elSmokingFrequency.setContent(data.getSmoke_name());
            mViewModel.drink_wine.set(data.getDrink_wine());
            mBinding.elDrinkingFrequency.setContent(data.getDrink_wine_name());
            mViewModel.exercise.set(data.getExercise());
            mBinding.elExerciseFrequency.setContent(data.getExercise_name());

            //图片/还是视频 ( // 图片视频类型 0-图片视频都没有，1-图片 2-视频)
            String image_type = data.getImage_type();
            if (image_type.equals("1")) {
                List<String> images = data.getImage();
                if (images != null && images.size() > 0) {
//                mViewModel.items =(ArrayList<String>) images;
                    List<NineGridView.NineItem> nineItem = new ArrayList<>();
                    for (int i = 0; i < images.size(); i++) {
                        nineItem.add(new NineGridView.NineItem(images.get(i), 1));
                    }
                    adapter.addItem(nineItem);
                }
            } else if (image_type.equals("2")) {
                List<String> images = data.getImage();
                if (images != null && images.size() > 0) {
//                mViewModel.items =(ArrayList<String>) images;
                    List<NineGridView.NineItem> nineItem = new ArrayList<>();
                    nineItem.add(new NineGridView.NineItem(images.get(0), 2, data.getCover_image()));
                    adapter.addItem(nineItem);
                }
            }
            //编辑的情况下 身份证号不可修改
            mBinding.etIdNumber.setEnabled(false);
        }

        //添加病例回调
        mViewModel.addCaseLiveData.observe(this, bean -> {
            dismissLoading();
            if (bean != null && bean.isSuccess()) {
                finish();
            }
        });
    }

    private String str0 = "", str1 = "", str2 = "", str3 = "", str4 = "";

    // 血型 1-A 2-B 3-AB 4-O 5-其他
    private List<SelectListBean> getBloodType() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "A型"));
        strings.add(new SelectListBean(2, "B型"));
        strings.add(new SelectListBean(3, "AB型"));
        strings.add(new SelectListBean(4, "O型"));
        strings.add(new SelectListBean(5, "其他"));
        return strings;
    }

    //是否有家族遗传病 1-有 2-无
    private List<SelectListBean> getHaveFamilialDisease() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "是"));
        strings.add(new SelectListBean(2, "否"));
        return strings;
    }

    // 吸烟频率 1、不吸烟 2、3支/周 3、1-4支/天 4、一天超过5支
    private List<SelectListBean> getSmokingFrequency() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "不吸烟"));
        strings.add(new SelectListBean(2, "3支/周"));
        strings.add(new SelectListBean(3, "1-4支/天"));
        strings.add(new SelectListBean(4, "1天超过5支"));
        return strings;
    }

    // 饮酒频率 1-每年小于3/4次 2-每周小于3次 3-每天
    private List<SelectListBean> getDrinkingFrequency() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "每年<3-4次"));
        strings.add(new SelectListBean(2, "每周<3次"));
        strings.add(new SelectListBean(3, "每天"));
        return strings;
    }

    //锻炼频率 1-不锻炼 2-1次/周 3- 3次/周 4->3次/周(大于3次/周)
    private List<SelectListBean> getExerciseFrequency() {
        List<SelectListBean> strings = new ArrayList<>();
        strings.add(new SelectListBean(1, "不锻炼"));
        strings.add(new SelectListBean(2, "1次/周"));
        strings.add(new SelectListBean(2, "3次/周"));
        strings.add(new SelectListBean(2, ">3次/周"));
        return strings;
    }

    private final int PREVIOUS_SURGICAL_EXPERIENCE = 3; // 以往手术经历
    private final int TRAUMATIC_EXPERIENCE = 4;         // 外伤经历
    private final int BECOME_BEAUTIFUL = 5;             // 变美经历
    private ClickEventHandler<Object> addOnlineCasesClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.el_sex:    //性别    0女 1男
                new SelectDialog(this)
                        .setTvText1("男")
                        .setText1Listener(v -> {
                            mViewModel.user_sex.set("1");
                            mBinding.elSex.setContent("男");
                        })
                        .setTvText2("女")
                        .setText2Listener(v -> {
                            mViewModel.user_sex.set("2");
                            mBinding.elSex.setContent("女");
                        })
                        .setTvCancleVisibility(false)
                        .show();
                break;
            case R.id.el_birthday:    //生日
                CommonUtilis.hideSoftKeyboard(this);
                if (timePickerView != null) {
                    timePickerView.show();
                } else {
                    timePickerView = TimePickerViewUtils.initTimePicker(this, new TimePickerViewUtils.OnUtilsTimeSelectListener() {
                        @Override
                        public void onTimeSelect(String time, int year1, int month1, int day1, String timestamp) { //timestamp 10位数时间戳
                            mBinding.elBirthday.setContent(time);
                            mViewModel.user_birthday.set(timestamp);
                        }
                    });
                    timePickerView.show();
                }
                break;
            case R.id.el_blood_type:    //血型
                new SelectListDialog(this, getBloodType(), str0)    // 血型 1-A 2-B 3-AB 4-O 5-其他
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                str0 = cause;
                                mViewModel.blood_type.set(String.valueOf(causeid));
                                mBinding.elBloodType.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.el_previous_surgical_experience:    //以往手术经历
                Intent intent = new Intent(this, WriteExperienceActivity.class);
                intent.putExtra("title", "以往手术经历");
                intent.putExtra("content", "请填写手术史（限1000中文字）");
                intent.putExtra("content_title", mViewModel.operation.get());
                startActivityForResult(intent, PREVIOUS_SURGICAL_EXPERIENCE);
                break;
            case R.id.el_traumatic_experience:    //外伤经历
                intent = new Intent(this, WriteExperienceActivity.class);
                intent.putExtra("title", "外伤经历");
                intent.putExtra("content", "请填写外伤经历（限1000中文字）");
                intent.putExtra("content_title", mViewModel.trauma.get());
                startActivityForResult(intent, TRAUMATIC_EXPERIENCE);
                break;
            case R.id.el_become_beautiful:    //变美经历
                intent = new Intent(this, WriteExperienceActivity.class);
                intent.putExtra("title", "变美经历");
                intent.putExtra("content", "填写你的简要历程（手术和针剂），以便医生了解能按照您的情况进行设计和操作（限1000中文字）");
                intent.putExtra("content_title", mViewModel.beauty.get());
                startActivityForResult(intent, BECOME_BEAUTIFUL);
                break;
            case R.id.el_have_familial_disease:    //是否有家族遗传病
                new SelectListDialog(this, getHaveFamilialDisease(), str1)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                str1 = cause;
                                mViewModel.family_diseases.set(String.valueOf(causeid));
                                mBinding.elHaveFamilialDisease.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.el_smoking_frequency:    //吸烟频率  1、不吸烟 2、3支/周 3、1-4支/天 4、一天超过5支
                new SelectListDialog(this, getSmokingFrequency(), str2)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                str2 = cause;
                                mViewModel.smoke.set(String.valueOf(causeid));
                                mBinding.elSmokingFrequency.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.el_drinking_frequency:    //饮酒频率
                new SelectListDialog(this, getDrinkingFrequency(), str3)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                str3 = cause;
                                mViewModel.drink_wine.set(String.valueOf(causeid));
                                mBinding.elDrinkingFrequency.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.el_exercise_frequency:    //锻炼频率
                new SelectListDialog(this, getExerciseFrequency(), str4)
                        .setSureListener(new SelectListDialog.OnSelectListener() {
                            @Override
                            public void onSelect(String cause, int causeid, int position) {
                                str4 = cause;
                                mViewModel.exercise.set(String.valueOf(causeid));
                                mBinding.elExerciseFrequency.setContent(cause);
                            }
                        })
                        .show();
                break;
            case R.id.btn_photo:
                showSelectMediaDialog();
                break;
            case R.id.tv_submit:    //提交
                //姓名 性别 省份证号码 联系电话
                String user_name = mViewModel.user_name.get();
                String user_sex = mViewModel.user_sex.get();
                String user_codes = mViewModel.user_codes.get();
                String phone = mViewModel.phone.get();
                if (TextUtils.isEmpty(user_name) && TextUtils.isEmpty(user_sex) && TextUtils.isEmpty(user_codes) && TextUtils.isEmpty(phone)) {
                    toast("必填项不得为空!");
                    return;
                }
                showLoading(Sys.LOADING);
                mViewModel.postForm();
                break;
        }
    };

    private void showSelectMediaDialog() {
        SelectMediaDialog optionsDialog = new SelectMediaDialog();
        int type = !mViewModel.items.isEmpty() ? mViewModel.items.get(0).getType() : 0;
        if (type == 1) {   // 1：图片 2：视频
            optionsDialog.setOptions(SelectMediaDialog.PICTURE);
        } else if (type == 2) {
            optionsDialog.setOptions(SelectMediaDialog.CAMERA);
        } else {
            optionsDialog.setOptions(0);
        }
        optionsDialog.show(getSupportFragmentManager(), SelectMediaDialog.class.getSimpleName());
        optionsDialog.setListener(options -> {
            switch (options) {
                case SelectMediaDialog.CAMERA:
                    List<String> permissions = new ArrayList<>();
                    if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.RECORD_AUDIO)) {
                        permissions.add(Manifest.permission.RECORD_AUDIO);
                    }
                    if (!PermissionUtils.isGranted(Manifest.permission.CAMERA)) {
                        permissions.add(Manifest.permission.CAMERA);
                    }
                    PermissionUtils.permission(permissions.toArray(new String[permissions.size()]))
                            .callback(new PermissionUtils.SimpleCallback() {
                                @Override
                                public void onGranted() {
                                    openCamera();
                                }

                                @Override
                                public void onDenied() {

                                }
                            }).request();
                    break;
                case SelectMediaDialog.PICTURE:
                    openPicture();
                    break;
                case SelectMediaDialog.VIDEO:
                    openVideo();
                    break;
            }
        });
    }

    //地址回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            switch (requestCode) {
//                case Sys.REQUEST_CODE_PIC:
//                    List<ImageItem> complaintsitems = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                    ArrayList<String> list = new ArrayList<>();
//                    for (ImageItem item : complaintsitems) {
//                        list.add(new String(item.path));
//                    }
//                    upload(list);
//                    break;
                case REQUEST_CODE_PIC:
                    List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    List<NineGridView.NineItem> list = new ArrayList<>();
                    for (ImageItem item : items) {
                        list.add(new NineGridView.NineItem(item.path, 1));
                    }
                    uploadPic(list);
//                    adapter.addItem(list);
                    break;
                case REQUEST_CODE_VIDEO:
                    VideoItem videoItem = (VideoItem) data.getSerializableExtra(VIDEO_ITEM);
                    List<NineGridView.NineItem> list1 = new ArrayList<>();
                    list1.add(new NineGridView.NineItem(videoItem.path, 2));
                    uploadPic(list1);
//                    adapter.addItem(new NineGridView.NineItem(videoItem.path, 2));
                    break;
                case REQUEST_CODE_CAMERA:
                    String videoPath = data.getStringExtra(VIDEO_PATH);
                    String picPath = data.getStringExtra(PHOTO_PATH);
                    List<NineGridView.NineItem> list2 = new ArrayList<>();
                    if (TextUtils.isEmpty(videoPath)) {
//                        adapter.addItem(new NineGridView.NineItem(picPath, 1));
                        list2 = new ArrayList<>();
                        list2.add(new NineGridView.NineItem(picPath, 1));
                        uploadPic(list2);

                    } else {
                        list2 = new ArrayList<>();
//                        adapter.addItem(new NineGridView.NineItem(videoPath, 2));
                        list2.add(new NineGridView.NineItem(videoPath, 2));
                        uploadPic(list2);

                    }
                    break;
//                case Sys.REQUEST_CODE_CAMERA:
//                    String picPath = data.getStringExtra(PHOTO_PATH);
//                    upload(picPath);
//                    break;
                case PREVIOUS_SURGICAL_EXPERIENCE: //以往手术经历
                    String operation = data.getStringExtra("content");
                    mViewModel.operation.set(operation);
                    int length = operation.length();
                    mBinding.elPreviousSurgicalExperience.setContent("已填写" + length + "字");
                    break;
                case TRAUMATIC_EXPERIENCE: //外伤经历
                    String trauma = data.getStringExtra("content");
                    mViewModel.trauma.set(trauma);
                    int length_trauma = trauma.length();
                    mBinding.elTraumaticExperience.setContent("已填写" + length_trauma + "字");
                    break;
                case BECOME_BEAUTIFUL: //变美经历
                    String beauty = data.getStringExtra("content");
                    mViewModel.beauty.set(beauty);
                    int length_beauty = beauty.length();
                    mBinding.elBecomeBeautiful.setContent("已填写" + length_beauty + "字");
                    break;
            }
        }
    }

    private void uploadPic(List<NineGridView.NineItem> items) {
        showLoading(Sys.LOADING);
        List<String> paths = new ArrayList<>();
        for (NineGridView.NineItem item : items) {
            paths.add(item.getLocalPath());
        }
        AliUpload.multipleUpload(paths, new AliUpload.MultipleCallBack() {
            @Override
            public ArrayList<String> onSuccess(List<String> paths) {
                dismissLoading();
                for (int i = 0; i < paths.size(); i++) {
                    adapter.addItem(new NineGridView.NineItem(paths.get(i), items.get(i).getType()));
                }

                return null;
            }

            @Override
            public ArrayList<String> onError(List<AliError> errors) {
                dismissLoading();
                toast("图片或视频上传失败!");
                return null;
            }
        });
    }


    private void openCamera() {

        int type = 0;
        if (!mViewModel.items.isEmpty()) {
            type = mViewModel.items.get(0).getType();
        }

        if (type == 2) {  //  拍视频
            ToastUtils.showShort("最多一个视频！");
            return;
        }
        if (mViewModel.items.size() >= 9) {
            ToastUtils.showShort("最多九张图片！");
            return;
        }

        Intent intent = new Intent(this, PublishCameraActivity.class);
        intent.putExtra(RECORD_TYPE, type);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);

    }

    public static final int REQUEST_CODE_PIC = 0x01;
    public static final int REQUEST_CODE_CAMERA = 0x06;
    public static final int REQUEST_CODE_VIDEO = 0x07;

    private void openPicture() {
        if (mViewModel.items.size() >= 9) {
            ToastUtils.showShort("最多九张图片！");
            return;
        }
        int needPicNum = 9 - mViewModel.items.size();
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setMultiMode(true);
        imagePicker.setCrop(true);
        imagePicker.setShowCamera(true);
        if (needPicNum < 0 || needPicNum > 9) {
            needPicNum = 1;
        }
        imagePicker.setSelectLimit(needPicNum);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PIC);
    }

    private void openVideo() {
        if (!mViewModel.items.isEmpty()) {
            ToastUtils.showShort("最多一个视频！");
            return;
        }
        startActivityForResult(new Intent(this, VideoGridActivity.class), REQUEST_CODE_VIDEO);
    }
}
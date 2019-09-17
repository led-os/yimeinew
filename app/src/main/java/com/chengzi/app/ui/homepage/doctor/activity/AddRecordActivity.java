package com.chengzi.app.ui.homepage.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAddRecordBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.homepage.doctor.fragment.ProfessionalExperienceFragment;
import com.chengzi.app.ui.homepage.viewmodel.ProfessionExperienceViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.MyDateUtils;
import com.chengzi.app.widget.MyDatePicker;
import com.chengzi.app.widget.NineGridView2;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 添加履历
 *
 * @ClassName:AddRecordActivity
 * @PackageName:com.yimei.app.ui.homepage.doctor.activity
 * @Create On 2019/5/28 0028   11:07
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/28 0028 handongkeji All rights reserved.
 */

public class AddRecordActivity extends BaseActivity<ActivityAddRecordBindingImpl, ProfessionExperienceViewModel> implements MyDatePicker.TimePickerInterface {

    public static final int REQUEST_CODE_PIC = 0x01;
    public static final int REQUEST_CODE_CAMERA = 0x02;

    private NineGridView2.NineGridAdapter adapter;

    private String time = "";
    private int[] start = new int[3];
    private int[] end = new int[3];

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_record;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mViewModel.setDoctorId(getIntent().getStringExtra("doctorId"));

        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView2.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.close_circle, mViewModel.items);
        mBinding.nineGridView.setAdapter(adapter);
        adapter.setListener(view -> {
//            openPicture();
            if (mViewModel.items.size() >= 9) {
                ToastUtils.showShort("最多九张图片！");
                return;
            }
            CertifiedHelp.showNineDialog(this, true, 9 - mViewModel.items.size());
        });

        //确定添加履历 //userRecordAdd
        mBinding.topBar.setOnRightClickListener(v -> {
            String start_time = mBinding.elStartTime.getContent();
            if (TextUtils.isEmpty(start_time)) {
                toast("请选择开始时间!");
                return;
            }
            String end_time = mBinding.elEndTime.getContent();
            if (TextUtils.isEmpty(end_time)) {
                toast("请选择结束时间!");
                return;
            }
            String content = mBinding.etContent.getText().toString();
            if (TextUtils.isEmpty(content)) {
                toast("请输入内容!");
                return;
            }
            if (mViewModel.items == null || mViewModel.items.size() <= 0) {
                toast("请上传图片!");
                return;
            }
            if (start[0] > end[0]) {
                toast("开始时间不能小于结束时间!");
                return;
            } else {
                if (start[1] > end[1]) {
                    toast("开始时间不能小于结束时间!");
                    return;
                } else {
                    if (start[2] > end[2]) {
                        toast("开始时间不能小于结束时间!");
                        return;
                    }
                }
            }
            showLoading(Sys.LOADING);
            mViewModel.userRecordAdd(start_time, end_time, content);
        });

        mViewModel.userRecordAddLiveData.observe(this, bean -> {
            dismissLoading();
            if (bean != null && bean.isSuccess()) {
                toast("履历添加成功!");
                ProfessionalExperienceFragment.fragment.refresh();
                finish();
            }
        });
    }

    @Override
    public void positiveListener(int Year, int Month, int Day, int Hour, int Mint) {
//        String times = Year + "年" + Month + "月" + Day + "日";
        String times = Year + "-" + Month + "-" + Day;
        if (time.equals("start")) {
            mBinding.elStartTime.setContent(times);
            start[0] = Year;
            start[1] = Month;
            start[2] = Day;
        } else if (time.equals("end")) {
            mBinding.elEndTime.setContent(times);
            end[0] = Year;
            end[1] = Month;
            end[2] = Day;
        }
    }

    @Override
    public void negativeListener() {
        if (time.equals("start")) {
            mBinding.elStartTime.setSubject("选择时间");
        } else if (time.equals("end")) {
            mBinding.elEndTime.setSubject("选择时间");
        }
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        MyDatePicker myDatePicker = new MyDatePicker(this);
        String time2 = MyDateUtils.getTime();//当前时间
        String times = MyDateUtils.timesOne(time2);
        int yearMin = Integer.valueOf(times.substring(0, 4));
        int monthMin = Integer.valueOf(times.substring(5, 7));
        int dayMin = Integer.valueOf(times.substring(8, 10));
        switch (view.getId()) {
            case R.id.el_start_time: // 开始时间
                time = "start";
                myDatePicker.showDateAndTimePickerDialog(this.wdith, yearMin, monthMin, dayMin, 0, 0, "0", time2 + "", findViewById(R.id.ll_pop));
                break;
            case R.id.el_end_time:  // 结束时间
                time = "end";
                myDatePicker.showDateAndTimePickerDialog(this.wdith, yearMin, monthMin, dayMin, 0, 0, "0", time2 + "", findViewById(R.id.ll_pop));
                break;
            case R.id.btn_photo:  //选择
                if (mViewModel.items.size() >= 9) {
                    ToastUtils.showShort("最多九张图片！");
                    return;
                }
                CertifiedHelp.showNineDialog(this, true, 9 - mViewModel.items.size());
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
            case REQUEST_CODE_PIC:
                List<ImageItem> complaintsitems = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<String> list = new ArrayList<>();
                for (ImageItem item : complaintsitems) {
                    list.add(new String(item.path));
                }
                upload(list);
                break;
            case REQUEST_CODE_CAMERA:
                String picPath = data.getStringExtra(PHOTO_PATH);
                upload(picPath);
                break;
        }
    }

    private void upload(List<String> images) {
        showLoading(Sys.LOADING);
        AliUpload.multipleUpload(images, new AliUpload.MultipleCallBack() {
            @Override
            public ArrayList<String> onSuccess(List<String> paths) {
                dismissLoading();
                adapter.addItem((ArrayList<String>) paths);
                return null;
            }

            @Override
            public ArrayList<String> onError(List<AliError> errors) {
                dismissLoading();
                Log.d("ALI_UP", "onError: " + errors.toString());
                toast("上传失败");
                return null;
            }
        });
    }

    private void upload(String images) {
        showLoading(Sys.LOADING);
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String paths) {
                dismissLoading();
                adapter.addItem(paths);
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
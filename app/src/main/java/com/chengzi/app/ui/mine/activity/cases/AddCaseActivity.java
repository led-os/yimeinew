package com.chengzi.app.ui.mine.activity.cases;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityAddCaseBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.mine.viewmodel.AddCaseViewModel;
import com.chengzi.app.ui.search.activity.AllGoodsActivity;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.MyDateUtils;
import com.chengzi.app.utils.OrderStatusHelp;
import com.chengzi.app.widget.MyDatePicker;
import com.chengzi.app.widget.NineGridView2;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 添加案例 / 日记
 *
 * @ClassName:AddCaseActivity
 * @PackageName:com.yimei.app.ui.mine.activity.cases
 * @Create On 2019/4/17 0017   13:55
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/17 0017 handongkeji All rights reserved.
 */

public class AddCaseActivity extends BaseActivity<ActivityAddCaseBindingImpl, AddCaseViewModel>
        implements MyDatePicker.TimePickerInterface {
    private static final String TAG = AddCaseActivity.class.getSimpleName();
    //添加案例1 / 日记2
    private int type;//type=1 添加案例 type=2 添加日记
    //添加日记
    private String cases_id;

    public static void start(Context context, int type, String cases_id) {
        context.startActivity(new Intent(context, AddCaseActivity.class)
                .putExtra("type", type)
                .putExtra("cases_id", cases_id)

        );
    }

    public static final int REQUEST_CODE_PIC = 0x01;//相册
    public static final int REQUEST_CODE_CAMERA = 0x02;//拍照
    public static final int REQUEST_CODE_SELECT_CITY = 0x03;//选择案例类型
    private NineGridView2.NineGridAdapter adapter;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_case;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        type = getIntent().getIntExtra("type", -1);
        cases_id = getIntent().getStringExtra("cases_id");

        mBinding.topBar.setCenterText(type == 1 ? "添加案例" : "添加日记");
        //添加案例时 显示选择案例类型
        mBinding.elChooseCaseType.setVisibility(type == 1 ? View.VISIBLE : View.GONE);

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
        /*点击确定*/
        mBinding.topBar.setOnRightClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (type == 1) {//添加案例
                    if (TextUtils.isEmpty(mViewModel.cate_id1.get()) || TextUtils.isEmpty(mViewModel.cate_id2.get()) || TextUtils.isEmpty(mViewModel.cate_id3.get())) {
                        toast("请选择案例类型!");
                        return;
                    }
                    if (TextUtils.isEmpty(mBinding.etContent.getText().toString())) {
                        toast("请输入内容!");
                        return;
                    }
                    if (mViewModel.items == null || mViewModel.items.size() <= 0) {
                        toast("请上传图片!");
                        return;
                    }
                    showLoading(Sys.LOADING);
                    String content = mBinding.elChooseTime.getContent();
                    mViewModel.caseCreate(!TextUtils.isEmpty(content) ? MyDateUtils.dataOne1(content) : "", mBinding.etContent.getText().toString());

                } else {//添加日记
                    if (TextUtils.isEmpty(mBinding.elChooseTime.getContent())) {
                        toast("请选择时间!");
                        return;
                    }
                    if (TextUtils.isEmpty(mBinding.etContent.getText().toString())) {
                        toast("请输入内容!");
                        return;
                    }
                    if (mViewModel.items == null || mViewModel.items.size() <= 0) {
                        toast("请上传图片!");
                        return;
                    }
                    showLoading(Sys.LOADING);
                    String content = mBinding.elChooseTime.getContent();
                    //!TextUtils.isEmpty(content) ? MyDateUtils.dataOne1(content) : ""
                    mViewModel.caseNoteCreate(cases_id, mBinding.etContent.getText().toString(),
                            !TextUtils.isEmpty(content) ? MyDateUtils.dataOne1(content) : "");
                }
            }
        });
        /*上传案例*/
        mViewModel.caseCreateLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                toast("上传案例成功!");
                //刷新案例-->form=1-->案例管理
                OrderStatusHelp.refreshBeautyOrderList(1);
                finish();
            }
        });
        /*上传日记*/
        mViewModel.caseNoteCreateLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                toast("上传日记!");
                //刷新日记
                finish();
            }
        });
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.el_choose_case_type:      //选择案例类型
                startActivityForResult(new Intent(this, AllGoodsActivity.class)
                                .putExtra(Sys.EXTRA_FOR_CATEGORY, true)
                                .putExtra(Sys.EXTRA, true)   //只能选择三级分类
                        , REQUEST_CODE_SELECT_CITY);
                break;
            case R.id.el_choose_time:  // 选择时间
                MyDatePicker myDatePicker = new MyDatePicker(AddCaseActivity.this);
                String time2 = MyDateUtils.getTime();//当前时间
                String times = MyDateUtils.timesOne(time2);
                int yearMin = Integer.valueOf(times.substring(0, 4));
                int monthMin = Integer.valueOf(times.substring(5, 7));
                int dayMin = Integer.valueOf(times.substring(8, 10));

//                myDatePicker.showDateAndTimePickerDialog(this.wdith, 0, 0, 0, 0, 0, time2 + "", findViewById(R.id.ll_pop));
                myDatePicker.showDateAndTimePickerDialog(this.wdith, yearMin, monthMin, dayMin, 0, 0, "0", time2 + "", findViewById(R.id.ll_pop));
                break;
            case R.id.btn_photo:  // 选择
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
            case REQUEST_CODE_SELECT_CITY:      //选择类型
                Bundle bundle = data.getExtras();
                String first_id = bundle.getString("first_id");
                String second_id = bundle.getString("second_id");
                String third_id = bundle.getString("third_id");
                String categoryName = bundle.getString("categoryName");
                mViewModel.cate_id1.set(first_id);
                mViewModel.cate_id2.set(second_id);
                mViewModel.cate_id3.set(third_id);
                mBinding.elChooseCaseType.setContent(categoryName);
                break;
        }
    }

    /*选择的时间结果回调结果*/
    @Override
    public void positiveListener(int Year, int Month, int Day, int Hour, int Mint) {
        mBinding.elChooseTime.setContent(Year + "-" + Month + "-" + Day);
    }

    @Override
    public void negativeListener() {
        mBinding.elChooseTime.setSubject("选择时间");
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

package com.chengzi.app.ui.mine.activity.myorder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
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
import com.chengzi.app.databinding.ActivityComplaintsBindingImpl;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.mine.bean.GetEvaluationObjectBean;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.utils.CertifiedHelp;
import com.chengzi.app.utils.OrderStatusHelp;
import com.chengzi.app.widget.NineGridView2;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.discover.activity.PublishCameraActivity.PHOTO_PATH;

/**
 * 投诉
 * ① 普通用户->我的订单(普通/拼购订单)
 * ② 医院   -> 订单投诉
 *
 * @ClassName:ComplaintsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/4 0004   14:29
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class ComplaintsActivity extends BaseActivity<ActivityComplaintsBindingImpl, MyOrderListViewModel> {

    private String order_id;

    private int type;
    private NineGridView2.NineGridAdapter adapter;
    private GetEvaluationObjectBean data;

    //如果有选中的投诉人则true
    private boolean isSubmit = false;
    //投诉的用户的id
    private String complaint_id = "";

    /**
     * @param context
     * @param order_id
     * @param type     订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）     3-->医院
     */
    public static void start(Context context, String order_id, int type) {
        context.startActivity(new Intent(context, ComplaintsActivity.class)
                .putExtra("order_id", order_id)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_complaints;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(complaintsClickListener);
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", 3);
//        if (type == 3) {   //医院
//            mBinding.tvTitle.setText("投诉用户");
//        }
        if (!TextUtils.isEmpty(order_id)) {
            showLoading(Sys.LOADING);
            mViewModel.getEvaluationObject(order_id);
        } else {
            toast("订单不存在或非待投诉状态!");
            finish();
        }
        observe();
        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView2.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.close_circle, mViewModel.complaintsitems);
        mBinding.nineGridView.setAdapter(adapter);

        adapter.setListener(view -> {
            if (mViewModel.complaintsitems.size() >= 9) {
                ToastUtils.showShort("最多九张图片！");
                return;
            }
            CertifiedHelp.showNineDialog(this, true, 9 - mViewModel.complaintsitems.size());
        });
    }

    private void observe() {
        //获取评价对象
        mViewModel.getEvaluationObjectLive.observe(this, getEvaluationObjectBean -> {
            dismissLoading();
            if (getEvaluationObjectBean != null) {
                mBinding.setBean(getEvaluationObjectBean.getData());
                data = getEvaluationObjectBean.getData();
                mBinding.setBean(data);
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    //普通用户 可显示投诉对象： 医生 咨询师 机构
                    if (data.getDoctor_data() != null) {
                        mBinding.clDoctor.setVisibility(View.VISIBLE);
                        mBinding.setUrlDoctor(data.getDoctor_data().getImage());
                    }
                    if (data.getCounselling_data() != null) {
                        mBinding.clCounselor.setVisibility(View.VISIBLE);
                        mBinding.setUrlCounselor(data.getCounselling_data().getImage());
                    }
                    if (data.getHospital_data() != null) {
                        mBinding.clHospital.setVisibility(View.VISIBLE);
                        mBinding.setUrlHospital(data.getHospital_data().getImage());
                    }
                } else {
                    //机构     可显示投诉对象：普通用户
                    if (data.getUser_data() != null) {
                        mBinding.clUser.setVisibility(View.VISIBLE);
                        mBinding.setUrl(data.getUser_data().getImage());
                    }
                }
            } else {
                finish();
            }
        });

        //投诉
        mViewModel.complaintEventLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                //// 投诉--    注意刷新 ①普通用户需要区分->type=1||type=2 ②医院不需要区分->type=3
                toast("投诉成功");
                if (type == 3) {        //代表医院 刷新医院列表
                    //刷新订单列表  -->0-全部 1-待使用，2-待评价，3-已完成
                    //确认使用成功  //刷新-->医院状态下的 全部/(待使用)/待评价/已完成
                    OrderStatusHelp.refreshOrderList(0);
                    OrderStatusHelp.refreshOrderList(2);
                    OrderStatusHelp.refreshOrderList(3);
                } else {    //普通用户->区分type 刷新
                    //刷新订单列表  -->0全部 4-待评价 5-已完成
                    OrderStatusHelp.refreshOrderList(type, 0);
                    OrderStatusHelp.refreshOrderList(type, 4);
                    OrderStatusHelp.refreshOrderList(type, 5);
                }
                finish();
            }
        });
    }

    private ClickEventHandler<Object> complaintsClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.ll_doctor:  //  投诉医生
            case R.id.tv_doctor:
            case R.id.tv_doctor1:
            case R.id.iv_doctor:
                GetEvaluationObjectBean.DoctorDataEntity doctor_data = data.getDoctor_data();
                doctor_data.setIsChoose(doctor_data.getIsChoose() ? false : true);
                mBinding.ivDoctor.setSelected(doctor_data.getIsChoose());
                break;
            case R.id.ll_counselor:  //  咨询师
            case R.id.tv_counselor:
            case R.id.tv_counselor1:
            case R.id.iv_counselor:
                GetEvaluationObjectBean.DoctorDataEntity counselling_data = data.getCounselling_data();
                counselling_data.setIsChoose(counselling_data.getIsChoose() ? false : true);
                mBinding.ivCounselor.setSelected(counselling_data.getIsChoose());
                break;
            case R.id.ll_hospital:  //  机构
            case R.id.tv_hospital:
            case R.id.iv_hospital:
                GetEvaluationObjectBean.DoctorDataEntity hospital_data = data.getHospital_data();
                hospital_data.setIsChoose(hospital_data.getIsChoose() ? false : true);
                mBinding.ivHospital.setSelected(hospital_data.getIsChoose());
                break;
            case R.id.tv_submit:  //  提交
                isSubmit = false;
                complaint_id = "";
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    if (data.getDoctor_data() != null && data.getDoctor_data().getIsChoose()) {
                        isSubmit = true;
                        complaint_id = complaint_id + data.getDoctor_data().getId() + ",";
                    }
                    if (data.getCounselling_data() != null && data.getCounselling_data().getIsChoose()) {
                        isSubmit = true;
                        complaint_id = complaint_id + data.getCounselling_data().getId() + ",";
                    }
                    if (data.getHospital_data() != null && data.getHospital_data().getIsChoose()) {
                        isSubmit = true;
                        complaint_id = complaint_id + data.getHospital_data().getId() + ",";
                    }
                    if (!isSubmit && TextUtils.isEmpty(complaint_id)) {
                        toast("请勾选你的投诉对象!");
                        break;
                    }
                } else {
                    isSubmit = true;
                    complaint_id = data.getUser_data().getId();
                }
                String content = mBinding.etNumber.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    toast("请填写你的投诉内容!");
                    break;
                }
                showLoading(Sys.LOADING);
                mViewModel.complaintEvent(order_id, content, complaint_id);
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
                List<ImageItem> complaintsitems = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<String> list = new ArrayList<>();
                for (ImageItem item : complaintsitems) {
                    list.add(new String(item.path));
                }
                upload(list);
                break;
            case Sys.REQUEST_CODE_CAMERA:
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
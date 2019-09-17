package com.chengzi.app.ui.mine.activity.myorder;

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
import com.chengzi.app.databinding.ActivityEvaluationBindingImpl;
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
 * 评价
 *
 * @ClassName:EvaluationActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/4 0004   14:31
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class EvaluationActivity extends BaseActivity<ActivityEvaluationBindingImpl, MyOrderListViewModel> {

    //订单id
    private String order_id;
    //订单类型1-普通订单和秒杀订单 2-拼购订单 （必须）
    private int type;
    private NineGridView2.NineGridAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_evaluation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", -1);
        mBinding.setListener(clickListener);
        if (!TextUtils.isEmpty(order_id)) {
            showLoading(Sys.LOADING);
            mViewModel.getEvaluationObject(order_id);
        } else {
            toast("订单不存在或非待评价状态!");
            finish();
        }
        observe();
        mBinding.nineGridView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridView2.NineGridAdapter(R.drawable.shangchuanzhaopian,
                R.drawable.close_circle, mViewModel.items);
        mBinding.nineGridView.setAdapter(adapter);
        adapter.setListener(view -> {
            if (mViewModel.items.size() >= 9) {
                ToastUtils.showShort("最多九张图片！");
                return;
            }
            CertifiedHelp.showNineDialog(this, true, 9 - mViewModel.items.size());
        });
    }

    private void observe() {
        mViewModel.getEvaluationObjectLive.observe(this, getEvaluationObjectBean -> {
            dismissLoading();
            if (getEvaluationObjectBean != null) {
                mBinding.setBean(getEvaluationObjectBean.getData());
                GetEvaluationObjectBean data = getEvaluationObjectBean.getData();
                mBinding.setBean(data);
                mBinding.clDoctor.setVisibility(data.getDoctor_data() != null ? View.VISIBLE : View.GONE);
                if (data.getDoctor_data() != null) {
                    mBinding.setUrlDoctor(data.getDoctor_data().getImage());
                }
                mBinding.clCounselor.setVisibility(data.getCounselling_data() != null ? View.VISIBLE : View.GONE);
                if (data.getCounselling_data() != null) {
                    mBinding.setUrlCounselor(data.getCounselling_data().getImage());
                }
            }
        });
        mViewModel.orderEvaluationLive.observe(this, bean -> {
            dismissLoading();
            if (bean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("评论成功！");
                //刷新订单列表  -->0全部 4-待评价 5-已完成
                OrderStatusHelp.refreshOrderList(type, 0);
                OrderStatusHelp.refreshOrderList(type, 4);
                OrderStatusHelp.refreshOrderList(type, 5);
                finish();
            }
        });
    }

    private ClickEventHandler<GetEvaluationObjectBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_sure_evaluation:  //  评价
                //医生 审美 技术
                int doctor_major = mBinding.rbDoctor1.getCurCount();
                int doctor_skill = mBinding.rbDoctor2.getCurCount();
                //咨询师 审美 服务
                int consultant_major = mBinding.rbCounselor1.getCurCount();
                int consultant_service = mBinding.rbCounselor2.getCurCount();
                //用户评价的内容  content
                String content = mBinding.etNumber.getText().toString().trim();
                if (bean.getDoctor_data() != null) {
                    if (doctor_skill == 0 || doctor_major == 0) {
                        toast("请选择医生评分!");
                        break;
                    }
                }
                if (bean.getCounselling_data() != null) {
                    if (consultant_service == 0 || consultant_major == 0) {
                        toast("请选择咨询师评分!");
                        break;
                    }
                }
                showLoading(Sys.LOADING);
                mViewModel.orderEvaluation(order_id,
                        String.valueOf(doctor_skill), String.valueOf(doctor_major),
                        String.valueOf(consultant_service), String.valueOf(consultant_major),
                        content);
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
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                ArrayList<String> list = new ArrayList<>();
                for (ImageItem item : items) {
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
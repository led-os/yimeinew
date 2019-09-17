package com.chengzi.app.ui.homepage.doctor.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEventHandler;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PropAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentInfoBinding;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.activity.AnswerActivity;
import com.chengzi.app.ui.homepage.activity.FansOrAttentionActivity;
import com.chengzi.app.ui.homepage.activity.ItsPublishActivity;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;
import com.chengzi.app.ui.mine.activity.editinfo.EditInfoDoctorOrCounselorActivity;
import com.chengzi.app.utils.NumberShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页  用户信息
 *
 * @ClassName:InfoFragment
 * @PackageName:com.yimei.app.ui.homepage.doctor.fragment
 * @Create On 2019/4/18 0018   17:53
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class InfoFragment extends RefreshableFragment<FragmentInfoBinding, DoctorHomePageViewModel>
        implements OnRefreshListener, ClickEventHandler<DoctorHomeInfoBean> {

    private PropAdapter propAdapter;
    private boolean isOpen = false;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(FragmentManager fm) {
        InfoFragment fragment = (InfoFragment) fm.findFragmentByTag(InfoFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new InfoFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_info;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.isSelf())
            viewModel.doctorHomeInfo();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        binding.setListener(this);
        binding.setIsSelf(viewModel.isSelf());

        initRecyclerView();

        observe();

    }

    private void observe() {
        viewModel.doctorHomeLive.observe(this, doctorHomeInfoBean -> {

            binding.setInit(true);
            refreshComplete = true;
            finishRefresh();

            if (doctorHomeInfoBean.getType() == 2) {
                bindDoctorInfo(doctorHomeInfoBean);
            } else if (doctorHomeInfoBean.getType() == 3) {
                bindCounselorInfo(doctorHomeInfoBean);
            }
            binding.elAutograph.setSubject(doctorHomeInfoBean.getType() == 2 ? "医生介绍" : "咨询师介绍");
            binding.elAutograph.setContent(doctorHomeInfoBean.getSynopsis());

            if (!TextUtils.isEmpty(doctorHomeInfoBean.getCover())) {
                ImageLoader.loadImage(binding.btnBackground, doctorHomeInfoBean.getCover(), 0, R.drawable.yonghuxinxi_beijing);
                binding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
            } else {
                binding.btnBackground.setImageResource(R.drawable.yonghuxinxi_beijing);
            }
        });

        viewModel.attentionLive.observe(this, integer -> {
            ToastUtils.showShort(integer == 1 ? "关注成功" : "已取消关注");
            binding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);
            EventBus.getDefault().post(new AttentionEvent(viewModel.getDoctorId(), integer == 1));
        });

        viewModel.changeCoverLive.observe(this, s -> {
            dismissLoading();
            if (TextUtils.isEmpty(s)) {
                binding.btnBackground.setImageResource(R.drawable.yonghuxinxi_beijing);
            } else {
                ImageLoader.loadImage(binding.btnBackground, s, 0, R.drawable.yonghuxinxi_beijing);
                binding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
            }
        });
    }

    private void bindDoctorInfo(DoctorHomeInfoBean doctorHomeInfoBean) {

        binding.setBean(doctorHomeInfoBean);

//        if (viewModel.isSelf()) {
//            binding.tvProfessionScore.setText(getString(R.string.profession_score, doctorHomeInfoBean.getGrade()));
//        } else {
//            binding.tvProfessionScore.setText(getString(R.string.profession_score_home_page, doctorHomeInfoBean.getGrade()));
//        }
        //医生是 审美和技术
        binding.tvProfessionScore.setText(getString(R.string.beauty_score_home_page, doctorHomeInfoBean.getGrade()));

        binding.tvTechniqueScore.setText(getString(R.string.technique_score_home_page, doctorHomeInfoBean.getSkill_grade()));

        List<PropAdapter.Prop> list = new ArrayList<>();
        list.add(new PropAdapter.Prop("下单", NumberShowUtils.processNumber(doctorHomeInfoBean.getOrder_num())));

        if (viewModel.isSelf()) {
            list.add(new PropAdapter.Prop("总成交", doctorHomeInfoBean.getOrder_transaction_amounth() + "元"));
        }

        list.add(new PropAdapter.Prop("预约", NumberShowUtils.processNumber(doctorHomeInfoBean.getAppointment_num())));
        list.add(new PropAdapter.Prop("咨询", NumberShowUtils.processNumber(doctorHomeInfoBean.getSheet_num())));
        list.add(new PropAdapter.Prop("发表", NumberShowUtils.processNumber(doctorHomeInfoBean.getFind_num())));
        list.add(new PropAdapter.Prop("粉丝", NumberShowUtils.processNumber(doctorHomeInfoBean.getFans_num())));
        list.add(new PropAdapter.Prop("关注", NumberShowUtils.processNumber(doctorHomeInfoBean.getFollor_num())));
        list.add(new PropAdapter.Prop("回答", NumberShowUtils.processNumber(doctorHomeInfoBean.getAnswer_num())));

        propAdapter.setNewData(list);
    }

    private void bindCounselorInfo(DoctorHomeInfoBean doctorHomeInfoBean) {
        binding.setBean(doctorHomeInfoBean);

        //咨询师 审美 服务
//        binding.tvProfessionScore.setText(getString(R.string.profession_score_home_page, doctorHomeInfoBean.getGrade()));
        binding.tvProfessionScore.setText(getString(R.string.beauty_score_home_page, doctorHomeInfoBean.getGrade()));
        binding.tvTechniqueScore.setText(getString(R.string.service_score_home_page, doctorHomeInfoBean.getSkill_grade()));

        binding.tvQualificationNumber.setVisibility(View.GONE);

        List<PropAdapter.Prop> list = new ArrayList<>();
        list.add(new PropAdapter.Prop("下单", NumberShowUtils.processNumber(doctorHomeInfoBean.getOrder_num())));

        if (viewModel.isSelf()) {
            list.add(new PropAdapter.Prop("总成交", doctorHomeInfoBean.getOrder_transaction_amounth() + "元"));
        }

        list.add(new PropAdapter.Prop("咨询", NumberShowUtils.processNumber(doctorHomeInfoBean.getSheet_num())));
        list.add(new PropAdapter.Prop("发表", NumberShowUtils.processNumber(doctorHomeInfoBean.getFind_num())));
        list.add(new PropAdapter.Prop("粉丝", NumberShowUtils.processNumber(doctorHomeInfoBean.getFans_num())));
        list.add(new PropAdapter.Prop("关注", NumberShowUtils.processNumber(doctorHomeInfoBean.getFollor_num())));
        list.add(new PropAdapter.Prop("回答", NumberShowUtils.processNumber(doctorHomeInfoBean.getAnswer_num())));

        propAdapter.setNewData(list);

    }

    private void initRecyclerView() {
        propAdapter = new PropAdapter();
        binding.recyclerView.setAdapter(propAdapter);

        propAdapter.setOnItemClickListener((adapter, view, position) -> {
            PropAdapter.Prop item = propAdapter.getItem(position);
            switch (item.propName) {
                case "发表":
                    ItsPublishActivity.start(getActivity(), viewModel.getDoctorId());
                    break;
                case "粉丝":
                    FansOrAttentionActivity.start(getActivity(), Sys.EXTRA_FANS, viewModel.getDoctorId());
                    break;
                case "关注":
                    FansOrAttentionActivity.start(getActivity(), Sys.EXTRA_ATTENTION, viewModel.getDoctorId());
                    break;
                case "回答":
                    AnswerActivity.start(getContext(), viewModel.getDoctorId());
                    break;
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.doctorHomeInfo();
    }

    @Subscribe
    public void onAttention(AttentionEvent event) {

        if (TextUtils.equals(viewModel.getDoctorId(), event.getUserId())) {
            int integer = event.isAttention() ? 1 : 0;
            binding.getBean().setFollow(integer);
            binding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);
            viewModel.doctorHomeInfo();
        }
    }


    @Override
    public void handleClick(View view, DoctorHomeInfoBean bean) {
        switch (view.getId()) {
            case R.id.btn_attention:
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (binding.getBean() == null) {
                    return;
                }
                int follow = binding.getBean().getFollow();
                viewModel.findFollow(follow == 1);
                break;
            case R.id.btn_compile_profile:  //  编辑资料
                goActivity(EditInfoDoctorOrCounselorActivity.class);
                break;
            case R.id.el_autograph:         //介绍
                if (!isOpen) {   //展开
                    binding.tvAutograph.setVisibility(View.VISIBLE);
                    binding.elAutograph.setContent("");
                    binding.elAutograph.setIvArrow(R.drawable.close);
                } else {        //关闭
                    binding.tvAutograph.setVisibility(View.GONE);
                    binding.elAutograph.setContent(bean.getSynopsis());
                    binding.elAutograph.setIvArrow(R.mipmap.xiangqing);
                }
                isOpen = !isOpen;
                break;
            case R.id.btn_background:   //  更换背景图
                if (viewModel.isSelf()) {
                    new SelectDialog(getActivity())
                            .setTvText1("更换封面")
                            .setText1Listener(v1 -> {
                                ImagePicker imagePicker = ImagePicker.getInstance();
                                imagePicker.setMultiMode(false);
                                imagePicker.setCrop(false);
                                imagePicker.setShowCamera(true);
                                imagePicker.setSelectLimit(1);
                                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                startActivityForResult(intent, Sys.REQUEST_CODE_PIC, data -> {
                                    List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                                    String picPath = items.get(0).path;
                                    upload(picPath);
                                });

                            })
                            .setTvCameraVisibility(false)
                            .show();
                }
                break;
        }
    }

    private void upload(String images) {
        showLoading("封面更换中");
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String path) {
                //更换封面
                viewModel.changeCover(path);
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

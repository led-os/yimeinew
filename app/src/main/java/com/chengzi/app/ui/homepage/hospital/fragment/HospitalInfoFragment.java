package com.chengzi.app.ui.homepage.hospital.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.chengzi.app.databinding.FragmentHospitalInfoBinding;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.activity.AnswerActivity;
import com.chengzi.app.ui.homepage.activity.FansOrAttentionActivity;
import com.chengzi.app.ui.homepage.activity.ItsPublishActivity;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.ui.mine.activity.editinfo.EditInfoHospitalActivity;
import com.chengzi.app.utils.NumberShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院主页信息
 *
 * @ClassName:HospitalInfoFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   10:34
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class HospitalInfoFragment extends RefreshableFragment<FragmentHospitalInfoBinding,
        HospitalHomePageViewModel> {

    private PropAdapter propAdapter;
    private boolean isOpen = false;

    public HospitalInfoFragment() {
    }

    public static HospitalInfoFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(HospitalInfoFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new HospitalInfoFragment();
            fragment.setArguments(args);
        }
        return (HospitalInfoFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_info;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        HospitalHomePageViewModel parentViewModel = ViewModelProviders.of(getActivity()).get(HospitalHomePageViewModel.class);
        viewModel.setHospitalId(parentViewModel.getHospitalId());
        viewModel.setSelf(parentViewModel.isSelf());

        binding.setListener(clickListener);
        binding.setIsSelf(viewModel.isSelf());

        initRecyclerView();
        observe();

    }

    private void observe() {
        viewModel.hospitalDetailLive.observe(this, hospitalHomeInfoBean -> {
            refreshComplete = true;
            finishRefresh();
            bindHospitalInfo(hospitalHomeInfoBean);
            binding.setInit(true);
            if (!TextUtils.isEmpty(hospitalHomeInfoBean.getInfo().getCover())) {
                ImageLoader.loadImage(binding.ivBackGround, hospitalHomeInfoBean.getInfo().getCover(), 0, R.drawable.yonghuxinxi_beijing);
                binding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
            } else {
                binding.ivBackGround.setImageResource(R.drawable.yonghuxinxi_beijing);
            }
            binding.elAutograph.setContent(hospitalHomeInfoBean.getInfo().getSynopsis());
        });

        viewModel.attentionLive.observe(this, integer -> {
            ToastUtils.showShort(integer == 1 ? "关注成功" : "已取消关注");
            binding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);
            EventBus.getDefault().post(new AttentionEvent(viewModel.getHospitalId(), integer == 1));
        });
        viewModel.changeCoverLive.observe(this, s -> {
            dismissLoading();
            if (s != null && s.isSuccess()) {
                if (TextUtils.isEmpty(viewModel.getHead_path())) {
                    binding.ivBackGround.setImageResource(R.drawable.yonghuxinxi_beijing);
                } else {
                    ImageLoader.loadImage(binding.ivBackGround, viewModel.getHead_path(), 0, R.drawable.yonghuxinxi_beijing);
                    binding.layoutHeader.setBackgroundColor(Color.parseColor("#50000000"));
                }
            }
        });
    }

    private void bindHospitalInfo(HospitalHomeInfoBean bean) {

        binding.setBean(bean);
        if (bean == null) {
            return;
        }

        List<HospitalHomeInfoBean.BegoodatBean> begoodat = bean.getBegoodat();
        if (begoodat != null) {
            StringBuilder sb = new StringBuilder();
            for (HospitalHomeInfoBean.BegoodatBean begoodatBean : begoodat) {
                if (begoodatBean == null) {
                    continue;
                }
                sb.append(begoodatBean.getName())
                        .append(",");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            binding.tvBetter.setText(sb);
        }

        List<PropAdapter.Prop> list = new ArrayList<>();
        list.add(new PropAdapter.Prop("下单", NumberShowUtils.processNumber(bean.getTotalOrderCount())));

        if (viewModel.isSelf()) {
            list.add(new PropAdapter.Prop("总成交", bean.getTotalOrderPrice() + "元"));
        }

        list.add(new PropAdapter.Prop("咨询", NumberShowUtils.processNumber(bean.getSheet_num())));
        list.add(new PropAdapter.Prop("发表", NumberShowUtils.processNumber(bean.getTotalPublishCount())));
        list.add(new PropAdapter.Prop("粉丝", NumberShowUtils.processNumber(bean.getFansCount())));
        list.add(new PropAdapter.Prop("关注", NumberShowUtils.processNumber(bean.getFollowCount())));
        list.add(new PropAdapter.Prop("回答", NumberShowUtils.processNumber(bean.getReplyCount())));
        list.add(new PropAdapter.Prop(" ", " "));

        propAdapter.setNewData(list);
    }

    private void initRecyclerView() {
        propAdapter = new PropAdapter();
        binding.recyclerView.setAdapter(propAdapter);

        propAdapter.setOnItemClickListener((adapter, view, position) -> {
            PropAdapter.Prop item = propAdapter.getItem(position);
            switch (item.propName) {
                case "发表":
                    ItsPublishActivity.start(getActivity(), viewModel.getHospitalId());
                    break;
                case "粉丝":
                    FansOrAttentionActivity.start(getActivity(), Sys.EXTRA_FANS, viewModel.getHospitalId());
                    break;
                case "关注":
                    FansOrAttentionActivity.start(getActivity(), Sys.EXTRA_ATTENTION, viewModel.getHospitalId());
                    break;
                case "回答":
                    /*// TODO: 2019/4/18 0018
                    //  医生已经回答过问题了，还能再回答吗？
                    startActivity(new Intent(getActivity(), AnswerActivity.class));*/
                    AnswerActivity.start(getContext(), viewModel.getHospitalId());
                    break;
            }
        });
    }

    private boolean isRefresh = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            onRefresh();
        }
        isRefresh = true;
    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.getHospitalDetail();
    }

    @Subscribe
    public void onAttention(AttentionEvent event) {

        if (TextUtils.equals(viewModel.getHospitalId(), event.getUserId())) {
            int integer = event.isAttention() ? 1 : 0;
            binding.getBean().setIs_follow(integer);
            binding.btnAttention.setText(integer == 1 ? R.string.cancel_attention : R.string.attention);

            viewModel.getHospitalDetail();
        }
    }


    @Override
    public boolean isRefreshFinished() {
        return refreshComplete;
    }

    private ClickEventHandler<HospitalHomeInfoBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.btn_attention:  //  关注
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (binding.getBean() == null) {
                    return;
                }
                int follow = binding.getBean().getIs_follow();
                viewModel.findFollow(follow == 1);
                break;
            case R.id.btn_compile_profile:  //  编辑信息
                goActivity(EditInfoHospitalActivity.class);
                break;
            case R.id.el_autograph:         //介绍
                if (!isOpen) {   //展开
                    binding.tvAutograph.setVisibility(View.VISIBLE);
                    binding.elAutograph.setContent("");
                    binding.elAutograph.setIvArrow(R.drawable.close);
                } else {        //关闭
                    binding.tvAutograph.setVisibility(View.GONE);
                    binding.elAutograph.setContent(bean.getInfo().getSynopsis());
                    binding.elAutograph.setIvArrow(R.mipmap.xiangqing);
                }
                isOpen = !isOpen;
                break;
            case R.id.iv_back_ground:   //  更换背景图
                if (viewModel.isSelf()) {
                    new SelectDialog(getActivity())
                            .setTvText1("更换封面")
                            .setText1Listener(v1 -> {
                                isRefresh = false;
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
    };

    private void upload(String images) {
        showLoading("封面更换中");
        AliUpload.upload(System.currentTimeMillis() + AccountHelper.getUserId(), images, new AliUpload.CallBack() {
            @Override
            public void onSuccess(String path) {
                //更换封面
                viewModel.setHead_path(path);
                viewModel.changeCover();
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

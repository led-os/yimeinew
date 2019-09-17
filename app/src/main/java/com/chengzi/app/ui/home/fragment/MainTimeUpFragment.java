package com.chengzi.app.ui.home.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentMainTimeUpBinding;
import com.chengzi.app.ui.home.viewmodel.RecommandViewModel;
import com.chengzi.app.ui.seckill.activity.SeckillActivity;
import com.chengzi.app.ui.seckill.bean.KillGoodsListBean;
import com.chengzi.app.ui.seckill.viewmodel.SeckillViewModel;

import java.util.List;

/**
 * 首页限时秒杀
 *
 * @ClassName:MainTimeUpFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   18:54
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class MainTimeUpFragment extends BaseFragment<FragmentMainTimeUpBinding, SeckillViewModel> {

    public static MainTimeUpFragment newInstance() {
        Bundle args = new Bundle();
        MainTimeUpFragment fragment = new MainTimeUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isOnResume = true;
    private boolean isHidden = true;

    public MainTimeUpFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main_time_up;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SeckillActivity.class));
        });

        observe();
        viewModel.killGoodsList();

    }

    private void observe() {
        RecommandViewModel homeViewModel = ViewModelProviders.of(getActivity()).get(RecommandViewModel.class);
        homeViewModel.refreshLive.observe(this, aBoolean -> {
            viewModel.killGoodsList();
        });

        viewModel.killGoodsListLiveData.observe(this, killGoodsListBean -> {
            dismissLoading();

            KillGoodsListBean data = killGoodsListBean;

            if (data == null) {
                return;
            }

            //?点场 + 倒计时
            binding.setTimes(data.getTimes() + "点场");
            long end_time = Long.parseLong(data.getEnd_time());
            setRemainTime(end_time);


            if (data == null || data.getData() == null || data.getData().isEmpty()) {
                binding.llItem.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {

                binding.llItem.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);

                //商品数据
                List<KillGoodsListBean.DataEntity> bean = data.getData();

                binding.setOneGoods(false);
                binding.setTwoGoods(false);
                int size = bean.size();
                switch (size) {
                    case 2:
                        binding.setTwoGoods(true);
                        binding.setBeanTwo(bean.get(1));
                        binding.setUrlTwo(bean.get(1).getGoods_image());
                        binding.tvTwoYuan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    case 1:
                        binding.setOneGoods(true);
                        binding.setBeanOne(bean.get(0));
                        binding.setUrlOne(bean.get(0).getGoods_image());
                        binding.tvOneYuan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        break;
                }

                //如果倒计时时间为0 则重新加载数据
                if (!TextUtils.equals(data.getTimes(), "0") && end_time == 0 && binding.tvEndTime.getText().equals("00:00:00") && isOnResume && isHidden) {
//                    toast("回调" + isOnResume);
                    viewModel.killGoodsList();
                }
            }

        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isOnResume) {
            isHidden = true;
            if (binding.tvEndTime.getText().equals("00:00:00")) {
                viewModel.killGoodsList();
            }
        } else {
            isHidden = false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.isHidden = hidden;
        if (isHidden) {
            isHidden = false;
        } else {
            if (binding.tvEndTime.getText().equals("00:00:00")) {
                viewModel.killGoodsList();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isOnResume = true;
        if (binding.tvEndTime.getText().equals("00:00:00")) {
        }
        viewModel.killGoodsList();
    }

    @Override
    public void onPause() {
        super.onPause();
        isOnResume = false;
    }

    private CountDownTimer timer;

    //
    public void setRemainTime(long remainTime) {
        setStopTimer();
        if (remainTime <= 0) {
            remainTime = 0;
            caculate(remainTime * 1000);
            return;
        }
        timer = new CountDownTimer(remainTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                caculate(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                setStopTimer();
                ///如果倒计时为0了、则表示这个时段的秒杀已结束，刷新数据，开始下一时段的倒计时
                viewModel.killGoodsList();
            }
        };
        timer.start();
    }

    private void caculate(long remainTimeInMillins) {
        int hour = (int) (remainTimeInMillins / (60 * 60 * 1000));
        remainTimeInMillins -= hour * (60 * 60 * 1000);
        int minute = (int) (remainTimeInMillins / (60 * 1000));
        remainTimeInMillins -= minute * (60 * 1000);
        int second = (int) (remainTimeInMillins / 1000);

        binding.tvEndTime.setText(String.format("%1$02d:%2$02d:%3$02d", hour, minute, second));
    }

    public void setStopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setStopTimer();
    }
}

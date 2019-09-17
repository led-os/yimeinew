package com.chengzi.app.ui.mine.activity.docotorroborder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityRobOrderSucBindingImpl;
import com.chengzi.app.event.IncomingCallEvent;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.SheetListBean;
import com.chengzi.app.ui.mine.viewmodel.RobOrderViewModel;
import com.chengzi.app.utils.NimUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 抢单成功(等待客户选择, 已选中, 未被选中,选择超时)
 *
 * @ClassName:RobOrderSucActivity
 * @PackageName:com.yimei.app.ui.mine.activity.docotorroborder
 * @Create On 2019/4/9 0009   10:55
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */

public class RobOrderSucActivity extends BaseActivity<ActivityRobOrderSucBindingImpl, RobOrderViewModel> {


    //sheet_type咨询类型 （必传）（1-私享咨询 2-在线面诊）
    //倒计时 60s
    private long ltime = 60;
    //1开始聊天(已被客户选中)/2返回(未选中 和 60秒倒计时结束)按钮
    private int btn_type = 0;
    private boolean timeout = false;    //  true 倒计时结束

    public static TimerHandler timerHandler;
    public Timer timer = new Timer();     //设置定时器Timer

    public class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (ltime > 0) {
                //② 每秒请求一次接口-->查询用户是/否选择我 停止倒计时(选中/未选中) 显示聊天/返回
                ltime = ltime - 1;  //-1秒
                mBinding.tvCountDown.setText(String.valueOf(ltime));
 /*               toast("请求接口");
                showLoading(Sys.LOADING);*/
                mViewModel.sheetStatus(mViewModel.getSheetId());
            } else {
                //③ 如果60中 ②接口中没有数据——>自动取消,倒计时结束(客户选择超时)  显示返回
                mBinding.tvCountDown.setVisibility(View.INVISIBLE);
                mBinding.tvTypeText.setText("客户选择超时，您可以抢新的订单");
                mBinding.tvGoOrFinish.setText("返回");
                mBinding.tvGoOrFinish.setVisibility(View.VISIBLE);
                btn_type = 2;
                timeout = true;
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (timerHandler != null) {
                    timerHandler = null;
                }
            }
        }
    }

    private class MyTimerTask extends TimerTask {
        public void run() {
            if (timerHandler != null)
                timerHandler.sendEmptyMessage(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerHandler != null) {
            timerHandler = null;
        }
        EventBus.getDefault().unregister(this);
    }

    public static void start(Context context, String sheet_id, int sheet_type, SheetListBean sheetListBean,String sheetJoinId) {
        context.startActivity(new Intent(context, RobOrderSucActivity.class)
                .putExtra("sheet_id", sheet_id)
                .putExtra("sheet_type", sheet_type)
                .putExtra("sheetListBean", sheetListBean)
                .putExtra("sheetJoinId",sheetJoinId)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_rob_order_suc;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        String sheet_id = getIntent().getStringExtra("sheet_id");
        int sheet_type = getIntent().getIntExtra("sheet_type", 1);

        mViewModel.setSheetId(sheet_id);
        mViewModel.setSheetType(sheet_type);

        SheetListBean sheetListBean = (SheetListBean) getIntent().getSerializableExtra("sheetListBean");
        mViewModel.setSheetListBean(sheetListBean);

        String sheetJoinId = getIntent().getStringExtra("sheetJoinId");
        mViewModel.setSheetJoinId(sheetJoinId);

        mBinding.setListener(clickEventHandler);

        if (TextUtils.isEmpty(sheet_id) && sheetListBean == null) {
            toast("数据有误!");
            finish();
        } else {
            mBinding.setBean(sheetListBean);
            mBinding.setUrl(sheetListBean.getImage());
            //tv_sex设置性别
            String gender = sheetListBean.getGender();
            mBinding.tvSex.setBackgroundResource(gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            mBinding.tvSex.setCompoundDrawables(drawable_n, null, null, null);
        }
        //① 进入该页面开始倒计时-->60S   (等待客户选择)
        if (ltime > 0) {
            timerHandler = new TimerHandler();  //实例化TimerHandler
            if (timer == null)
                timer = new Timer();
            timer.schedule(new MyTimerTask(), 0, 1000);  //0表示无延迟，1000表示隔1000ms
        }

        mViewModel.sheetStatusLiveData.observe(this, data -> {

            if (data == null) {
                return;
            }
            // 是否选择咨询对象 (1-是 2-否)
            String is_chose = data.getIs_chose();
            //被选择id
            String answer_id = data.getAnswer_id();
            if (TextUtils.equals(is_chose, "1")) {
                mBinding.tvCountDown.setVisibility(View.INVISIBLE);
                mBinding.tvGoOrFinish.setVisibility(View.VISIBLE);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (timerHandler != null) {
                    timerHandler = null;
                }
                if (TextUtils.equals(answer_id, AccountHelper.getUserId())) {//选中
                    btn_type = 1;
                    mBinding.tvTypeText.setText("您已被客户选择为咨询对象");
                    mBinding.tvGoOrFinish.setText("开始聊天");
                    if (mViewModel.getSheetType() == 2) {
                        mBinding.tvGoOrFinish.setVisibility(View.GONE);
                    }
                } else {
                    //未选中
                    btn_type = 2;
                    mBinding.tvTypeText.setText("很遗憾，您未被选中");
                    mBinding.tvGoOrFinish.setText("返回");
                }
            }
            //已取消 -->普通用户已取消
            if (TextUtils.equals(data.getIs_cancel(), "1")) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (timerHandler != null) {
                    timerHandler = null;
                }
                toast("客户已取消!");
                finish();
            }
        });
    }

    private ClickEventHandler<SheetListBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //查看发布人的信息
                UserHomePageActivity.start(this, String.valueOf(bean.getUser_id()));
                break;
            case R.id.tv_go_or_finish:  //开始聊天/返回
                //1开始聊天(已被客户选中)/2返回(未选中 和 60秒倒计时结束)按钮
                if (btn_type == 1) {
                    if (mViewModel.getSheetType() == 1) {
                        NimUtils.startP2PSession(this, mViewModel.getSheetListBean().getYunxin_accid());
                    }
                    finish();
                } else if (btn_type == 2) {
                    finish();
                }
                break;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!timeout) {
            mViewModel.delSheetChoseList();
        }
    }

    @Subscribe
    public void onSelected(IncomingCallEvent event){
        finish();
    }
}
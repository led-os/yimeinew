package com.chengzi.app.ui.mine.activity.electronicinvoice;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.WindowManager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityInvoicesSubmitSuccessBindingImpl;

/**
 * 发票提交成功
 *
 * @ClassName:InvoicesSubmitSuccessActivity
 * @PackageName:com.yimei.app.ui.mine.activity.electronicinvoice
 * @Create On 2019/4/8 0008   17:21
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class InvoicesSubmitSuccessActivity extends BaseActivity<ActivityInvoicesSubmitSuccessBindingImpl, BaseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_invoices_submit_success;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding.setListener(cliclkListener);
    }

    private ClickEventHandler cliclkListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_finish:      //返回
                finish();
                break;
            case R.id.tv_history_invoice:    //开票历史
                goActivity(HistoryInvoiceActivity.class);
                finish();
                break;
        }
    };
}
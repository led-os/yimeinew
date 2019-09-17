package com.chengzi.app.ui.mine.activity.electronicinvoice;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityIssueElectronicInvoicesBindingImpl;
import com.chengzi.app.ui.mine.bean.ElectronicInvoiceBean;
import com.chengzi.app.ui.mine.bean.ShopCarBean;
import com.chengzi.app.ui.mine.viewmodel.ElectronicInvoiceModel;
import com.chengzi.app.utils.CommonUtilis;

import java.util.ArrayList;
import java.util.List;

/**
 * 开具电子发票
 *
 * @ClassName:IssueElectronicInvoicesActivity
 * @PackageName:com.yimei.app.ui.mine.activity.electronicinvoice
 * @Create On 2019/4/8 0008   16:18
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class IssueElectronicInvoicesActivity extends BaseActivity<ActivityIssueElectronicInvoicesBindingImpl, ElectronicInvoiceModel> {

    private static final String TAG = IssueElectronicInvoicesActivity.class.getSimpleName();
    private List<ElectronicInvoiceBean> list = new ArrayList<>();
    private Double sumMoney = 0.0;
    ArrayList<String> payment_ids = new ArrayList<>();// 列表的发票id 列表转换成String给后台
    private String riseType = "1"; // 发票类型 id 1-企业单位 2-个人 （必须）

    @Override
    public int getLayoutRes() {
        return R.layout.activity_issue_electronic_invoices;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        mBinding.setModel(mViewModel);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        list = (List<ElectronicInvoiceBean>) bundle.getSerializable("data");

        int identity = AccountHelper.getIdentity();
        if (identity == Sys.TYPE_HOSPITAL) {
            //发票类型： 企业单位
            mBinding.elTypeCompany.setVisibility(View.VISIBLE);
            mBinding.rgType.setVisibility(View.GONE);
            //隐藏税号
//            mBinding.llDutyParagraph.setVisibility(View.VISIBLE);
        } else {
            //显示选择发票类型
            mBinding.rgType.setVisibility(View.VISIBLE);
            mBinding.elTypeCompany.setVisibility(View.GONE);
            //显示税号
//            mBinding.llDutyParagraph.setVisibility(View.VISIBLE);
        }
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Double money = Double.parseDouble(CommonUtilis.getTwoDecimal(list.get(i).getMoney()));
                sumMoney += money;
                payment_ids.add(list.get(i).getId());
            }
        }
        mBinding.etInvoiceAmount.setText("¥" + sumMoney);

        //开发票
        mViewModel.invoiceAddLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                finish();
                goActivity(InvoicesSubmitSuccessActivity.class);
            }
        });
    }

    private ClickEventHandler<ShopCarBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.rb_company:      //企业单位
//                toast("企业单位");
                riseType = "1";
                break;
            case R.id.rb_personage:    //个人
//                toast("个人");
                riseType = "2";
                break;
            case R.id.tv_submit:    //提交-->提交成功
                if (TextUtils.isEmpty(riseType)) {
                    toast("请选择发票类型!");
                    return;
                }
                //    //发票抬头 税号 发票内容 电子邮箱
                if (TextUtils.isEmpty(mViewModel.rise.get())) {
                    toast("请填写发票抬头!");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.identify_number.get())) {
                    toast("请填写纳税人识别号!");
                    return;
                }
                String invoice_content = mBinding.elInvoiceContent.getContent().toString().trim();
                if (TextUtils.isEmpty(invoice_content)) {
                    toast("请填写发票内容!");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.email.get())) {
                    toast("请天下接收发票的电子邮箱!");
                    return;
                }
                if (payment_ids != null && payment_ids.size() > 0) {
                    showLoading(Sys.LOADING);
                    mViewModel.invoiceAdd(payment_ids, riseType, invoice_content);
                } else {
                    toast("不可开票!");
                }
                break;
        }
    };
}
package com.chengzi.app.ui.mine.activity.electronicinvoice;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityElectronicInvoiceBindingImpl;
import com.chengzi.app.databinding.ItemElectronicInvoiceLayoutBinding;
import com.chengzi.app.ui.mine.bean.ElectronicInvoiceBean;
import com.chengzi.app.ui.mine.viewmodel.ElectronicInvoiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 电子发票
 *
 * @ClassName:ElectronicInvoiceActivity
 * @PackageName:com.yimei.app.ui.mine.activity.electronicinvoice
 * @Create On 2019/4/8 0008   15:18
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class ElectronicInvoiceActivity extends BaseActivity<ActivityElectronicInvoiceBindingImpl, ElectronicInvoiceModel> {
    private boolean chooseAll = false;
    private PagingLoadHelper helper;
    private ArrayList<ElectronicInvoiceBean> list = new ArrayList<>();
    List<ElectronicInvoiceBean> data;
    ElectronicInvoiceAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_electronic_invoice;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(clickListener);
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ElectronicInvoiceAdapter(adapterClickListener);
        mBinding.mRecyclerView.setAdapter(adapter);
        helper = new PagingLoadHelper(mBinding.mRecyclerView, mViewModel);


        mViewModel.electronicLive.observe(this, new Observer<List<ElectronicInvoiceBean>>() {
            @Override
            public void onChanged(@Nullable List<ElectronicInvoiceBean> electronicInvoiceBeans) {
                //每次刷新 全选则为不选中状态
                chooseAll = false;
                mBinding.ivChooseAll.setSelected(chooseAll ? true : false);
                if (electronicInvoiceBeans != null && electronicInvoiceBeans.size() > 0) {
                    data = electronicInvoiceBeans;
                    if (data.size() > 0) {
                        helper.onComplete(data);
                    } else {
                        helper.onComplete(new ArrayList<>());
                    }
                } else {
                    helper.onComplete(new ArrayList<>());
                }
            }
        });

        // 开票历史
        mBinding.topBar.setOnRightClickListener(v -> {
            goActivity(HistoryInvoiceActivity.class);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.start();
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_choose_all:  //  全选
            case R.id.tv_choose_all:
                //全选-->店铺和店铺中的商品全选
                if (data != null && data.size() > 0) {
                    if (!chooseAll) {
                        chooseAll = true;
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setIsChoose(true);
                        }
                    } else {
                        //取消全选-->店铺和店铺中的商品全选
                        chooseAll = false;
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setIsChoose(false);
                        }
                    }
                    mBinding.ivChooseAll.setSelected(chooseAll);
                } else {
                    chooseAll = false;
                    mBinding.ivChooseAll.setSelected(chooseAll);
                }
                adapter.notifyDataSetChanged();
                //选中或者取消全部选中计算
                //重新计算优惠券和总计
                if (chooseAll)
                    list.addAll(data);
                else {
                    list.clear();
                }
                break;
            case R.id.tv_submit:  // 下一步
                // 需要传递给下一个界面 开票金额 以及各个id的数据
                /*逻辑 首先判断是否全部选中  如果全部选中把所有的data都传递给开发票的页面   如果只是多选 在选中的地方建立一个数据 将选择的数据加入list
                 * 然后将选中的data  重新打造成一个list数据 传递给开发票的界面*/
                if (chooseAll) {
                    if (data != null && data.size() > 0) {
                        list.clear();
                        list.addAll(data);
                        Intent intent = new Intent();
                        intent.setClass(this, IssueElectronicInvoicesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    if (list.size() > 0) {
                        Intent intent = new Intent();
                        intent.setClass(this, IssueElectronicInvoicesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", list);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        toast("请选择开票内容");
                    }
                }
                break;
        }
    };
    private ClickEventHandler<ElectronicInvoiceBean> adapterClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_choose:  // 选中
                boolean isHaveChoose = false;   //商品中是否有 选中的商品
                bean.setIsChoose(bean.getIsChoose() ? false : true);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < data.size(); i++) {
                    if (!data.get(i).getIsChoose()) {
                        chooseAll = false;
                        data.get(i).setIsChoose(false);
                        break;
                    } else {
                        data.get(i).setIsChoose(true);
                        chooseAll = true;           //全选
                    }
                }
                mBinding.ivChooseAll.setSelected(chooseAll);
                //选中或者取消选中购物车中的商品
                //重新计算优惠券和总计
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getIsChoose()) {
                        isHaveChoose = true;
                        break;
                    }
                }
                if (isHaveChoose)
                    if (list.contains(bean)) {
                        list.remove(bean);
                    } else {
                        list.add(bean);
                    }
                else {
                    list = new ArrayList<>();
                    list.add(bean);
                }
                break;
        }
    };

    //店铺中的商品adapter
    private class ElectronicInvoiceAdapter extends BaseQuickAdapter<ElectronicInvoiceBean, BaseViewHolder> {

        private ClickEventHandler<ElectronicInvoiceBean> clickEventHandler;

        public ElectronicInvoiceAdapter(ClickEventHandler<ElectronicInvoiceBean> clickEventHandler) {
            super(R.layout.item_electronic_invoice_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, ElectronicInvoiceBean item) {
            ItemElectronicInvoiceLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.executePendingBindings();
            binding.ivChoose.setSelected(item.getIsChoose() ? true : false);

            AppCompatCheckedTextView view = helper.getView(R.id.iv_choose);
            view.setChecked(item.getIsChoose());
        }
    }
}
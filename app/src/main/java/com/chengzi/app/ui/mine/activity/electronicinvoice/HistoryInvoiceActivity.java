package com.chengzi.app.ui.mine.activity.electronicinvoice;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHistoryInvoiceBindingImpl;
import com.chengzi.app.databinding.ItemHistoryInvoiceLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.mine.bean.HistoryInvoiceBean;
import com.chengzi.app.ui.mine.viewmodel.HistoryInvoiceViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * 开票历史
 *
 * @ClassName:HistoryInvoiceActivity
 * @PackageName:com.yimei.app.ui.mine.activity.electronicinvoice
 * @Create On 2019/4/8 0008   17:34
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class HistoryInvoiceActivity extends BaseActivity<ActivityHistoryInvoiceBindingImpl, HistoryInvoiceViewModel> {

    private PagingLoadHelper helper;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_history_invoice;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        HistoryInvoiceAdapter adapter = new HistoryInvoiceAdapter(cliclkListener);
        mBinding.swipeRefreshView.setAdapter(adapter);
        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        helper.start();

        /*开票历史*/
        mViewModel.historyInvoiceLive.observe(this, new Observer<List<HistoryInvoiceBean>>() {
            @Override
            public void onChanged(@Nullable List<HistoryInvoiceBean> data) {
                if (data != null && data.size() > 0) {
                    helper.onComplete(data);
                } else {
                    helper.onComplete(new ArrayList<>());
                }
            }
        });
    }

    private ClickEventHandler<HistoryInvoiceBean> cliclkListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_look:      //查看发票
                ArrayList<String> strings = new ArrayList<>();
//                strings.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3764360916,1049130352&fm=26&gp=0.jpg");
                strings.add(bean.getImage());
                startActivity(new Intent(this, PictureSwitcherActivity.class)
                        .putExtra(PICTURE_URLS, strings)
                        .putExtra(PICTURE_INDEX, 1)
                        .putExtra("title", "开票历史")
                );
                break;
        }
    };

    public class HistoryInvoiceAdapter extends BaseQuickAdapter<HistoryInvoiceBean, BaseViewHolder> {
        private ClickEventHandler<HistoryInvoiceBean> clickEventHandler;

        public HistoryInvoiceAdapter(ClickEventHandler<HistoryInvoiceBean> clickEventHandler) {
            super(R.layout.item_history_invoice_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, HistoryInvoiceBean item) {
            ItemHistoryInvoiceLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.executePendingBindings();
        }
    }
}
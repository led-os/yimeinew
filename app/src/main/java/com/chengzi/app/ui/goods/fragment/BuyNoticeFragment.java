package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseFragment;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentBuyNoticeBinding;

/**
 * 下单须知
 * @ClassName:BuyNoticeFragment

 * @PackageName:com.yimei.app.ui.goods.fragment

 * @Create On 2019/4/16 0016   15:14

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class BuyNoticeFragment extends BaseFragment<FragmentBuyNoticeBinding, BaseViewModel> {

    public BuyNoticeFragment() {
    }

    public static BuyNoticeFragment newInstance() {
        android.os.Bundle args = new Bundle();
        BuyNoticeFragment fragment = new BuyNoticeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_buy_notice;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }
}

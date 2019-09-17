package com.chengzi.app.ui.homepage.hospital.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentOtherGoodsBinding;
import com.chengzi.app.ui.homepage.RefreshableFragment;

/**
 * 医院 --其他商品
 *
 * @ClassName:OtherGoodsFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   11:45
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class OtherGoodsFragment extends RefreshableFragment<FragmentOtherGoodsBinding, BaseViewModel> {

    //    public OtherGoodsFragment() {
//        // Required empty public constructor
//    }
//
//    public static OtherGoodsFragment newInstance(FragmentManager fm) {
//        OtherGoodsFragment fragment = (OtherGoodsFragment) fm.findFragmentByTag(OtherGoodsFragment.class.getSimpleName());
//        if (fragment == null) {
//            android.os.Bundle args = new Bundle();
//            fragment = new OtherGoodsFragment();
//            fragment.setArguments(args);
//        }
//        return fragment;
//    }
//
//    @Override
    public int getLayoutRes() {
        return R.layout.fragment_other_goods;
    }

    //
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
//
//        String[] types = {"眼部整形", "鼻部整形", "胸部整形", "面部轮廓", "脂肪整形", "眉部整形", "毛发美容", "口唇整形", "牙齿整形"};
//        for (String type : types) {
//            CustomTabLayout.Tab tab = binding.tabLayout.newTab().setText(type);
//            binding.tabLayout.addTab(tab);
//        }
//
//
//        GoodsGridAdapter adapter = new GoodsGridAdapter();
//        binding.recyclerOtherGoods.setAdapter(adapter);
//
//        adapter.setOnItemClickListener((adapter1, view, position) -> {
//
//            GoodsDetailActivity.start(getActivity(),adapter.getItem(position).getId());
//
//        });
//
//        Random random = new Random();
//
//        binding.tabLayout.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener(){
//            @Override
//            public void onTabSelected(CustomTabLayout.Tab tab) {
//                int i = random.nextInt(20);
//                i = i <= 0?5:i;
//                List<Object> list = new ArrayList<>();
//                for (int i1 = 0; i1 < i; i1++) {
//                    list.add(i1);
//                }
//
//            }
//        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isRefreshFinished() {
        return false;
    }
}

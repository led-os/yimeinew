package com.chengzi.app.ui.find.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindBaseAdapter;
import com.chengzi.app.adapter.MainDoctorAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentFindDoctorBinding;
import com.chengzi.app.ui.find.viewmodel.FindDoctorViewModel;
import com.chengzi.app.ui.home.bean.DoctorBean;

import java.util.List;

/**
 * 找医生
 *
 * @ClassName:FindDoctorFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/4/10 0010   16:26
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class FindDoctorFragment extends BaseFragment<FragmentFindDoctorBinding, FindDoctorViewModel> {

    public FindDoctorFragment() {
    }

    /**
     * @param type 0 医生 ，1 咨询师
     * @return
     */
    public static FindDoctorFragment newInstance(int type) {
        android.os.Bundle args = new Bundle();
        args.putInt(Sys.EXTRA, type);
        FindDoctorFragment fragment = new FindDoctorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find_doctor;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        int type = getArguments().getInt(Sys.EXTRA, 0);
        viewModel.setType(type);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);

        FindBaseAdapter<DoctorBean> baseAdapter = new FindBaseAdapter();
        baseAdapter.setFactory(new FindBaseAdapter.ItemAdapterFactory<DoctorBean>() {
            @Override
            public BaseQuickAdapter createAdapter(List<DoctorBean> list) {
                MainDoctorAdapter adapter = new MainDoctorAdapter(viewModel.getType() == 0 ? 2 : 3);
                adapter.setNewData(list);
                return adapter;
            }

            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });

        binding.swipeRefreshView.setAdapter(baseAdapter);
        binding.swipeRefreshView.setPullupEnable(false);


        PagingLoadHelper helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        helper.start();
        viewModel.doctorLive.observe(this, list -> {
            helper.onComplete(list);
        });
    }
}

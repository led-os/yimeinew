package com.chengzi.app.ui.homepage.hospital.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CaseListAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalCaseListBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.popu.DropDownGridPopup;
import com.chengzi.app.popu.DropDownPopup;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.homepage.bean.CasesCategoryByHospitalBean;
import com.chengzi.app.ui.homepage.viewmodel.HospitalCaseListViewModel;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 医院案例列表
 *
 * @ClassName:HospitalCaseListActivity
 * @PackageName:com.yimei.app.ui.homepage.hospital.activity
 * @Create On 2019/4/19 0019   14:40
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class HospitalCaseListActivity extends BaseActivity<ActivityHospitalCaseListBinding,
        HospitalCaseListViewModel> implements View.OnClickListener {

    private List<DropDownPopup.PopupBean> data = new ArrayList<>();
    private DropDownGridPopup gridPopup;
    private PagingLoadHelper helper;
    private CaseListAdapter adapter;

    public static void start(Context context, String hospital_id) {
        context.startActivity(new Intent(context, HospitalCaseListActivity.class)
                .putExtra(Sys.EXTRA, hospital_id)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_case_list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(this);
        EventBus.getDefault().register(this);

        mViewModel.setLifecycleOwner(this);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        adapter = new CaseListAdapter();
        adapter.setBaseViewModel(mViewModel);
        mBinding.swipeRefreshView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_10));
        mBinding.swipeRefreshView.addItemDecoration(dividerItemDecoration);

        //被查看的医院id 和 默认分类id为-1
        mViewModel.hospital_id.set(getIntent().getStringExtra(Sys.EXTRA));
        mViewModel.cate_id.set("-1");

        helper.start();
        //获取全部分类(默认选中 全部)
        data = new ArrayList<>();
        data.add(new DropDownPopup.Item(-1, "全部案例", "", ""));
        showLoading(Sys.LOADING);
        mViewModel.casesCategoryByHospital();

        observe();

    }

    private void observe(){

        mViewModel.casesLiveData.observe(this, list -> {
            if (list != null && list.size() > 0) {
                helper.onComplete(list);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });

        mViewModel.casesCategoryByHospitalLiveData.observe(this, list -> {
            dismissLoading();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    CasesCategoryByHospitalBean casesBean = list.get(i);
                    data.add(new DropDownPopup.Item(Integer.valueOf(casesBean.getId()), casesBean.getName(), "", ""));
                }
            }
        });

        mViewModel.caseDetailLive.observe(this,caseNoteListBeans -> {
            if (caseNoteListBeans == null || caseNoteListBeans.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteListBeans.get(0);
            for (CaseBean caseBean : adapter.getData()) {
                if (TextUtils.equals(bean.getCases_id(),caseBean.getId())) {
                    caseBean.setComment_number(bean.getComment_number()+"");
                    mViewModel.commentObservable.update();
                    break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_type:
                if (gridPopup == null) {
                    gridPopup = new DropDownGridPopup(this);
                }
                gridPopup.showAsDropDown(v);
                gridPopup.setData(data);
                gridPopup.setOnDismissListener(() -> {
                    mViewModel.cate_id.set("-1");
                });
                gridPopup.setSureListener(bean -> {
                    String categoryId = bean.getId();
                    String name = bean.getName();
//                    binding.tvBeGoodat.setText(TextUtils.equals("全部", name) ? getString(R.string.Good_goods) : name);
//                    viewModel.setCategoryId(categoryId);
//                    helper.start();
                    mBinding.btnType.setText(name);
                    mViewModel.cate_id.set(categoryId);
                    helper.start();

                });
                break;
        }
    }

    @Subscribe
    public void onCaseThumbUp(CaseThumbUpEvent event) {
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getCaseId())) {
                caseBean.setIs_relation(event.isThumbUp() ? 1 : 0);
                caseBean.setLike_number(caseBean.getLike_number() + (event.isThumbUp() ? 1 : -1));
                mViewModel.thumbUpObservable.update();
                break;
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 1) {
            return;
        }
        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            mViewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        CaseBean bean = null;
        for (CaseBean caseBean : adapter.getData()) {
            if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                bean = caseBean;
                break;
            }
        }

        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            mViewModel.commentObservable.update();
        }
    }


    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.getCaseDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        mViewModel.getCaseDetail(event.getTarget());
    }

}

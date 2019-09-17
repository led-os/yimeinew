package com.chengzi.app.ui.onlinequestionandanswer.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CustomTabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindBaseAdapter;
import com.chengzi.app.adapter.MainDoctorAdapter;
import com.chengzi.app.adapter.OnlineQuestionAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityOnlineQuestionBinding;
import com.chengzi.app.databinding.ItemOnlineQuestionHeaderLayoutBinding;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.viewmodel.OnlineQuestionViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线问答
 *
 * @ClassName:QuestionAndAnswerOnlineActivity
 * @PackageName:com.yimei.app.ui.onlinequestionandanswer.activity
 * @Create On 2019/4/13 0013   16:35
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/13 0013 handongkeji All rights reserved.
 */
public class OnlineQuestionActivity extends BaseActivity<ActivityOnlineQuestionBinding,
        OnlineQuestionViewModel> implements View.OnClickListener {

    private static final String DOCTOR_TYPE = "2";   //  医生
    private static final String COUNSELOR_TYPE = "3";   //  咨询师

    private OnlineQuestionAdapter adapter;
    private PagingLoadHelper helper;
    private ItemOnlineQuestionHeaderLayoutBinding headerBinding;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_online_question;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(this);

        initRecyclerView();

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        observe();
        helper.start();
        mViewModel.categoryList();
    }

    private void observe() {
        mViewModel.liveData.observe(this, qAbeanWraper -> {
            List<FindBaseBean<QAbean>> questionList = qAbeanWraper.getQuestionList();
            headerBinding.getRoot().setVisibility(View.VISIBLE);
            List<Object> list = new ArrayList<>();
            if (questionList != null) {
                for (FindBaseBean<QAbean> findBaseBean : questionList) {
                    list.add(findBaseBean);
                    list.addAll(findBaseBean.getData());
                }
            }

            helper.onComplete(list);

            QAbean.NumBean num = qAbeanWraper.getNum();
            if (num != null) {
                headerBinding.tvResolved.setText(String.valueOf(num.getResolved()));
                headerBinding.tvUnresolved.setText(String.valueOf(num.getUnsolved()));
            }
        });

        mViewModel.categoryLive.observe(this, navigatorItems -> {

//            if (!navigatorItems.isEmpty()) {
//                navigatorItems.remove(0);
//            }

            for (CategoryItem categoryItem : navigatorItems) {
                String name = categoryItem.getName();
                CustomTabLayout.Tab tab = headerBinding.tabLayoutDoctor.newTab().setText(name);
                tab.setTag(categoryItem);
                headerBinding.tabLayoutDoctor.addTab(tab);

                CustomTabLayout.Tab tab1 = headerBinding.tabLayoutCounselor.newTab().setText(name);
                tab1.setTag(categoryItem);
                headerBinding.tabLayoutCounselor.addTab(tab1);
            }

//            if (!navigatorItems.isEmpty()) {
//                String id = navigatorItems.get(0).getId();
//                mViewModel.questionPeo(DOCTOR_TYPE, id);
//                mViewModel.questionPeo(COUNSELOR_TYPE, id);
//            }
        });

        mViewModel.doctorLive.observe(this, doctorBeans -> {

            BaseQuickAdapter adapter = (BaseQuickAdapter) headerBinding.recyclerDoctor.getAdapter();
            if (doctorBeans == null || doctorBeans.isEmpty()) {
                int size = adapter.getData().size();
                adapter.getData().clear();
                adapter.notifyItemRangeRemoved(0, size);
                return;
            }

            int selectedTabPosition = headerBinding.tabLayoutDoctor.getSelectedTabPosition();
            CategoryItem tag = (CategoryItem) headerBinding.tabLayoutDoctor.getTabAt(selectedTabPosition).getTag();
            FindBaseBean<DoctorBean> baseBean;
            if (adapter.getData().isEmpty()) {

                baseBean = new FindBaseBean<>();
                baseBean.setCategory_id(tag.getId());
                baseBean.setName(tag.getName());
                baseBean.setData(doctorBeans);
                adapter.addData(baseBean);
                adapter.notifyItemInserted(0);
            } else {
                baseBean = (FindBaseBean<DoctorBean>) adapter.getData().get(0);
                baseBean.setCategory_id(tag.getId());
                baseBean.setName(tag.getName());
                baseBean.setData(doctorBeans);
                adapter.notifyItemChanged(0, baseBean);
            }
        });
        mViewModel.counselorLive.observe(this, doctorBeans -> {
            BaseQuickAdapter adapter = (BaseQuickAdapter) headerBinding.recyclerCounselor.getAdapter();
            if (doctorBeans == null || doctorBeans.isEmpty()) {
                int size = adapter.getData().size();
                adapter.getData().clear();
                adapter.notifyItemRangeRemoved(0, size);
                return;
            }

            int selectedTabPosition = headerBinding.tabLayoutCounselor.getSelectedTabPosition();
            CategoryItem tag = (CategoryItem) headerBinding.tabLayoutCounselor.getTabAt(selectedTabPosition).getTag();
            FindBaseBean<DoctorBean> baseBean;
            if (adapter.getData().isEmpty()) {

                baseBean = new FindBaseBean<>();
                baseBean.setCategory_id(tag.getId());
                baseBean.setName(tag.getName());
                baseBean.setData(doctorBeans);
                adapter.addData(baseBean);
                adapter.notifyItemInserted(0);
            } else {
                baseBean = (FindBaseBean<DoctorBean>) adapter.getData().get(0);
                baseBean.setCategory_id(tag.getId());
                baseBean.setName(tag.getName());
                baseBean.setData(doctorBeans);
                adapter.notifyItemChanged(0, baseBean);
            }
        });
    }

    private void initRecyclerView() {

        View headerView = initHeader();

        adapter = new OnlineQuestionAdapter();
        adapter.setHeaderAndEmpty(true);
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setPullupEnable(false);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);
        adapter.addHeaderView(headerView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            int itemViewType = adapter.getItemViewType(position + adapter.getHeaderLayoutCount());
            if (itemViewType == OnlineQuestionAdapter.TYPE_ITEM) {
                String questinId = ((QAbean) adapter.getItem(position)).getId();
                QuestionDetailActivity.start(this, questinId);
            }
        });
    }

    private View initHeader() {
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.item_online_question_header_layout,
                mBinding.swipeRefreshView.getRecyclerView(), false);
        headerBinding.getRoot().setVisibility(View.GONE);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);

        headerBinding.tabLayoutDoctor.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                CategoryItem categoryItem = (CategoryItem) tab.getTag();
                String id = categoryItem.getId();
                mViewModel.questionPeo(DOCTOR_TYPE, id);
            }
        });

        headerBinding.tabLayoutCounselor.addOnTabSelectedListener(new CustomTabLayout.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(CustomTabLayout.Tab tab) {
                CategoryItem categoryItem = (CategoryItem) tab.getTag();
                String id = categoryItem.getId();
                mViewModel.questionPeo(COUNSELOR_TYPE, id);
            }
        });

        List list = new ArrayList();
        headerBinding.recyclerDoctor.setAdapter(createAdapter(pool, list, 2));
        headerBinding.recyclerCounselor.setAdapter(createAdapter(pool, new ArrayList(list), 3));

        headerBinding.setListener(this);
        return headerBinding.getRoot();
    }

    private BaseQuickAdapter createAdapter(RecyclerView.RecycledViewPool pool, List list, final int userType) {
        FindBaseAdapter<DoctorBean> baseAdapter = new FindBaseAdapter();
        baseAdapter.setFactory(new FindBaseAdapter.ItemAdapterFactory<DoctorBean>() {
            @Override
            public BaseQuickAdapter createAdapter(List<DoctorBean> list) {
                MainDoctorAdapter adapter = new MainDoctorAdapter(userType);
                adapter.setNewData(list);
                return adapter;
            }

            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });
        baseAdapter.setNewData(list);
        return baseAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:  //  搜索
                QuestionSearchActivity.start(this, Sys.EXTRA_STATUS_ALL);
                break;
            case R.id.btn_ask:    //  我要问
                int identity = AccountHelper.getIdentity();
                if (identity > 1) {
                    ToastUtils.showShort("只有普通用户可以提问题");
                    return;
                }
                startActivity(new Intent(this, AskQuestionActivity.class));
                break;
            case R.id.btn_answer:   //  我要答
                ResolvingQuestionActivity.start(this, Sys.EXTRA_STATUS_UNRESOLVED);
                break;
            case R.id.btn_resolving:
                ResolvingQuestionActivity.start(this, Sys.EXTRA_STATUS_UNRESOLVED);
                break;
            case R.id.btn_resolved:
                ResolvingQuestionActivity.start(this, Sys.EXTRA_STATUS_RESOLVED);
                break;
        }
    }
}

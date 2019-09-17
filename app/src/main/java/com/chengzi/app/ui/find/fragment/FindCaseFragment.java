package com.chengzi.app.ui.find.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.viewpager2.widget.ViewPager2;
import android.text.TextUtils;
import android.util.SparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CaseListAdapter;
import com.chengzi.app.databinding.FragmentFindCaseBinding;
import com.chengzi.app.databinding.ItemFindCaseBaseLayoutBinding;
import com.chengzi.app.event.AddChildCommentEvent;
import com.chengzi.app.event.AddCommentEvent;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.event.DelChildCommentEvent;
import com.chengzi.app.event.DelCommentEvent;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.find.viewmodel.FindCaseViewModel;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * 找案例
 *
 * @ClassName:FindCaseFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/4/11 0011   10:47
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class FindCaseFragment extends BaseFragment<FragmentFindCaseBinding, FindCaseViewModel> {

    private MyAdapter baseAdapter;
    private PagingLoadHelper helper;

    public FindCaseFragment() {
    }

    public static FindCaseFragment newInstance() {
        android.os.Bundle args = new Bundle();
        FindCaseFragment fragment = new FindCaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find_case;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);

        viewModel.setLifecycleOwner(this);

        baseAdapter = new MyAdapter();
        baseAdapter.setFactory(new ItemAdapterFactory() {
            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });
        binding.swipeRefreshView.setAdapter(baseAdapter);
        binding.swipeRefreshView.setPullupEnable(false);


        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        observe();

        helper.start();

    }

    private void observe(){

        viewModel.casesLive.observe(this, list -> {
            helper.onComplete(list);
        });

        viewModel.caseDetailLive.observe(this,caseNoteListBeans -> {
            if (caseNoteListBeans == null || caseNoteListBeans.isEmpty()) {
                return;
            }
            CaseNoteListBean bean = caseNoteListBeans.get(0);
            for (FindBaseBean<CaseBean> baseBean : baseAdapter.getData()) {
                boolean find = false;
                for (CaseBean caseBean : baseBean.getData()) {
                    if (TextUtils.equals(bean.getCases_id(),caseBean.getId())) {
                        caseBean.setComment_number(bean.getComment_number()+"");
                        viewModel.commentObservable.update();
                        find = true;
                        break;
                    }
                }
                if (find) {
                    break;
                }
            }

        });
    }

    @Subscribe
    public void onCaseThumbUp(CaseThumbUpEvent event) {
        List<FindBaseBean<CaseBean>> list = baseAdapter.getData();
        for (FindBaseBean<CaseBean> baseBean : list) {
            for (CaseBean caseBean : baseBean.getData()) {
                if (TextUtils.equals(caseBean.getId(), event.getCaseId())) {
                    caseBean.setIs_relation(event.isThumbUp() ? 1 : 0);
                    caseBean.setLike_number(caseBean.getLike_number() + (event.isThumbUp() ? 1 : -1));
                    viewModel.thumbUpObservable.update();
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onCommentAdd(AddCommentEvent event) {
        if (event.getCommentType() != 1) {
            return;
        }
        CaseBean bean = null;
        for (FindBaseBean<CaseBean> baseBean : baseAdapter.getData()) {
            for (CaseBean caseBean : baseBean.getData()) {
                if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                    bean = caseBean;
                    break;
                }
            }
            if (bean != null) {
                break;
            }
        }
        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onChildCommentAdd(AddChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        CaseBean bean = null;
        for (FindBaseBean<CaseBean> baseBean : baseAdapter.getData()) {
            for (CaseBean caseBean : baseBean.getData()) {
                if (TextUtils.equals(caseBean.getId(), event.getTarget())) {
                    bean = caseBean;
                    break;
                }
            }
            if (bean != null) {
                break;
            }
        }
        if (bean != null) {
            String comment_number = bean.getComment_number();
            bean.setComment_number(String.valueOf(Integer.valueOf(comment_number) + 1));
            viewModel.commentObservable.update();
        }
    }

    @Subscribe
    public void onCommentDel(DelCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }

    @Subscribe
    public void onChildCommentDel(DelChildCommentEvent event) {
        if (event.getCommentType() != 1) {  //  评论类型  1 案例 2 美人筹 3 发现圈
            return;
        }
        viewModel.getCaseDetail(event.getTarget());
    }

    private class MyAdapter extends BaseQuickAdapter<FindBaseBean<CaseBean>, BaseViewHolder> {
        private SparseArray<Parcelable> stateSparseArray = new SparseArray<>();
        private ItemAdapterFactory factory;

        public void setFactory(ItemAdapterFactory factory) {
            this.factory = factory;
        }

        public MyAdapter() {
            super(R.layout.item_find_case_base_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, FindBaseBean<CaseBean> item) {
            ItemFindCaseBaseLayoutBinding binding = DataBindingUtil.bind(helper.itemView);

            factory.setRecyclerPool(binding.viewPager2.getRecyclerView());
            binding.setItemCount(item.getData().size());
            binding.textView.setText(item.getName());

            CaseListAdapter adapter = new CaseListAdapter();
            adapter.setCategoryName(item.getName());
            adapter.setBaseViewModel(viewModel);
            adapter.replaceData(item.getData());
            binding.viewPager2.swapAdapter(adapter, false);
            binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    binding.setSelectedPos(position);
                }
            });
            Parcelable parcelable = stateSparseArray.get(helper.getAdapterPosition());
            if (parcelable != null) {
                binding.viewPager2.getRecyclerView().getLayoutManager().onRestoreInstanceState(parcelable);
            }
        }

        @Override
        public void onViewRecycled(@NonNull BaseViewHolder holder) {
            super.onViewRecycled(holder);
            ViewPager2 viewPager2 = holder.getView(R.id.viewPager2);
            if (viewPager2 == null) {
                return;
            }
            Parcelable parcelable = viewPager2.getRecyclerView().getLayoutManager().onSaveInstanceState();
            stateSparseArray.put(holder.getAdapterPosition(), parcelable);
        }

    }

    public interface ItemAdapterFactory {
        void setRecyclerPool(RecyclerView recyclerView);
    }

}

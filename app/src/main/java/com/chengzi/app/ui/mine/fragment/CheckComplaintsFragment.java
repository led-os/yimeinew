package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.NineImageAdapter;
import com.chengzi.app.databinding.FragmentCheckComplaintsBindingImpl;
import com.chengzi.app.databinding.ItemComplaintsLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.ComplaintListBean;
import com.chengzi.app.ui.mine.viewmodel.CheckComplaintsModel;

import java.util.ArrayList;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * 投诉我的
 *
 * @ClassName:CheckComplaintsFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/30 0030   16:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/30 0030 handongkeji All rights reserved.
 */

public class CheckComplaintsFragment extends LazyloadFragment<FragmentCheckComplaintsBindingImpl, CheckComplaintsModel> {
    private PagingLoadHelper helper;

    public static CheckComplaintsFragment newInstance() {
        CheckComplaintsFragment fragment = new CheckComplaintsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_check_complaints;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        CheckComplaintsAdapter adapter = new CheckComplaintsAdapter();
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.complaintListLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class CheckComplaintsAdapter extends BaseQuickAdapter<ComplaintListBean, BaseViewHolder> {

        public CheckComplaintsAdapter() {
            super(R.layout.item_complaints_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ComplaintListBean item) {
            ItemComplaintsLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
            //tv_sex性别
            String uu_gender = item.getGender();
            binding.tvSex.setBackgroundResource(uu_gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(uu_gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);

            binding.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            NineImageAdapter imageAdapter = new NineImageAdapter();
            binding.mRecyclerView.setAdapter(imageAdapter);
            ArrayList<String> strings = (ArrayList<String>) item.getComplanint_img();
            if (strings != null && strings.size() > 0)
                imageAdapter.setNewData(strings);
            //看大图
            imageAdapter.setOnItemClickListener((adapter1, view, position) -> {
                startActivity(new Intent(getContext(), PictureSwitcherActivity.class)
                        .putExtra(PICTURE_URLS, strings)
                        .putExtra(PICTURE_INDEX, position)
                );
            });
        }
    }

    private ClickEventHandler<ComplaintListBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getUser_id());
                break;
        }
    };
}

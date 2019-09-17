package com.chengzi.app.ui.mine.activity.cases;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.CommonUtils;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.adapter.CommentAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityCaseDetailsBindingImpl;
import com.chengzi.app.databinding.ItemCaseDetailsLayoutBinding;
import com.chengzi.app.databinding.ItemImageLayoutBinding;
import com.chengzi.app.ui.comment.OnCommentClickListener;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.viewmodel.CaseSearchPromotionViewModel;

import java.util.ArrayList;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * 案例详情
 *
 * @ClassName:CaseDetailsActivity
 * @PackageName:com.yimei.app.ui.mine.activity.cases
 * @Create On 2019/4/15 0015   17:29
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class CaseDetailsActivity extends BaseActivity<ActivityCaseDetailsBindingImpl, CaseSearchPromotionViewModel> {
    private static final String TAG = CaseDetailsActivity.class.getSimpleName();

    //1案例管理 / 案例站内搜索推广
    private int form;
    private String cases_id = "", cate_id = "", hospital_img = "", hospital_name = "", catename = "", like_number = "";
    private CaseDetailsAdapter imageAdapter;
    private boolean like;   //// 当前用户是否点赞
    private Drawable drawable_n;

    /**
     * @param context
     * @param form          1案例管理 / 案例站内搜索推广
     * @param cases_id      案例id
     * @param hospital_img  案例医院头像
     * @param hospital_name 案例医院名称
     * @param catename      案例类型
     * @param like_number   案例点赞数
     * @param like          当前用户是否点赞
     */
    public static void start(Context context, int form, String cases_id, String cate_id,
                             String hospital_img, String hospital_name,
                             String catename, String like_number, boolean like) {
        context.startActivity(new Intent(context, CaseDetailsActivity.class)
                .putExtra("form", form)
                .putExtra("cases_id", cases_id)
                .putExtra("hospital_img", hospital_img)
                .putExtra("hospital_name", hospital_name)
                .putExtra("catename", catename)
                .putExtra("like_number", like_number)
                .putExtra("like", like)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_case_details;
    }

    //Indexing paused due to batch update
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(clickListener);
        mBinding.setModel(mViewModel);
        form = getIntent().getIntExtra("form", -1);
        cases_id = getIntent().getStringExtra("cases_id");
        cate_id = getIntent().getStringExtra("cate_id");
        hospital_img = getIntent().getStringExtra("hospital_img");
        hospital_name = getIntent().getStringExtra("hospital_name");
        catename = getIntent().getStringExtra("catename");
        like_number = getIntent().getStringExtra("like_number");
        like = getIntent().getBooleanExtra("like", true);
        ImageLoader.loadRoundImage(mBinding.ivHead, hospital_img, R.drawable.morentouxiang);
        mBinding.tvName.setText(hospital_name);
        mBinding.tvCasesName.setText(catename);
        mBinding.tvPraise.setText(like_number);

        if (form == 1) {    //案例管理可以点赞
            drawable_n = getResources().getDrawable(like ? R.drawable.dianzan_weidian : R.drawable.dianzan_yidian);
        } else {            //推广案例不能点赞
            drawable_n = getResources().getDrawable(R.drawable.dianzan_weidian);
            like = false;
        }
        drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
        mBinding.tvPraise.setCompoundDrawables(drawable_n, null, null, null);

        mBinding.rlAdd.setVisibility(form == 1 ? View.VISIBLE : View.GONE);
        //变美过程
        initmRecyclerView();

        //评论
        initCommentRecycler();

        //发送评论
        mBinding.etCommentContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //进行自己操作
                    String content = mBinding.etCommentContent.getText().toString();
                    showLoading(Sys.LOADING);
                    mViewModel.casesComment(cases_id, "0", "0", content);
                    return true;
                }
                return false;
            }
        });
        //美丽日记的列表
        mViewModel.caseNoteListLive.observe(this, caseNoteList -> {
            dismissLoading();
            if (caseNoteList != null && caseNoteList.size() > 0) {
                imageAdapter.setNewData(caseNoteList);
            } else {
                imageAdapter.setNewData(null);
            }
        });
        //点赞
        mViewModel.caseThumbUpLive1.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                like = false;
                drawable_n = getResources().getDrawable(R.drawable.dianzan_yidian);
                drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
                mBinding.tvPraise.setCompoundDrawables(drawable_n, null, null, null);
            }
        });
        //删除评论
        mViewModel.casesCommentDeleteLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mViewModel.caseCommentList(cases_id);
            }
        });
        //评论
        mViewModel.caseCommentLive.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mViewModel.caseCommentList(cases_id);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //案例详情]
        showLoading(Sys.LOADING);
//         mViewModel.casesDetail(cases_id);
        mViewModel.caseCommentList(cases_id);
//        请求变美过程的列表
        mViewModel.caseNoteList(cases_id);

    }

    //变美过程
    private void initmRecyclerView() {
        mBinding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageAdapter = new CaseDetailsAdapter();
        mBinding.mRecyclerView.setAdapter(imageAdapter);
//        imageAdapter.addData("");
//        imageAdapter.addData("");
    }

    public class CaseDetailsAdapter extends BaseQuickAdapter<CaseNoteListBean, BaseViewHolder> {
        public CaseDetailsAdapter() {
            super(R.layout.item_case_details_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CaseNoteListBean item) {
            ItemCaseDetailsLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.executePendingBindings();
            ArrayList<String> images = (ArrayList<String>) item.getImages();
            if (images != null && images.size() > 0) {
                binding.mRecyclerView.setLayoutManager(new GridLayoutManager(CaseDetailsActivity.this, 3));
                NineImageAdapter imageAdapter = new NineImageAdapter();
                binding.mRecyclerView.setAdapter(imageAdapter);

                imageAdapter.setNewData(images);
                //看大图
                imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        startActivity(new Intent(CaseDetailsActivity.this, PictureSwitcherActivity.class)
                                .putExtra(PICTURE_URLS, images)
                                .putExtra(PICTURE_INDEX, position)
                        );
                    }
                });
            }
        }
    }

    private void initCommentRecycler() {

        mBinding.recyclerComment.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // TODO: 2019/5/24 0024    点击评论
        CommentAdapter commentAdapter = new CommentAdapter(1);
        commentAdapter.setListener(new OnCommentClickListener() {
            @Override
            public void onContent(View view, CommentBean bean) {
                //评论

            }

            @Override
            public void onDelete(View view, CommentBean bean) {
                showLoading(Sys.LOADING);
                mViewModel.casesCommentDelete("" + bean.getId());
            }
        });
        mBinding.recyclerComment.setAdapter(commentAdapter);
        /*评论列表*/
        mViewModel.caseCommentListLive.observe(this, caseCommentList -> {
            if (caseCommentList != null && caseCommentList.getData() != null && caseCommentList.getData().size() > 0) {
                String all_comment_number = String.valueOf(caseCommentList.getTotal());
                mBinding.tvCommectNumber.setText(!TextUtils.isEmpty(all_comment_number) ?
                        "评论(" + all_comment_number + ")" : "评论(0)");
                commentAdapter.setNewData(caseCommentList.getData());
            } else {
                commentAdapter.setNewData(new ArrayList<>());
            }
        });
    }

    public class NineImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public NineImageAdapter() {
            super(R.layout.item_image_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ItemImageLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            ImageView view = binding.ivImg;
            int height = CommonUtils.dip2px(CaseDetailsActivity.this, 60);
            int left = CommonUtils.dip2px(CaseDetailsActivity.this, 5);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(height, height);
            p.setMargins(left, 0, 0, left);
            view.setLayoutParams(p);
            binding.setUrl(item);
            binding.executePendingBindings();

        }
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_add:  // 添加日记
                AddCaseActivity.start(this, 2, cases_id);
                break;
            case R.id.tv_praise:  // 点赞
//                if (!like) { //未点赞
//                    toast("点赞");
//                    like = !like;
//                    if (form == 1) {    //案例管理可以点赞
//                        drawable_n = getResources().getDrawable(like ? R.drawable.dianzan_yidian : R.drawable.dianzan_weidian);
//                    } else {            //推广案例不能点赞
//                        drawable_n = getResources().getDrawable(R.drawable.dianzan_weidian);
//                        like = true;
//                    }
//                    drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
//                    mBinding.tvPraise.setCompoundDrawables(drawable_n, null, null, null);
//                }
                if (form == 1 && like) {    //案例管理可以点赞
                    mViewModel.caseClickLike(cases_id);
                }
                break;
        }
    };
}
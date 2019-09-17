package com.chengzi.app.ui.peopleraise.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.TextView;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.model.CommentModel;
import com.chengzi.app.ui.comment.viewmodel.BaseCommentViewModel;
import com.chengzi.app.ui.homepage.model.UserHomeService;
import com.chengzi.app.ui.mine.model.MineService;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.bean.RaiseJoinUserBean;
import com.chengzi.app.ui.peopleraise.model.RaiseService;
import com.chengzi.app.utils.AlwaysAliveObservable;
import com.chengzi.app.utils.URLUtils;

import java.util.List;

public class PeopleRaiseDetailViewModel extends BaseCommentViewModel {

    public final MutableLiveData<RaiseBean> raiseLive = new MutableLiveData<>();

    public final MutableLiveData<CommentBean> commentDetailLive = new MutableLiveData<>();
    public final MutableLiveData<List<RaiseBean>> recommendRaiseLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> addCommentLive = new MutableLiveData<>();  //  添加评论
    public final MutableLiveData<Boolean> joinPlanLive = new MutableLiveData<>();  //  参与美人筹

    public final MutableLiveData<PageBean<CommentBean>> commentListLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> deleteLive = new MutableLiveData<>();

    public final MutableLiveData<Pair<RaiseJoinUserBean, Integer>> attentionLive = new MutableLiveData<>();

    public final ObservableField<String> content = new ObservableField<>();  //  评论内容

    public final AlwaysAliveObservable attentionObservable = new AlwaysAliveObservable();

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();
    private int authStatus = -1;

    //美人筹总人数
    private int all_count = 0;
    //美人筹参与人数
    private int count = 0;

    private String planId;
    private String categoryId;

    private final CommentModel commentModel = new CommentModel();
    private final RaiseService raiseService;

    public PeopleRaiseDetailViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
    }

    public void planDetails() {
        Params params = Params.newParams()
                .put("user_id", TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId())
                .put("plan_order_id", planId);
        raiseService.planDetails(params.params())
                .subscribe(new SimpleObserver<ResponseBean<RaiseBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<RaiseBean> raiseBeanResponseBean) {
                        raiseLive.postValue(raiseBeanResponseBean.getData());
                    }
                });

    }

    //  推荐美人筹列表
    public void planRecommendList(String plan_order_id) {
        raiseService.planRecommendList(categoryId, plan_order_id)
                .subscribe(new SimpleObserver<ResponseBean<List<RaiseBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<RaiseBean>> listResponseBean) {
                        recommendRaiseLive.postValue(listResponseBean.getData());
                    }
                });
    }

    //  添加评论
    public void planCommentAdd() {

        String userId = AccountHelper.getUserId();

        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", userId)
                .put("plan_order_id", planId)
                .put("content", URLUtils.encode(content.get()));

        raiseService.planCommentAdd(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addCommentLive.postValue(true);
                    }
                });
        content.set("");
    }

    // 参与美人筹
    public void joinPlan() {
        String token = AccountHelper.getToken();
        String userId = AccountHelper.getUserId();
        raiseService.joinToPlan(token, userId, planId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        joinPlanLive.postValue(true);
                    }
                });
    }

    //  评论列表 ,详情页最多展示3条评论
    public void planCommentList() {
        raiseService.planCommentList(planId, "3", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> pageBeanResponseBean) {
                        commentListLive.postValue(pageBeanResponseBean.getData());
                    }
                });
    }

    public void deleteComment(String commentId) {
        String userId = AccountHelper.getUserId();
        // type 1 美人筹  2  发现圈 3 案例
        commentModel.deleteComment(commentId, "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        deleteLive.postValue(true);
                    }
                });
    }

    public void fetchCommentDetail(String commentId) {
        commentModel.getChildComment(commentId, commentType)
                .subscribe(new SimpleObserver<ResponseBean<CommentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CommentBean> commentBeanResponseBean) {
                        commentDetailLive.postValue(commentBeanResponseBean.getData());
                    }
                });
    }

    //  关注
    public void follow(RaiseJoinUserBean bean) {
        Api.getApiService(UserHomeService.class)
                .findFollow(AccountHelper.getToken(), AccountHelper.getUserId(), bean.getUser_id())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        attentionLive.postValue(new Pair<>(bean, bean.getFollow() == 1 ? 0 : 1));
                    }
                });
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    //总人数 和 参与人数
    public void setCount(int all_count, int count) {
        this.all_count = all_count;
        this.count = count;
        if (all_count <= count) {
            setTextView(btnParticipate, false);
        }
    }

    //用户信息
    public void userInfo() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        Api.getApiService(MineService.class).userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        userInfoLiveData.postValue(userInfoBeanResponseBean.getData());
                    }
                });
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    private TextView btnParticipate;

    public void setTextView(TextView btnParticipate, boolean b) {
        this.btnParticipate = btnParticipate;
        btnParticipate.setEnabled(!b ? false : true);
    }

    public void setTextView(boolean b) {
        btnParticipate.setEnabled(!b ? false : true);
    }
}

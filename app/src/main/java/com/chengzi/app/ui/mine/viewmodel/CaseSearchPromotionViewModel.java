package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.mine.bean.CaseManageListBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.List;

public class CaseSearchPromotionViewModel extends CaseBaseViewModel implements IRequest {

    private MineHospitalService mineService;

    public CaseSearchPromotionViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    //案例管理列表/案例推广
    public final MutableLiveData<List<CaseManageListBean>> caseManageLive = new MutableLiveData<>();

    //    public final MutableLiveData<ResponseBean<String>> clickLikeLive = new MutableLiveData<>();//点赞

    public final MutableLiveData<PageBean<CommentBean>> caseCommentListLive = new MutableLiveData<>();//评论列表

    public final MutableLiveData<List<CaseNoteListBean>> caseNoteListLive = new MutableLiveData<>();//案例详情
    public final MutableLiveData<ResponseBean> caseCommentLive = new MutableLiveData<>();//评论删除
    public final MutableLiveData<ResponseBean> casesCommentDeleteLive = new MutableLiveData<>();//评论删除

    // Promotion - 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
    public final MutableLiveData<ResponseBean> setSearchPromotionAmountLiveData = new MutableLiveData<>();
    // 我的推广-添加搜索推广(包括商品、医院等所有的搜索推广)
    public final MutableLiveData<ResponseBean> addSearchPromotionItemLiveData = new MutableLiveData<>();
    // 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
    public final MutableLiveData<ResponseBean> removeSearchPromotionItemLiveData = new MutableLiveData<>();

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    //    1案例管理 / 案例站内搜索推广
    public int form = -1;
    //分类id
    public String cate_id;

    @Override
    public void onRequest(int currentPage, int pageSize) {
        if (form == 1) {
            //案例管理 所以uid 和 clk_id都是user_id
            String userId = AccountHelper.getUserId();
            mineService.caseManagement(AccountHelper.getToken(), userId, userId,userId, String.valueOf(currentPage), String.valueOf(pageSize))
                    .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseManageListBean>>>() {
                        @Override
                        public void onSuccess(ResponseBean<PageBean<CaseManageListBean>> data) {
                            caseManageLive.postValue(data.getData().getData());
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            super.onError(code, errorMsg);
                            caseCommentListLive.postValue(null);
                        }
                    });
        } else {
            mineService.casesPromotion(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), cate_id, String.valueOf(currentPage), String.valueOf(pageSize))
                    .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseManageListBean>>>() {
                        @Override
                        public void onSuccess(ResponseBean<PageBean<CaseManageListBean>> data) {
                            caseManageLive.postValue(data.getData().getData());
                        }

                        @Override
                        public void onError(int code, String errorMsg) {
                            super.onError(code, errorMsg);
                            caseCommentListLive.postValue(null);
                        }
                    });
        }
    }

    //    public final MutableLiveData<Pair<CaseManageListBean, Integer>> caseThumbUpLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> caseThumbUpLive1 = new MutableLiveData<>();
//    public final AlwaysAliveObservable thumbUpObservable = new AlwaysAliveObservable();     //  点赞
//
//    public void setLifecycleOwner(LifecycleOwner owner) {
//        caseThumbUpLive.observe(owner, pair -> {
//            CaseManageListBean caseBean = pair.first;
//            Integer integer = pair.second;
////            caseBean.setLike(integer == 1);
////            caseBean.setLike_number(caseBean.getLike_number() + (integer == 1 ? 1 : -1));
//            thumbUpObservable.update();
//            EventBus.getDefault().post(new CaseThumbUpEvent(caseBean.getId(), integer == 1));
//        });
//    }
//
//    public void caseClickLike(CaseManageListBean caseBean, String cases_id) {
////        mineService.caseClickLike(cate_id, cases_id, AccountHelper.getUserId(), AccountHelper.getUserId())
////                .subscribe(new SimpleObserver<ResponseBean>() {
////                    @Override
////                    public void onSuccess(ResponseBean responseBean) {
////                        caseThumbUpLive.postValue(responseBean);
////                    }
////                });
//        boolean thumbUp = caseBean.isLike();
//        mineService.caseThumbUp(cases_id, AccountHelper.getUserId(), AccountHelper.getUserId())
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        caseThumbUpLive.postValue(new Pair<>(caseBean, thumbUp ? 0 : 1));
//                    }
//                });
//    }

    //    //弃用->链接次方法的案例详情弃用
    public void caseClickLike(String cases_id) {
//        mineService.caseThumbUp(cases_id, AccountHelper.getUserId(), AccountHelper.getUserId())
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        caseThumbUpLive1.postValue(responseBean);
//                    }
//                });
//
    }

    /*案例详情*/
//    public void casesDetail(String case_note_id) {
//        mineService.casesDetail(case_note_id + "")
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean responseBean) {
//                        caseDetailLive.postValue(responseBean);
//                    }
//                });
//    }

    /*评论列表*/
    public void caseCommentList(String cases_id) {
        mineService.caseCommentList(cases_id)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> responseBean) {
                        caseCommentListLive.postValue(responseBean.getData());
                    }
                });
    }

    /*变美过程---美丽日记列表*/
    public void caseNoteList(String cases_id) {
        mineService.caseNoteList(AccountHelper.getToken(), AccountHelper.getUserId(), cases_id, "1", String.valueOf(Integer.MAX_VALUE))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseNoteListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseNoteListBean>> responseBean) {
                        caseNoteListLive.postValue(responseBean.getData().getData());
                    }
                });
    }

    /*评论的删除*/
    public void casesCommentDelete(String comment_id) {
        mineService.casesCommentDelete(AccountHelper.getUserId(), comment_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        casesCommentDeleteLive.postValue(responseBean);
                    }
                });
    }

    /*评论*/
    public void casesComment(String cases_id, String to_uid, String pid, String content) {
        /*
        * @Field("cases_id") String cases_id,案例ID

                                         @Field("from_uid") String from_uid, 评论人ID
                                         @Field("to_uid") String to_uid,     被评论人ID，没有则填0
                                         @Field("pid") String pid,           被评论的主评论（最顶层的）ID，没有则填0
                                         @Field("content") String content    评论内容
        * */
        mineService.caseComment(cases_id, AccountHelper.getUserId(), to_uid, pid, content)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseCommentLive.postValue(responseBean);
                    }
                });
    }

    /**
     * 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
     *
     * @param id     要设置的对象的id，比如医生ID、商品ID
     * @param type   类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param amount 设置的限额数
     */
    public void setSearchPromotionAmount(String id, String type, String amount) {
        mineService.setSearchPromotionAmount(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type, amount)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        setSearchPromotionAmountLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        setSearchPromotionAmountLiveData.postValue(null);
                    }
                });
    }


    /**
     * 添加搜索推广(包括商品、医院等所有的搜索推广)
     *
     * @param id       【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type     【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param //amount 设置的限额数，不填则默认为0，不限额
     */
    public void addSearchPromotionItem(String id, String type) {
        mineService.addSearchPromotionItem(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addSearchPromotionItemLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        addSearchPromotionItemLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
     *
     * @param id   【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type 【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     */
    public void removeSearchPromotionItem(String id, String type) {
        mineService.removeSearchPromotionItem(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        removeSearchPromotionItemLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        removeSearchPromotionItemLiveData.postValue(null);
                    }
                });
    }
}

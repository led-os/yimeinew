package com.chengzi.app.ui.cases.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CaseService {

    /**
     * 案例点赞
     *
     * @param casesId 【必填】案例Id
     * @param uid     【必填】用户ID
     * @return
     */
    @FormUrlEncoded
    @POST("caseClickLike")
    Observable<ResponseBean<String>> caseThumbUp(@Field("cases_id") String casesId,
                                                 @Field("user_id") String user_id,
                                                 @Field("uid") String uid);


    /**
     * 案例评论列表
     *
     * @param caseId
     * @param pageSize
     * @param page
     * @return
     */
    @GET("caseCommentList")
    Observable<ResponseBean<PageBean<CommentBean>>> caseCommentList(@Query("cases_id") String caseId,
                                                                    @Query("limit") String pageSize,
                                                                    @Query("page") String page);

    /**
     * 案例管理--评论添加(黄)
     *
     * @param params
     */
    @FormUrlEncoded
    @POST("caseComment")
    Observable<ResponseBean> addCaseComment(@FieldMap Map<String, String> params);

    /**
     * 扣除案例搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutCasesSearch")
    Observable<ResponseBean> promotionCutCasesSearch(@Field("id") String id,
                                                     @Field("uid") String userId,
                                                     @Field("code")String code);

}

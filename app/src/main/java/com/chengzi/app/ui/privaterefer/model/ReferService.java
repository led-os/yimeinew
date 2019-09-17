package com.chengzi.app.ui.privaterefer.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.privaterefer.bean.ReferChoseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ReferService {

    /**
     * 私享咨询、在线面诊 发起(小芸) 3.6
     *
     * @param userId     发起人id （必传）
     * @param categoryId 分类id （必传）
     * @param referType  咨询类型 （必传）（1-私享咨询 2-在线面诊）
     * @return
     */
    @FormUrlEncoded
    @POST("sheetStart")
    Observable<ResponseBean<String>> sheetStart(@Field("user_id") String userId,
                                                @Field("cate_id") String categoryId,
                                                @Field("sheet_type") String referType);

    /**
     * 接单列表(小芸)
     *
     * @param userId  用户id （必传）
     * @param referId 抢单id （必传）
     * @return
     */
    @FormUrlEncoded
    @POST("sheetChoseList")
    Observable<ResponseBean<ReferChoseBean>> sheetChoseList(@Field("user_id") String userId,
                                                            @Field("sheet_id") String referId);

    /**
     * 手动取消咨询(小芸)
     * @param userId        用户id （必传）
     * @param referId       咨询抢单id （必传）
     * @return
     */
    @FormUrlEncoded
    @POST("sheetClear")
    Observable<ResponseBean> sheetCancel(@Field("user_id") String userId,
                                         @Field("sheet_id") String referId);

    /**
     *
     * @param token
     * @param userId            用户id （必传）
     * @param targetUserId      选择用户id（必传）
     * @param targetUserType    用户类型 （必传）
     * @param sheetId           抢单id （必传）
     * @param sheetType         咨询类型 （必传）（1-私享咨询 2-在线面诊）
     * @return
     */
    @FormUrlEncoded
    @POST("chooseSheetUsers")
    Observable<ResponseBean> chooseSheetUsers(@Field("token")String token,
                                              @Field("user_id")String userId,
                                              @Field("click_id")String targetUserId,
                                              @Field("user_type")String targetUserType,
                                              @Field("sheet_id")String sheetId,
                                              @Field("sheet_type")String sheetType);

}

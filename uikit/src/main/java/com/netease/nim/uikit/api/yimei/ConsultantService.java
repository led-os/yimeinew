package com.netease.nim.uikit.api.yimei;

import com.handong.framework.base.ResponseBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ConsultantService {

    //  获取在线咨询师
    @GET("getConsultant")
    Observable<ResponseBean<ConsultantBean>> getConsultant(@Query("user_id") String userId,
                                                           @Query("query_id") String queryId,
                                                           @Query("get_consultants_type") String getConsultantsType);

    //  标记咨询师不在线
    @GET("markConsultant")
    Observable<ResponseBean> markConsultant(@Query("user_id") String userId,
                                            @Query("query_id") String queryId,
                                            @Query("get_consultants_type") String getConsultantsType,
                                            @Query("consultant_id") String consultantId);
}

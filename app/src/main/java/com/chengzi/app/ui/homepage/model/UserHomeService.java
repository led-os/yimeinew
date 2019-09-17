package com.chengzi.app.ui.homepage.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.UserHeaderInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserHomeService {


    /**
     * 用户头部信息3.17(阿布）
     */
    @GET("userInfo")
    Observable<ResponseBean<UserHeaderInfoBean>> userInfo(@QueryMap Map<String, String> params);


    //User__ - 用户主页头部--更换自己的封面
    @FormUrlEncoded
    @POST("changeCover")
    Observable<ResponseBean> changeCover(@Field("token") String token,
                                         @Field("user_id") String user_id,
                                         @Field("cover_image") String cover_image);

    //Consultant - 关注/取消关注（冀）
    @GET("findFollow")
    Observable<ResponseBean> findFollow(@Query("token") String token,
                                        @Query("user_id") String user_id,
                                        @Query("cover_id") String cover_id);   //对象id

}

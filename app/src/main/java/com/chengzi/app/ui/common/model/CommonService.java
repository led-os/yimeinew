package com.chengzi.app.ui.common.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.bean.ProvinceBean;
import com.chengzi.app.ui.home.bean.CategoryItem;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CommonService {

    /**
     * 上传图片，（支持多图）
     *
     * @param body
     */
    @Multipart
    @POST("upload")
    Observable<ResponseBean<List<String>>> upload(@Part List<MultipartBody.Part> body);

    @GET("cityListChild")
    Observable<ResponseBean<List<ProvinceBean>>> cityList();


    /**
     * 首页App导航 VIP专区分类、普通分类(小芸)
     */
    @FormUrlEncoded
    @POST("hospitalBegoodat")
    Observable<ResponseBean<List<CategoryItem>>> hospitalBegoodat(@Field("uid") String uid);
}

package com.chengzi.app.third.pay.model;


import com.handong.framework.base.ResponseBean;
import com.chengzi.app.third.pay.bean.WXEntity;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Desc:
 * @ClassName:PayDataService
 * @PackageName:com.qihuang.app.common.pay.model
 * @Create On 2019/2/27 0027
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public interface PayDataService {

    /**
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("wxapp/pay")
    Observable<ResponseBean<WXEntity>> getPayInfo(@FieldMap Map<String, String> params);
}

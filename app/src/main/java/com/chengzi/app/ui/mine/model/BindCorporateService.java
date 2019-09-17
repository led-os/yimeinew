package com.chengzi.app.ui.mine.model;

import com.chengzi.app.ui.mine.bean.BankBean;
import com.handong.framework.base.ResponseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface BindCorporateService {

    @GET("banks")
    Observable<ResponseBean<List<BankBean>>> getBanks();

}

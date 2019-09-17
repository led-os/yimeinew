package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.ArrayList;

public class AddCaseViewModel extends BaseViewModel {
    private static final String TAG = AddCaseViewModel.class.getSimpleName();
    private MineHospitalService mineService;
    //添加案例/日记 图
    public final ArrayList<String> items = new ArrayList<>();

    public AddCaseViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    public final MutableLiveData<ResponseBean> caseCreateLive = new MutableLiveData<>();//案例管理  案例添加
    public final MutableLiveData<ResponseBean> caseNoteCreateLive = new MutableLiveData<>();//添加日记


    // 一级分类 二级分类 三级分类
    public ObservableField<String> cate_id1 = new ObservableField<>();
    public ObservableField<String> cate_id2 = new ObservableField<>();
    public ObservableField<String> cate_id3 = new ObservableField<>();

    /*添加案例*/
    public void caseCreate(String create_time, String contents) {
        mineService.caseCreate(AccountHelper.getUserId(), create_time,
                cate_id1.get(), cate_id2.get(), cate_id3.get(), contents, items)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseCreateLive.postValue(responseBean);
                    }
                });
    }

    /*添加日记*/
    public void caseNoteCreate(String cases_id, String contents, String create_time) {
        mineService.caseNoteCreate(AccountHelper.getUserId(), cases_id, contents, items, create_time)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseNoteCreateLive.postValue(responseBean);
                    }
                });
    }
}

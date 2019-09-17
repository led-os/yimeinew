package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.bean.UserRecordListBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.util.ArrayList;
import java.util.List;

public class ProfessionExperienceViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<UserRecordListBean>> userRecordListLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<String>> userRecordAddLiveData = new MutableLiveData<>();

    private String doctorId;
    private boolean self;    //  true 从个人中心查看自己的主页，false 查看别人的主页
    private int userType;   //  2 医生   3 咨询师

    private final DoctorHomeService doctorHomeService;

    public ProfessionExperienceViewModel() {
        doctorHomeService = Api.getApiService(DoctorHomeService.class);
    }

    //添加履历图
    public final ArrayList<String> items = new ArrayList<>();

    @Override //履历列表
    public void onRequest(int currentPage, int pageSize) {
        doctorHomeService.userRecordList(AccountHelper.getToken(), doctorId,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<UserRecordListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<UserRecordListBean>> userRecordListBean) {
                        userRecordListLiveData.postValue(userRecordListBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        userRecordListLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    //添加履历
    public void userRecordAdd(String start_time, String end_time, String contnet) {
        doctorHomeService.userRecordAdd(AccountHelper.getToken(), doctorId,
                start_time, end_time, contnet, items)
                .subscribe(new SimpleObserver<ResponseBean<String>>() {
                    @Override
                    public void onSuccess(ResponseBean<String> userRecordListBean) {
                        userRecordAddLiveData.postValue(userRecordListBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        userRecordAddLiveData.postValue(null);
                    }
                });
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
package com.chengzi.app.ui.onlinequestionandanswer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.model.OnlineQAService;

import java.util.List;

public class OnlineQuestionViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<QAbean.QAbeanWraper> liveData = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<DoctorBean>> doctorLive = new MutableLiveData<>();
    public final MutableLiveData<List<DoctorBean>> counselorLive = new MutableLiveData<>();

    private final OnlineQAService onlineQAService;
    private final CommonModel model = new CommonModel();

    public OnlineQuestionViewModel() {
        onlineQAService = Api.getApiService(OnlineQAService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        onlineQAService.questionList()
                .subscribe(new SimpleObserver<ResponseBean<QAbean.QAbeanWraper>>() {
                    @Override
                    public void onSuccess(ResponseBean<QAbean.QAbeanWraper> qAbeanWraperResponseBean) {
                        liveData.postValue(qAbeanWraperResponseBean.getData());
                    }
                });
    }

    public void categoryList() {
        model.categoryList1().subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
            @Override
            public void onSuccess(ResponseBean<List<CategoryItem>> navigatorListBeanResponseBean) {
//                List<CategoryItem> put = navigatorListBeanResponseBean.getData().getPut();
                List<CategoryItem> put = navigatorListBeanResponseBean.getData();
                categoryLive.postValue(put);
            }
        });
    }

    public void questionPeo(String userType, String categoryId) {
        onlineQAService.questionPeo(userType, categoryId)
                .subscribe(new SimpleObserver<ResponseBean<List<DoctorBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<DoctorBean>> listResponseBean) {
                        if (TextUtils.equals(userType, "2")) {
                            doctorLive.postValue(listResponseBean.getData());
                        } else if (TextUtils.equals(userType, "3")) {
                            counselorLive.postValue(listResponseBean.getData());
                        }
                    }
                });
    }
}

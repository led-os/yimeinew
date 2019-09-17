package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.homepage.bean.CasesCategoryByHospitalBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;

import java.util.HashMap;
import java.util.List;

public class HospitalCaseListViewModel extends CaseBaseViewModel implements IRequest {

    public final MutableLiveData<List<CasesCategoryByHospitalBean>> casesCategoryByHospitalLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<CaseBean>> casesLiveData = new MutableLiveData<>();

    private final HospitalHomeService hospitalHomeService;

    public HospitalCaseListViewModel() {
        hospitalHomeService = Api.getApiService(HospitalHomeService.class);
    }

    //被查看的医院id 和分类id
    public ObservableField<String> hospital_id = new ObservableField<>();
    public ObservableField<String> cate_id = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String token = AccountHelper.getToken();
        String user_id = AccountHelper.getUserId();
        String hospital_ids = hospital_id.get();
        if (TextUtils.isEmpty(hospital_ids)) {
            hospital_ids = "0";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", AccountHelper.getUserId());
        map.put("user_id", AccountHelper.getUserId());
        map.put("page", String.valueOf(currentPage));
        map.put("limit", String.valueOf(pageSize));
        map.put("clk_id", hospital_ids);
        if (!TextUtils.isEmpty(cate_id.get()) && !cate_id.get().equals("-1"))  //不传是全部
            map.put("cte_id", cate_id.get());
        hospitalHomeService.hospitalCaseList(map)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseBean>> pageBeanResponseBean) {
                        casesLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        casesLiveData.postValue(null);
                    }
                });
    }

    /**
     * @param //hospital_id 查看的医院id
     */
    public void casesCategoryByHospital() {
        String hospital_ids = hospital_id.get();
        if (TextUtils.isEmpty(hospital_ids)) {
            ToastUtils.showShort("被查看的医院有误!");
            return;
        }
        hospitalHomeService.casesCategoryByHospital(hospital_ids)
                .subscribe(new SimpleObserver<ResponseBean<List<CasesCategoryByHospitalBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CasesCategoryByHospitalBean>> listResponseBean) {
                        casesCategoryByHospitalLiveData.postValue(listResponseBean.getData());
                    }
                });
    }
}
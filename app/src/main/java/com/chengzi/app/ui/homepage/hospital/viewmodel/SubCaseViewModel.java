package com.chengzi.app.ui.homepage.hospital.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.homepage.model.HospitalHomeService;

import java.util.HashMap;

public class SubCaseViewModel extends CaseBaseViewModel {

    public final MutableLiveData<PageBean<CaseBean>> casesLive = new MutableLiveData<>();

    private String hospitalId;
    private boolean isSelf;

    private final HospitalHomeService hospitalHomeService;

    public SubCaseViewModel() {
        hospitalHomeService = Api.getApiService(HospitalHomeService.class);
    }

    public void getHospitalCases() {
        String token = AccountHelper.getToken();
        String user_id = AccountHelper.getUserId();
        String hospital_ids = hospitalId;
        if (TextUtils.isEmpty(hospital_ids)) {
            hospital_ids = "0";
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", user_id);
        map.put("user_id", user_id);
        map.put("page", "1");
        map.put("limit", "3");
        map.put("clk_id", hospital_ids);
//        if (!TextUtils.isEmpty(cate_id.get()) && !cate_id.get().equals("-1"))  //不传是全部
//            map.put("cte_id", cate_id.get());
//        hospitalHomeService.hospitalCaseList(token, hospitalId, userId, "1", "3")
        hospitalHomeService.hospitalCaseList(map)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseBean>> pageBeanResponseBean) {
                        casesLive.postValue(pageBeanResponseBean.getData());
                    }
                });
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}

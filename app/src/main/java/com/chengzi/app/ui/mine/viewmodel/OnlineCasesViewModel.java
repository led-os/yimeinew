package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.mine.bean.CaseInfoBean;
import com.chengzi.app.ui.mine.model.MineService;
import com.chengzi.app.widget.NineGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlineCasesViewModel extends BaseViewModel {
    //图片
    public final List<NineGridView.NineItem> items = new ArrayList<>();

    //获取线上病例详情
    public final MutableLiveData<CaseInfoBean> caseInfoLiveData = new MutableLiveData<>();
    //增加、编辑线上病例 addCase
    public final MutableLiveData<ResponseBean> addCaseLiveData = new MutableLiveData<>();

    private String targetUserId;
    private CaseInfoBean caseInfoBean;
    private boolean forwardCase;
    private boolean forView;    //  转发后，医生查看

    //获取线上病例详情
    public void caseInfo() {
        Api.getApiService(MineService.class).caseInfo(AccountHelper.getToken(),
                AccountHelper.getUserId(), targetUserId)
                .subscribe(new SimpleObserver<ResponseBean<CaseInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CaseInfoBean> bean) {
                        caseInfoLiveData.postValue(bean.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        caseInfoLiveData.postValue(null);
                    }
                });
    }

    /**
     * 增加、编辑线上病例
     * <p>
     * token	Number	 用户token （必须）
     * uid	Number	 用户id （必须）
     * user_name	String	 姓名 （必须）
     * user_sex	Number	 性别 （必须） 0女 1男
     * user_codes	String	身份证号码 （必须）
     * phone	String	 联系电话 （必须）
     * user_birthday	String	 用户生日
     * city_id	Number	 地址
     * height	String	身高
     * weight	String	体重
     * blood_type	Number	 血型 1-A 2-B 3-AB 4-O 5-其他
     * heart_rate	Number	心率
     * blood_pressure_low	Number	 血压/低
     * blood_pressure_high	Number	 血压/高
     * operation	String	 以往手术经历
     * trauma	String	 外伤经历
     * beauty	String	 变美经历
     * allergy_drugs	String	 过敏药物
     * sidease	Number	慢性病
     * smoke	Number	 吸烟频率 1、不吸烟 2、3支/周 3、1-4支/天 4、一天超过5支
     * drink_wine	Number	 饮酒频率 1-每年小于3/4次 2-每周小于3次 3-每天
     * exercise	Number	 锻炼频率 1-不锻炼 2-1次/周 3- 3次/周 4-大于3次/周
     * course	String	变美历程
     * image	Array	图片数组
     */
    //姓名 性别  身份证号码 联系电话 用户生日 地址 身高 体重
    public final ObservableField<String> user_name = new ObservableField<>();
    public final ObservableField<String> user_sex = new ObservableField<>();
    public final ObservableField<String> user_codes = new ObservableField<>();
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableField<String> user_birthday = new ObservableField<>(); //不必传
    public final ObservableField<String> address = new ObservableField<>(); //不必传
    public final ObservableField<String> height = new ObservableField<>(); //不必传
    public final ObservableField<String> weight = new ObservableField<>(); //不必传
    //血型 心率   血压/低  血压/高 以往手术经历
    public final ObservableField<String> blood_type = new ObservableField<>(); //不必传
    public final ObservableField<String> heart_rate = new ObservableField<>(); //不必传
    public final ObservableField<String> blood_pressure_low = new ObservableField<>(); //不必传
    public final ObservableField<String> blood_pressure_high = new ObservableField<>(); //不必传
    public final ObservableField<String> operation = new ObservableField<>(); //不必传
    // 外伤经历 变美经历 过敏药物 慢性病
    public final ObservableField<String> trauma = new ObservableField<>(); //不必传
    public final ObservableField<String> beauty = new ObservableField<>(); //不必传
    public final ObservableField<String> allergy_drugs = new ObservableField<>(); //不必传
    public final ObservableField<String> sidease = new ObservableField<>(); //不必传
    //家族遗传病 吸烟频率 饮酒频率 锻炼频率
    public final ObservableField<String> family_diseases = new ObservableField<>(); //不必传TODO:
    public final ObservableField<String> smoke = new ObservableField<>(); //不必传
    public final ObservableField<String> drink_wine = new ObservableField<>(); //不必传
    public final ObservableField<String> exercise = new ObservableField<>(); //不必传

    //Field map contained null value for key 'phone'
    public void addCase(List<String> urls) {
        HashMap<String, String> maps = new HashMap();
        //必传数据
        maps.put("token", AccountHelper.getToken());
        maps.put("user_id", AccountHelper.getUserId());
        maps.put("user_name", user_name.get());
        maps.put("user_sex", user_sex.get());
        maps.put("user_codes", user_codes.get());
        maps.put("phone", phone.get());
        //////
        if (!TextUtils.isEmpty(user_birthday.get()))
            maps.put("user_birthday", user_birthday.get());
        if (!TextUtils.isEmpty(address.get()))
            maps.put("address", address.get());
        if (!TextUtils.isEmpty(height.get()))
            maps.put("height", height.get());
        if (!TextUtils.isEmpty(weight.get()))
            maps.put("weight", weight.get());
        if (!TextUtils.isEmpty(blood_type.get()))
            maps.put("blood_type", blood_type.get());
        if (!TextUtils.isEmpty(heart_rate.get()))
            maps.put("heart_rate", heart_rate.get());

        if (!TextUtils.isEmpty(blood_pressure_low.get()))
            maps.put("blood_pressure_low", blood_pressure_low.get());
        if (!TextUtils.isEmpty(blood_pressure_high.get()))
            maps.put("blood_pressure_high", blood_pressure_high.get());
        if (!TextUtils.isEmpty(operation.get()))
            maps.put("operation", operation.get());
        if (!TextUtils.isEmpty(trauma.get()))
            maps.put("trauma", trauma.get());
        if (!TextUtils.isEmpty(beauty.get()))
            maps.put("beauty", beauty.get());
        if (!TextUtils.isEmpty(allergy_drugs.get()))
            maps.put("allergy_drugs", allergy_drugs.get());
        if (!TextUtils.isEmpty(sidease.get()))
            maps.put("sidease", sidease.get());

        if (!TextUtils.isEmpty(family_diseases.get()))
            maps.put("family_diseases", family_diseases.get());
        if (!TextUtils.isEmpty(smoke.get()))
            maps.put("smoke", smoke.get());
        if (!TextUtils.isEmpty(drink_wine.get()))
            maps.put("drink_wine", drink_wine.get());
        if (!TextUtils.isEmpty(exercise.get()))
            maps.put("exercise", exercise.get());
        if (urls != null) {
            Api.getApiService(MineService.class).addCase(maps, urls)
                    .subscribe(new SimpleObserver<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean bean) {
                            addCaseLiveData.postValue(bean);
                        }
                    });
        } else {
            Api.getApiService(MineService.class).addCase(maps)
                    .subscribe(new SimpleObserver<ResponseBean>() {
                        @Override
                        public void onSuccess(ResponseBean bean) {
                            addCaseLiveData.postValue(bean);
                        }
                    });
        }
    }

    public void postForm() {
        /*if (!items.isEmpty()) {
            uploadPic();
        } else {
            addCase(new ArrayList<>());
        }*/
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            strings.add(items.get(i).getLocalPath());
        }
        addCase(strings);
    }

    private void uploadPic() {
        List<String> paths = new ArrayList<>();
        for (NineGridView.NineItem item : items) {
            paths.add(item.getLocalPath());
        }
        AliUpload.multipleUpload(paths, new AliUpload.MultipleCallBack() {
            @Override
            public ArrayList<String> onSuccess(List<String> paths) {
                addCase(paths);
                return null;
            }

            @Override
            public ArrayList<String> onError(List<AliError> errors) {
                return null;
            }
        });
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public CaseInfoBean getCaseInfoBean() {
        return caseInfoBean;
    }

    public void setCaseInfoBean(CaseInfoBean caseInfoBean) {
        this.caseInfoBean = caseInfoBean;
    }

    public boolean isForwardCase() {
        return forwardCase;
    }

    public void setForwardCase(boolean forwardCase) {
        this.forwardCase = forwardCase;
    }

    public boolean isForView() {
        return forView;
    }

    public void setForView(boolean forView) {
        this.forView = forView;
    }
}
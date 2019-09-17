package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.util.Pair;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.SheetListBean;
import com.chengzi.app.ui.mine.bean.SheetStatusBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;

import java.util.List;

public class RobOrderViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<SheetListBean>> sheetListLiveData = new MutableLiveData<>();
    public final MutableLiveData<Pair<SheetListBean,String>> sheetJoinLiveData = new MutableLiveData<>();
    public final MutableLiveData<SheetStatusBean> sheetStatusLiveData = new MutableLiveData<>();

    private int sheetType;      //   咨询类型 （必传）（1-私享咨询 2-在线面诊）
    private String sheetId;      //sheet_id抢单id
    //sheetListBean 显示客户信息
    private SheetListBean sheetListBean;
    public final ObservableBoolean requestCallPermissionDoc = new ObservableBoolean();
    private SheetListBean bean;
    private String sheetJoinId; //  参与抢单id

    private final MineDoctorOrCounselorService mineService;

    public RobOrderViewModel() {
        mineService = Api.getApiService(MineDoctorOrCounselorService.class);
    }

    /**
     * //咨询师/医生抢单操作
     *
     * @param sheet_type 咨询类型 （必传）（1-私享咨询 2-在线面诊）
     */
    public void sheetJoin(SheetListBean sheetBean, String sheet_type) {
        String sheetId = sheetBean.getId();
        mineService.sheetJoin(AccountHelper.getToken(), AccountHelper.getUserId(), String.valueOf(AccountHelper.getIdentity()),
                sheetId, sheet_type)
                .subscribe(new SimpleObserver<ResponseBean<String>>() {
                    @Override
                    public void onSuccess(ResponseBean<String> listResponseBean) {
                        sheetJoinLiveData.postValue(new Pair<>(sheetBean,listResponseBean.getData()));
                    }
                });
    }

    //咨询师/医生抢单操作
    public void sheetStatus(String sheet_id) {
        mineService.sheetStatus(AccountHelper.getToken(), AccountHelper.getUserId(), sheet_id)
                .subscribe(new SimpleObserver<ResponseBean<SheetStatusBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<SheetStatusBean> listResponseBean) {
                        sheetStatusLiveData.postValue(listResponseBean.getData());
                    }
                });
    }

    //私享咨询/在线面诊的列表
    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.sheetList(AccountHelper.getToken(), AccountHelper.getUserId(), sheetType + "")
                .subscribe(new SimpleObserver<ResponseBean<List<SheetListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<SheetListBean>> listResponseBean) {
                        sheetListLiveData.postValue(listResponseBean.getData());
                    }

                });
    }

    public void delSheetChoseList(){
        mineService.delSheetChoseList(sheetJoinId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }

    public int getSheetType() {
        return sheetType;
    }

    public void setSheetType(int sheetType) {
        this.sheetType = sheetType;
    }

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public SheetListBean getSheetListBean() {
        return sheetListBean;
    }

    public void setSheetListBean(SheetListBean sheetListBean) {
        this.sheetListBean = sheetListBean;
    }

    public SheetListBean getBean() {
        return bean;
    }

    public void setBean(SheetListBean bean) {
        this.bean = bean;
    }

    public String getSheetJoinId() {
        return sheetJoinId;
    }

    public void setSheetJoinId(String sheetJoinId) {
        this.sheetJoinId = sheetJoinId;
    }
}

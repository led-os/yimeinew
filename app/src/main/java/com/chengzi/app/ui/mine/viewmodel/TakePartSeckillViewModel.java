package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.mine.bean.KillHospitalListBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.List;

/**
 * 参与秒杀 fragment
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class TakePartSeckillViewModel extends BaseViewModel implements IRequest {
    //秒杀分类 秒杀列表
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<KillHospitalListBean>> killHospitalListLiveData = new MutableLiveData<>();
    //参与秒杀 取消秒杀
    public final MutableLiveData<ResponseBean> killAddLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> killCancelLiveData = new MutableLiveData<>();

    //分类id
    public ObservableField<String> category_id = new ObservableField<>();
    //确认参与秒杀
    //商品id 时间 秒杀价格 秒杀数量
    public ObservableField<String> goods_id = new ObservableField<>();
    public ObservableField<String> time = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> num = new ObservableField<>();

    private MineHospitalService mineService;

    public TakePartSeckillViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();


    private final CommonModel commonModel = new CommonModel();

    public void categoryList() {
        commonModel.categoryList1()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> navigatorListBeanResponseBean) {
                        categoryLive.postValue(navigatorListBeanResponseBean.getData());
                    }
                });
    }


    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.killHospitalList(AccountHelper.getToken(), AccountHelper.getUserId(),
                category_id.get(), String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<KillHospitalListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<KillHospitalListBean>> bean) {
                        killHospitalListLiveData.postValue(bean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        killHospitalListLiveData.postValue(null);
                    }
                });
    }

    //参与秒杀
    public void killAdd(String cate_id) {
        String times = time.get();
        if (time.get().equals("00:00")) {
            times = "24:00";
        }
        mineService.killAdd(AccountHelper.getToken(), AccountHelper.getUserId(), cate_id, goods_id.get(), times, price.get(), num.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        killAddLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        killAddLiveData.postValue(null);
                    }
                });
    }

    //取消秒杀
    public void killCancel(String goods_id) {
        mineService.killCancel(AccountHelper.getToken(), AccountHelper.getUserId(), goods_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        killCancelLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        killCancelLiveData.postValue(null);
                    }
                });
    }
}
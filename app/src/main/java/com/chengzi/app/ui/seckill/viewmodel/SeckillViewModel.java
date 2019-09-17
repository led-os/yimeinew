package com.chengzi.app.ui.seckill.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.seckill.bean.KillGoodsListBean;
import com.chengzi.app.ui.seckill.bean.KillListBean;
import com.chengzi.app.ui.seckill.bean.KillTimeBean;
import com.chengzi.app.ui.seckill.model.SeckillService;

import java.util.List;

public class SeckillViewModel extends BaseViewModel implements IRequest {
    //分类 秒杀时间段 (距离下个时段倒计时) 列表+倒计时
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<KillTimeBean>> killTimeLive = new MutableLiveData<>();
    //    public final MutableLiveData<ResponseBean<CountDownBean>> countDownLive = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<KillListBean>> killIndexLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> killRemindLiveData = new MutableLiveData<>();

    //首页秒杀的商品
    public final MutableLiveData<KillGoodsListBean> killGoodsListLiveData = new MutableLiveData<>();


    private SeckillService seckillService;

    public SeckillViewModel() {
        seckillService = Api.getApiService(SeckillService.class);
    }

    //分类id 和 times
    public ObservableField<String> category_id = new ObservableField<>();
    public ObservableField<String> times = new ObservableField<>();
    //1-抢购中 0-即将开始
    public ObservableField<String> status = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        seckillService.killIndex(AccountHelper.getToken(), AccountHelper.getUserId(), times.get(), category_id.get(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<KillListBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<KillListBean> killIndexBean) {
                        killIndexLiveData.postValue(killIndexBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        killIndexLiveData.postValue(null);
                    }
                });
    }

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

    public void killTime() {
        seckillService.killTime()
                .subscribe(new SimpleObserver<ResponseBean<List<KillTimeBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<KillTimeBean>> killTimeBean) {
                        killTimeLive.postValue(killTimeBean.getData());
                    }
                });
    }

    public void killRemind(String plan_order_id) {
        seckillService.killRemind(AccountHelper.getToken(), AccountHelper.getUserId(), plan_order_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        killRemindLiveData.postValue(bean);
                    }
                });
    }

    public void killGoodsList() {
        seckillService.killGoodsList()
                .subscribe(new SimpleObserver<ResponseBean<KillGoodsListBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<KillGoodsListBean> bean) {
                        killGoodsListLiveData.postValue(bean.getData());
                    }
                });
    }

//    public void countDown() {
//        seckillService.countDown()
//                .subscribe(new SimpleObserver<ResponseBean<CountDownBean>>() {
//                    @Override
//                    public void onSuccess(ResponseBean<CountDownBean> countDownBean) {
//                        countDownLive.postValue(countDownBean);
//                    }
//                });
//    }
}

package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.CartGetPriceBean;
import com.chengzi.app.ui.mine.bean.ShopCarBean;
import com.chengzi.app.ui.mine.model.ShopCartAndOrderService;
import com.chengzi.app.utils.AlwaysAliveObservable;
import com.chengzi.app.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 *
 * @ClassName:AccountDetailsViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class MyShopCarViewModel extends BaseViewModel implements IRequest {
    //购物车列表 修改购物车中商品数量 获取选中的总价和优惠券价格 删除
    public final MutableLiveData<List<ShopCarBean>> cartGoodsListLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> updateCartNumLiveData = new MutableLiveData<>();
    public final MutableLiveData<CartGetPriceBean> cartGetPriceLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> cartDeleteLiveData = new MutableLiveData<>();
    //购物车下单
    public final MutableLiveData<List<String>> cartAddOrderLiveData = new MutableLiveData<>();

    public final AlwaysAliveObservable aliveObservable = new AlwaysAliveObservable();

    private final ShopCartAndOrderService shopCarService;

    //0编辑结算 1完成删除
    private int type = 0;

    //是否全选
    private boolean chooseAll = false;

    public final List<String> selectedIds = new ArrayList<>();

    public MyShopCarViewModel() {
        shopCarService = Api.getApiService(ShopCartAndOrderService.class);
    }

    //购物车列表
    public void onRequest(int currentPage, int pageSize) {
        shopCarService.cartGoodsList(AccountHelper.getToken(), AccountHelper.getUserId(),
                Integer.MAX_VALUE, currentPage)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ShopCarBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ShopCarBean>> pageBeanResponseBean) {
                        cartGoodsListLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    //修改购物车中商品数量
    public void updateCartNum(ShopCarBean item, String num) {
        shopCarService.cartUpdateNum(AccountHelper.getToken(), AccountHelper.getUserId(), item.getCart_info_id() + "", num)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean listResponseBean) {
                        item.setGoods_num(num);
                        updateCartNumLiveData.postValue(num);
                    }
                });
    }

    //获取选中的总价和优惠券价格
    public void cartGetPrice(List<String> card_info_id) {
        shopCarService.cartGetPrice(AccountHelper.getToken(), AccountHelper.getUserId(), card_info_id)
                .subscribe(new SimpleObserver<ResponseBean<CartGetPriceBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CartGetPriceBean> listResponseBean) {
                        if (listResponseBean != null && listResponseBean.getData() != null) {
                            cartGetPriceLiveData.postValue(listResponseBean.getData());
                        }
                    }
                });
    }

    //删除
    public void cartDelete(List<String> card_info_id) {
        shopCarService.cartDelete(AccountHelper.getToken(), AccountHelper.getUserId(), card_info_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean listResponseBean) {
                        cartDeleteLiveData.postValue(listResponseBean);
                    }
                });
    }

    //购物车下单
    public void cartAddOrder(List<String> card_info_id) {
        shopCarService.cartAddOrder(AccountHelper.getToken(), AccountHelper.getUserId(), card_info_id
                , PreferenceManager.getPreference(PreferenceManager.LONGITUDE)  //精度
                , PreferenceManager.getPreference(PreferenceManager.LATITUDE))  //纬度
                .subscribe(new SimpleObserver<ResponseBean<List<String>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<String>> listResponseBean) {
                        cartAddOrderLiveData.postValue(listResponseBean.getData());
                    }
                });
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChooseAll() {
        return chooseAll;
    }

    public void setChooseAll(boolean chooseAll) {
        this.chooseAll = chooseAll;
    }
}
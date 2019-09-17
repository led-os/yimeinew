package com.chengzi.app.ui.goods.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;
import com.chengzi.app.ui.goods.model.GoodService;

public class SubSpellViewModel extends BaseViewModel {

    public final MutableLiveData<PageBean<SpellBean>> spellLive = new MutableLiveData<>();

    private GoodDetailBean goodDetailBean;
    private String goodsId;

    private boolean isSpellEmpty = true;   //  true 拼单列表为空
    private boolean isKilling = true;      //  true 正在秒杀中

    private final GoodService goodService;

    public SubSpellViewModel() {
        goodService = Api.getApiService(GoodService.class);
    }

    public void goodsSpellList() {
        goodService.goodsSpellList(goodsId, "2", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<SpellBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<SpellBean>> responseBean) {
                        spellLive.postValue(responseBean.getData());
                    }
                });
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public GoodDetailBean getGoodDetailBean() {
        return goodDetailBean;
    }

    public void setGoodDetailBean(GoodDetailBean goodDetailBean) {
        this.goodDetailBean = goodDetailBean;
    }

    public boolean isSpellEmpty() {
        return isSpellEmpty;
    }

    public void setSpellEmpty(boolean spellEmpty) {
        isSpellEmpty = spellEmpty;
    }

    public boolean isKilling() {
        return isKilling;
    }

    public void setKilling(boolean killing) {
        isKilling = killing;
    }
}

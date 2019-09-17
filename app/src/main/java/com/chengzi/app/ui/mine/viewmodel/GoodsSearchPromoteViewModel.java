package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.mine.bean.ProductPromotionProductBean;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.List;

/**
 * 推广
 *
 * @ClassName:GoodsSearchPromoteViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/6/1 0001   09:18
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/6/1 0001 handongkeji All rights reserved.
 */

public class GoodsSearchPromoteViewModel extends BaseViewModel implements IRequest {

    private MineHospitalService mineService;

    public GoodsSearchPromoteViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    // 我的推广--商品分类列表(syx)
    public final MutableLiveData<ResponseBean<List<PromotionSelectCategoryBean>>> promotionSelectCategoryLiveData = new MutableLiveData<>();
    // Promotion - 我的推广--商品搜索推广-我的商品列表页(syx)
    public final MutableLiveData<List<ProductPromotionProductBean>> productPromotionProductLiveData = new MutableLiveData<>();
    // Promotion - 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
    public final MutableLiveData<ResponseBean> setSearchPromotionAmountLiveData = new MutableLiveData<>();
    // 我的推广-添加搜索推广(包括商品、医院等所有的搜索推广)
    public final MutableLiveData<ResponseBean> addSearchPromotionItemLiveData = new MutableLiveData<>();
    // 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
    public final MutableLiveData<ResponseBean> removeSearchPromotionItemLiveData = new MutableLiveData<>();

    public final MutableLiveData<ResponseBean<List<CommentBean>>> caseCommentListLive = new MutableLiveData<>();//评论列表
    //分类id
    public String cate_id;
    //选中的分类id
    public String pid;

    /**
     * 我的推广--商品分类列表(syx)
     *
     * @param zone_type //专区类型 （1.普通;2.VIP）
     */
    public void promotionSelectCategory(String zone_type) {
        String userId = AccountHelper.getUserId();
        mineService.promotionSelectCategory(AccountHelper.getToken(), userId, userId, zone_type)
                .subscribe(new SimpleObserver<ResponseBean<List<PromotionSelectCategoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<PromotionSelectCategoryBean>> responseBean) {
                        promotionSelectCategoryLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        promotionSelectCategoryLiveData.postValue(null);
                    }
                });
    }

    //商品站内推广搜索
    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = AccountHelper.getUserId();
        mineService.productPromotionProduct(AccountHelper.getToken(), userId, userId, cate_id,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ProductPromotionProductBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ProductPromotionProductBean>> listResponseBean) {
                        productPromotionProductLiveData.postValue(listResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        productPromotionProductLiveData.postValue(null);
                    }
                });
    }

    /**
     * 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
     *
     * @param id     要设置的对象的id，比如医生ID、商品ID
     * @param type   类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param amount 设置的限额数
     */
    public void setSearchPromotionAmount(String id, String type, String amount) {
        String userId = AccountHelper.getUserId();
        mineService.setSearchPromotionAmount(AccountHelper.getToken(), userId, userId, id, type, amount)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        setSearchPromotionAmountLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        setSearchPromotionAmountLiveData.postValue(null);
                    }
                });
    }


    /**
     * 添加搜索推广(包括商品、医院等所有的搜索推广)
     *
     * @param id       【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type     【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     * @param //amount 设置的限额数，不填则默认为0，不限额
     */
    public void addSearchPromotionItem(String id, String type) {
        String userId = AccountHelper.getUserId();
        mineService.addSearchPromotionItem(AccountHelper.getToken(), userId, userId, id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addSearchPromotionItemLiveData.postValue(responseBean);
                    }
                });
    }

    /**
     * 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
     *
     * @param id   【必填】要设置的对象的id，比如医生ID、商品ID
     * @param type 【必填】类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
     */
    public void removeSearchPromotionItem(String id, String type) {
        String userId = AccountHelper.getUserId();
        mineService.removeSearchPromotionItem(AccountHelper.getToken(), userId, userId, id, type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        removeSearchPromotionItemLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        removeSearchPromotionItemLiveData.postValue(null);
                    }
                });
    }
}
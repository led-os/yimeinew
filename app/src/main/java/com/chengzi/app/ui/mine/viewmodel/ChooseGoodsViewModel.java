package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.PromotionSelectProductBean;
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

public class ChooseGoodsViewModel extends BaseViewModel implements IRequest {

    private MineHospitalService mineService;

    public ChooseGoodsViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    // Promotion - 我的推广--商品搜索推广-我的商品列表页(syx)
    public final MutableLiveData<List<PromotionSelectProductBean>> productPromotionProductLiveData = new MutableLiveData<>();

    //选中的分类id
    public String cate_id;
    //分类id
    public String pid;

    //专区推广中的选择商品
    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = AccountHelper.getUserId();
        mineService.promotionSelectProduct(AccountHelper.getToken(), userId, userId, cate_id, pid, "1", String.valueOf(Integer.MAX_VALUE))
                .subscribe(new SimpleObserver<ResponseBean<List<PromotionSelectProductBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<PromotionSelectProductBean>> listResponseBean) {
                        productPromotionProductLiveData.postValue(listResponseBean.getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        productPromotionProductLiveData.postValue(null);
                    }
                });
    }
}
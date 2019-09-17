package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 美丽档案编辑
 *
 * @ClassName:BeautifulPhotoViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/25 0025   17:32
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/25 0025 handongkeji All rights reserved.
 */

public class BeautifulPhotoViewModel extends BaseViewModel {

    public final ArrayList<String> items = new ArrayList<>();

    private final MineService mineService;

    public BeautifulPhotoViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    //
//    //上传图片
//    public final MutableLiveData<ResponseBean<List<String>>> uploadLiveData = new MutableLiveData<>();
    //美丽档案编辑
    public final MutableLiveData<ResponseBean> recordEditLiveData = new MutableLiveData<>();

    //美丽档案编辑
    public void recordEdit(String orderid, List<String> images) {
        mineService.recordEdit(AccountHelper.getToken(), AccountHelper.getUserId(), orderid, images)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        recordEditLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }
//
//    public void uploadImage(List<String> images) {
//        CommonModel model = new CommonModel();
//        model.upload(ImageIsUploadUtils.isNotUploadImage(images))
//                .subscribe(new SimpleObserver<ResponseBean<List<String>>>() {
//                    @Override
//                    public void onSuccess(ResponseBean<List<String>> listResponseBean) {
//                        uploadLiveData.postValue(listResponseBean);
//                    }
//                });
//    }

}

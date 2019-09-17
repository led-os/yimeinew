package com.chengzi.app.ui.common.model;

import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.handongkeji.utils.BitmapUtils;
import com.chengzi.app.ui.common.bean.ProvinceBean;
import com.chengzi.app.ui.home.bean.CategoryItem;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonModel {

    private final CommonService commonService;

    public CommonModel() {
        commonService = Api.getApiService(CommonService.class);
    }

    public Observable<ResponseBean<List<String>>> upload(List<String> localPaths) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String localPath : localPaths) {
            File file = new File(localPath);
            String mediaType = URLConnection.guessContentTypeFromName(localPath);

            byte[] bytes = BitmapUtils.compress(localPath, 0);
            RequestBody body = RequestBody.create(MediaType.parse(mediaType), bytes);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file[]", file.getName(), body);
            parts.add(part);
        }
        return commonService.upload(parts);
    }

    public Observable<ResponseBean<List<ProvinceBean>>> getCityList() {
        return commonService.cityList();
    }

    public Observable<ResponseBean<List<CategoryItem>>> categoryList1() {
        return commonService.hospitalBegoodat("0");
    }

}

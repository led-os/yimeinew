package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.third.fileuplod.AliError;
import com.chengzi.app.third.fileuplod.AliUpload;
import com.chengzi.app.ui.discover.model.DiscoverService;
import com.chengzi.app.utils.URLUtils;
import com.chengzi.app.widget.NineGridView;

import java.util.ArrayList;
import java.util.List;

////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//         佛祖保佑       永无BUG     永不修改                  //
////////////////////////////////////////////////////////////////////

public class PostViewModel extends BaseViewModel {

    public final MutableLiveData<Boolean> liveData = new MutableLiveData<>();

    public final ObservableField<String> content = new ObservableField<>();

    public final List<NineGridView.NineItem> items = new ArrayList<>();

    private String contentJson;
    private List<String> atUserIds;

    private final DiscoverService discoverService;

    public PostViewModel() {
        discoverService = Api.getApiService(DiscoverService.class);
    }

    public void postForm() {

        if (!items.isEmpty()) {

            uploadPic();

        } else {
            realPostForum(null);
        }

    }

    private void realPostForum(List<String> urls) {

        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("content", URLUtils.encode(contentJson));

        if (!items.isEmpty() && !urls.isEmpty()) {
            int type = items.get(0).getType();
            params.put("type", String.valueOf(type));    //  当有图片或视频资源的时候 1 图片 2 视频

            if (type == 1) {
                StringBuilder sb = new StringBuilder();
                for (String url : urls) {
                    sb.append(url).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                params.put("pics", sb.toString());
            } else if (type == 2) {
                params.put("videoUrl", urls.get(0));
            }
        }

        discoverService.postForum(params.params(), atUserIds)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        liveData.postValue(true);
                    }
                });

    }

    private void uploadPic() {
        List<String> paths = new ArrayList<>();
        for (NineGridView.NineItem item : items) {
            paths.add(item.getLocalPath());
        }
        AliUpload.multipleUpload(paths, new AliUpload.MultipleCallBack() {
            @Override
            public ArrayList<String> onSuccess(List<String> paths) {
                realPostForum(paths);
                return null;
            }

            @Override
            public ArrayList<String> onError(List<AliError> errors) {
                return null;
            }
        });
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public List<String> getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(List<String> atUserIds) {
        this.atUserIds = atUserIds;
    }
}

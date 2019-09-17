package com.chengzi.app.third;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 *
 * @ClassName:ThirdLoginUtil

 * @PackageName:com.yueertang.app.third

 * @Create On 2018/1/17 0017   19:04

 * @Site:http://www.handongkeji.com

 * @author:gjun

 * @Copyrights 2018/1/17 0017 handongkeji All rights reserved.
 */


public class ThirdLoginUtil {

    private UMShareAPI mShareAPI = null;

    private SHARE_MEDIA platform = null;

    public static final int WX = 1;                       //选中微信的类型
    public static final int QQ = 2;                       //选中QQ的类型
    public static final int SINA = 3;                       //选中微博的类型

    protected int mCurState;

    Context context;

    public ThirdLoginUtil (Context context){
        this.context = context;
        mShareAPI= UMShareAPI.get(context);
    }

    public void checkState(int state) {
        mCurState=state;
        switch (state){
            case WX:
                if (isInstall("com.tencent.mm")) {
                    platform = SHARE_MEDIA.WEIXIN;
                    mShareAPI.doOauthVerify((Activity) context, platform, umAuthListener);
                } else {
                    Toast.makeText(context, "您还没有安装微信", Toast.LENGTH_SHORT).show();
                }
                break;
            case QQ:
                if (isInstall("com.tencent.mobileqq")) {
                    platform = SHARE_MEDIA.QQ;
                    mShareAPI.doOauthVerify((Activity) context, platform, umAuthListener);
                } else {
                    Toast.makeText(context, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
                }
                break;
            case SINA:
                platform = SHARE_MEDIA.SINA;
                if (isInstall("com.sina.weibo")) {
                    mShareAPI.doOauthVerify((Activity) context, platform, umAuthListener);
                } else {
                    Toast.makeText((Activity) context, "您还没有安装微博", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }


    /**
     * 第三方
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.d("Tag",share_media.toString());
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            // 获取uid
            String userOpenToken, uid = null;
            if (null != data) {
                if (mCurState == QQ || mCurState == SINA) {  //qq
                    uid = data.get("uid");
                    userOpenToken = data.get("access_token");
                } else {
                    uid = data.get("openid");
                    userOpenToken = data.get("access_token");
                }
                if (listener!=null){
                    listener.resultThirdLog(mCurState, uid, userOpenToken);
                }
            } else {
                Toast.makeText(context, "get fail", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.d("Tag",t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.d("Tag",action+"");
        }
    };

    private ThirdLogin listener;

    public void setListener(ThirdLogin listener){
        this.listener=listener;
    }

    public interface ThirdLogin{
        void resultThirdLog(int mCurState, String uid, String userOpenToken);
    }

    /**
     * 判断用户手机是否装了第三方的App
     *
     * @param packgeName
     * @return
     */
    private boolean isInstall(String packgeName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packgeName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

}

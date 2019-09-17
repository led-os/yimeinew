package com.chengzi.app.third;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.chengzi.app.MyApp;
import com.chengzi.app.R;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class ShareUtil {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public ShareUtil(Activity activity) {
        this.mActivity = activity;
    }

    SHARE_MEDIA shareMedia = null;

    /**
     * 微信
     */
    public static final int WX = 0X1;

    public static final int SINA = 0X2;

    public static final int QQ = 0X3;

    public static final int FRIEND = 0X4;

    public static final int QZONE = 0X5;

    public static final int IMAGE = 0X11;

    public static final int VIDEO = 0X12;

    public static final int TEXT = 0X13;

    public static final int URL = 0X14;

    private Activity mActivity;

    /**
     * 分享类型是QQ还是微信
     *
     * @param type
     * @return
     */
    public boolean share(int type) {
        int permission = ActivityCompat.checkSelfPermission(MyApp.getInstance().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    mActivity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            return checkType(type);
        }
        return false;
    }

    /**
     * 是那种分享
     *
     * @param type
     */
    private boolean checkType(int type) {
        switch (type) {
            case WX:
                shareMedia = SHARE_MEDIA.WEIXIN;
                if (isInstall("com.tencent.mm")) {

                } else {
                    Toast.makeText(mActivity, "您还没有安装微信", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case FRIEND:
                shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                if (isInstall("com.tencent.mm")) {

                } else {
                    Toast.makeText(mActivity, "您还没有安装微信", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case SINA:
                shareMedia = SHARE_MEDIA.SINA;
                if (isInstall("com.sina.weibo")) {
                } else {
                    Toast.makeText(mActivity, "您还没有安装微博", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case QQ:
                shareMedia = SHARE_MEDIA.QQ;
                if (!isInstall("com.tencent.mobileqq")) {
                    Toast.makeText(mActivity, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;

            case QZONE:
                shareMedia = SHARE_MEDIA.QZONE;
                if (!isInstall("com.tencent.mobileqq")) {
                    Toast.makeText(mActivity, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            default:
        }
        return true;
    }

    /**
     * 分享类型 是图片还是url
     *
     * @param state
     * @param url
     * @param dec
     */
    public ShareUtil shareType(int state, String url, String title, String dec, String image) {
        checkShareType(state, url, title, dec, image);
        return this;
    }

    private void checkShareType(int state, String url, String title, String dec, String image) {
        switch (state) {
            case VIDEO:
                UMVideo video = new UMVideo(url);
                video.setThumb(new UMImage(mActivity,R.drawable.logo));
                video.setTitle(title);//"我在名师网上面发布了一段视频，快来观看吧"
//                video.setThumb(new UMImage(mActivity, image));//"我在名师网上面发布了一段视频，快来观看吧"
                if (!TextUtils.isEmpty(image)) {
                    video.setThumb(new UMImage(mActivity, image));
                }
                video.setDescription(dec);
                Log.i("fenxiang ", "checkShareType: " + video.toString());
                new ShareAction((Activity) mActivity)
                        .withText(dec)
                        .withMedia(video)
                        .setPlatform(shareMedia)
                        .setCallback(shareListener).share();
                break;
            case IMAGE:
                new ShareAction(mActivity)
                        .withMedia(new UMImage(mActivity, image))
                        .withText(dec)
                        .setPlatform(shareMedia)
                        .setCallback(shareListener).share();
                break;
            case URL:
                UMWeb web = new UMWeb(url);
                web.setTitle(title);
                if (!TextUtils.isEmpty(image)) {
                    web.setThumb(new UMImage(mActivity, image));
                } else {
                    web.setThumb(new UMImage(mActivity, R.drawable.logo));
                }
                //描述
                web.setDescription(dec);
                new ShareAction(mActivity)
                        .withText(dec)
                        .withMedia(web)
                        .setPlatform(shareMedia)
                        .setCallback(shareListener).share();
                break;
            case TEXT:
                break;
            default:
        }
    }

    /**
     * share监听
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if (onShareStartListener != null) {
                onShareStartListener.onShareStart();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(mActivity, "分享成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(mActivity, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(mActivity, "分享取消", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 判断用户手机是否装了第三方的App
     *
     * @param packgeName
     * @return
     */
    private boolean isInstall(String packgeName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(packgeName, 0);
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


    /**
     * 分享图片
     *
     * @param activity
     * @param bitmap
     * @param share_media
     */
    public void shareUMBitMap(Activity activity, Bitmap bitmap, SHARE_MEDIA share_media) {
        UMImage image = new UMImage(activity, bitmap);
        new ShareAction(activity)
                .setPlatform(share_media)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        Log.i("asdasdasd", "start");
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.showShort("分享成功");
                        Log.i("asdasdasd", "result");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.showShort(throwable.getMessage());
                        Log.i("asdasdasd", throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .share();
    }

    /**
     * 分享链接
     *
     * @param activity
     * @param url
     * @param share_media
     */
    public void shareUMWeb(Activity activity, String url, String title, String desc, SHARE_MEDIA share_media) {
        UMImage image = new UMImage(activity,R.drawable.logo);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(share_media)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        ToastUtils.showShort("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .share();
    }

    private OnShareStartListener onShareStartListener;

    public void setOnShareStartListener(OnShareStartListener onShareStartListener) {
        this.onShareStartListener = onShareStartListener;
    }

    public interface OnShareStartListener{
        void onShareStart();
    }
}

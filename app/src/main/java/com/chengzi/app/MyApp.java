package com.chengzi.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.event.OnLoginSuccessEvent;
import com.chengzi.app.event.OnNewMessageEvent;
import com.chengzi.app.im.ImUtils;
import com.chengzi.app.third.UmHelper;
import com.chengzi.app.ui.message.activity.AtMeActivity;
import com.chengzi.app.ui.message.activity.CommentAndThumbupActivity;
import com.chengzi.app.ui.message.activity.FansActivity;
import com.chengzi.app.ui.message.activity.QuestionsAndAnswersActivity;
import com.chengzi.app.ui.message.activity.SystemMessageActivity;
import com.chengzi.app.utils.CardUtils;
import com.chengzi.app.utils.GlideImageLoader;
import com.chengzi.app.utils.NimUtils;
import com.chengzi.app.utils.ParamsEncryptInterceptor;
import com.chengzi.app.utils.ProxyHelper;
import com.chengzi.app.utils.UmcustomMsgParser;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.ApiConfigration;
import com.handongkeji.common.Constants;
import com.hwangjr.rxbus.RxBus;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @ClassName:MyApp
 * @PackageName:com.hulianchuxing.app
 * @Create On 2018/1/24 0024   11:04
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2018/1/24 0024 handongkeji All rights reserved.
 */
public class MyApp extends Application {

    private static final String TAG = "MyApp";

    private static MyApp instance;
    public WeakReference<Activity> topActivity;

    public static Activity getTopActivity() {
        return getInstance().topActivity == null ? null : getInstance().topActivity.get();
    }

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        configBugly();

        config();

        configApi();

        //初始化友盟
        initYoumeng();

        registerCallback();

        //解决9.0对非官方公开API 方法或接口有弹窗提示
        closeAndroidPDialog();

        //用户信用信息中 带颜色的CardView
        CardUtils.init();

        ImUtils.getInstance().onCreate(getApplicationContext());

        AccountHelper.getInstance().setOnLogoutListener(new AccountHelper.OnLogoutListener() {
            @Override
            public void onPreLogout() {

                String[] tags = {"user", "doctor", "consultant", "service_organization"};
                UmHelper.deleteTags(getApplicationContext(), tags);

                UmHelper.removeAlias(AccountHelper.getUserId(), getApplicationContext());
                NimUtils.logout();
            }

            @Override
            public void onLogout() {
                EventBus.getDefault().post(new OnLoginSuccessEvent());

            }
        });

    }

    private void configBugly() {
        // 获取当前包名
        String packageName = getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "721e600bee", true, strategy);
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                topActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (topActivity != null && topActivity.get() == activity) {
                    topActivity.clear();
                }
            }
        });
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void config() {

        ProxyHelper.initProxy(this);

        //TODO：
        //  友盟统计 场景类型设置接口
//        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        Utils.init(this);
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);

        initImagePicker();//图片选择加载器初始化

        Constants.init(this);//缓存各目录初始化

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)
                .showThreadInfo(true)
                .tag("yimei")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    private void configApi() {
        ApiConfigration apiConfig = new ApiConfigration();  //106 68   9721
        apiConfig
//                .debugBaseUrl("http://192.168.1.106:8888/")
//                .debugBaseUrl("http://192.168.1.71/")
//                .debugBaseUrl("http://192.168.1.68/")
//                .debugBaseUrl("http://192.168.1.135/")
//                .debugBaseUrl("http://192.168.1.71/")
//                .debugBaseUrl("http://tapi.inchengzi.com/")
//                .debugBaseUrl("http://api.inchengzi.com/")
                .debugBaseUrl(BuildConfig.debugUrl)
                .releaseBaseUrl(BuildConfig.releaseUrl)
                .addInterceptor(new ParamsEncryptInterceptor());
        Api.config(apiConfig);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initYoumeng() {


        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 app key
         * 参数3:友盟 channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(getApplicationContext(), "5cbec4f24ca3572149000ffe", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "3f7506c15c1d00601d3d2e826804a6e0");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setResourcePackageName(getPackageName());
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 自定义消息的回调方法
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {


                EventBus.getDefault().post(new OnNewMessageEvent());

                UmcustomMsgParser parser = UmcustomMsgParser.parse(msg.extra);

                Notification.Builder builder;

                if (Build.VERSION.SDK_INT >= 26) {
                    String push_custom = SPUtils.getInstance().getString("push_custom");
                    if (TextUtils.isEmpty(push_custom)) {
                        SPUtils.getInstance().put("push_custom", "push_custom");
                        NotificationChannel channel = new NotificationChannel("push_custom", "push", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }
                    builder = new Notification.Builder(context, "push_custom");

                } else {
                    builder = new Notification.Builder(context);
                }
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                builder.setTicker(parser.getTicker())
                        .setContentTitle(parser.getTitle())
                        .setContentText(parser.getText())
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.logo);

                Notification notification = builder.build();
                PendingIntent contentIntent = parser.getClickPendingIntent(context);
                PendingIntent deleteIntent = parser.getDismissPendingIntent(context);
                notification.contentIntent = contentIntent;
                notification.deleteIntent = deleteIntent;

                notificationManager.notify(new Random(100).nextInt(), notification);

            }

            /**
             * 自定义通知栏样式的回调方法
             * */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                switch (msg.builder_id) {
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }

            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        UmcustomMsgParser.setMessageClickHandler(new UmcustomMsgParser.MessageClickHandler() {
            @Override
            public void onCustomNotificationClick(Context context, UmcustomMsgParser.JumpParam jumpParam) {
                if (jumpParam == null) {
                    return;
                }
                int type = jumpParam.getType();
                switch (type) {
                    case UmcustomMsgParser.JumpParam.BIND_MESSAGE:      // 0;  解绑和绑定
                    case UmcustomMsgParser.JumpParam.ORDER_MESSAGE:     // 1;  订单成交
                    case UmcustomMsgParser.JumpParam.COMPLAINT_MESSAGE: // 2;  投诉
                    case UmcustomMsgParser.JumpParam.GOODS_MESSAGE:     // 3;  商品信息
                    case UmcustomMsgParser.JumpParam.AUDIT_MESSAGE:     // 4;  审核
                    case UmcustomMsgParser.JumpParam.EVALUATION_MESSAGE:    // 5;  评价
                    case UmcustomMsgParser.JumpParam.PLAN_MESSAGE:      // 6;  美人愁
                    case UmcustomMsgParser.JumpParam.MAIL_MESSAGE:      // 8;  站内信
                        if (AccountHelper.shouldLogin(context, false)) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            if (topActivity != null && am != null) {
                                am.moveTaskToFront(topActivity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
                            } else {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                        }
                        //  系统消息
                        Intent intent = new Intent(context, SystemMessageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case UmcustomMsgParser.JumpParam.FIND_REMIND_MESSAGE:   // 7;  发表@通知
                        //  @我的消息
                        if (AccountHelper.shouldLogin(context)) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            if (topActivity != null && am != null) {
                                am.moveTaskToFront(topActivity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
                            } else {
                                intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                        }
                        intent = new Intent(context, AtMeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case UmcustomMsgParser.JumpParam.PLAN_COMMENT_MESSAGE:  // 9;  美人愁评论
                    case UmcustomMsgParser.JumpParam.PLAN_COMMENT_REPLY_MESSAGE:    // 10; 美人愁回复评论
                    case UmcustomMsgParser.JumpParam.FIND_COMMENT_MESSAGE:      // 11; 发表评论
                    case UmcustomMsgParser.JumpParam.FIND_COMMENT_REPLY_MESSAGE:    //  12; 发表回复评论
                    case UmcustomMsgParser.JumpParam.FIND_LIKE_MESSAGE:         //  13;发表点赞
                        //  评论和赞
                        if (AccountHelper.shouldLogin(context)) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            if (topActivity != null && am != null) {
                                am.moveTaskToFront(topActivity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
                            } else {
                                intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                        }
                        intent = new Intent(context, CommentAndThumbupActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case UmcustomMsgParser.JumpParam.QUESITION_COMMENT_MESSAGE:     //  14;在线问答回复
                    case UmcustomMsgParser.JumpParam.ASKOTHERS_COMMENT_MESSAGE:     //  15; 大家说回答
                        //  问答
                        if (AccountHelper.shouldLogin(context)) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            if (topActivity != null && am != null) {
                                am.moveTaskToFront(topActivity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
                            } else {
                                intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                        }
                        intent = new Intent(context, QuestionsAndAnswersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case UmcustomMsgParser.JumpParam.FANS_MESSAGE:          //    16; 粉丝关注
                        //  粉丝
                        if (AccountHelper.shouldLogin(context)) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            if (topActivity != null && am != null) {
                                am.moveTaskToFront(topActivity.getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
                            } else {
                                intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                        }
                        intent = new Intent(context, FansActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                }

            }

            @Override
            public void onCustomNotificationDismess(Context context, UmcustomMsgParser.JumpParam jumpParam) {

            }

        });

        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {

            }

            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d("aaa", "onSuccess  deviceToken :" + deviceToken);

                RxBus.get().post(Sys.REGISTER_UM_DEVICE_TOKEN, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d("aaa", "onFailure: " + s + "  s1 " + s1);
            }
        });

    }

    {
        PlatformConfig.setWeixin("wx81f3cd1780d24bc7", "d43441cc806b28b8390f822082ed28f5");//
        PlatformConfig.setQQZone("1108210624", "zE7M01CfN1RN40pS");//
        PlatformConfig.setSinaWeibo("3365249122", "66646c5f7f48b66703f600b716a633e1", "https://www.baidu.com");
        PlatformConfig.setAlipay("2019022035349826");
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
    }
}
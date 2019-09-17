package com.chengzi.app.im;

import android.content.Context;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.config.AVChatOptions;
import com.netease.nim.avchatkit.model.ITeamDataProvider;
import com.netease.nim.avchatkit.model.IUserInfoProvider;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.event.IncomingCallEvent;
import com.chengzi.app.im.config.preference.UserPreferences;
import com.chengzi.app.im.event.DemoOnlineStateContentProvider;
import com.chengzi.app.im.session.NimDemoLocationProvider;
import com.chengzi.app.im.session.SessionHelper;
import com.chengzi.app.im.session.extension.UnBindAttachment;
import com.chengzi.app.utils.NimUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

public class ImUtils {

    private static final ImUtils INSTANCE = new ImUtils();

    public static ImUtils getInstance() {
        return INSTANCE;
    }

    public void onCreate(Context context) {

        DemoCache.setContext(context);

        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(context, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(context));

        // crash handler
//        AppCrashHandler.getInstance(this);

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(context)) {

            // 注册自定义推送消息处理，这个是可选项
//            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());

            // 初始化红包模块，在初始化UIKit模块之前执行
//            NIMRedPacketClient.init(this);
            // init pinyin
            PinYin.init(context);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit(context);
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            //关闭撤回消息提醒
//            NIMClient.toggleRevokeMessageNotification(false);
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
            // 初始化音视频模块
            initAVChatKit();
            // 初始化rts模块
//            initRTSKit();

            NIMClient.getService(MsgServiceObserve.class)
                    .observeReceiveMessage(imMessages -> {
                        processDeleteMsg(imMessages);
                    }, true);

        }

    }

    private void initUIKit(Context context) {
        // 初始化
        NimUIKit.init(context, buildUIKitOptions(context));

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();
//
//        // 聊天室聊天窗口的定制初始化。
//        ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
//        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
//        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private UIKitOptions buildUIKitOptions(Context context) {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(context) + "/app";
        return options;
    }

    private void initAVChatKit() {
        AVChatOptions avChatOptions = new AVChatOptions() {
            @Override
            public void logout(Context context) {
//                MainActivity.logout(context, true);
            }
        };
        avChatOptions.entranceActivity = MainActivity.class;
        avChatOptions.notificationIconRes = R.drawable.login_logo;
        AVChatKit.init(avChatOptions);

        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData avChatData) {
                EventBus.getDefault().post(new IncomingCallEvent());
            }
        }, true);
        // 初始化日志系统
        LogHelper.init();
        // 设置用户相关资料提供者
        AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return NimUIKit.getUserInfoProvider().getUserInfo(account);
            }

            @Override
            public String getUserDisplayName(String account) {
                return UserInfoHelper.getUserDisplayName(account);
            }
        });
        // 设置群组数据提供者
        AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
            @Override
            public String getDisplayNameWithoutMe(String teamId, String account) {
                return TeamHelper.getDisplayNameWithoutMe(teamId, account);
            }

            @Override
            public String getTeamMemberDisplayName(String teamId, String account) {
                return TeamHelper.getTeamMemberDisplayName(teamId, account);
            }
        });
    }

//    private void initRTSKit() {
//        RTSOptions rtsOptions = new RTSOptions() {
//            @Override
//            public void logout(Context context) {
////                MainActivity.logout(context, true);
//            }
//        };
//        RTSKit.init(rtsOptions);
//        RTSHelper.init();
//    }

    private LoginInfo getLoginInfo() {
        String account = AccountHelper.getYunxinAccid();
        String token = AccountHelper.getYunxinToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void processDeleteMsg(List<IMMessage> imMessages) {
        if (imMessages == null || imMessages.isEmpty()) {
            return;
        }
        IMMessage imMessage1 = imMessages.get(0);

        IMMessage delMsg = null;
        boolean needDeleteContact = false;
        for (IMMessage imMessage : imMessages) {
            MsgAttachment attachment = imMessage.getAttachment();
            if (attachment != null && (attachment instanceof UnBindAttachment)) {
                needDeleteContact = true;
                delMsg = imMessage;
                break;
            }
        }
        if (!needDeleteContact) {
            return;
        }

        if (delMsg == null) {
            return;
        }

        String fromAccount = delMsg.getFromAccount();
        NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(fromAccount);
        if (userInfo == null) {
            return;
        }
        Map<String, Object> extensionMap = userInfo.getExtensionMap();
        if (extensionMap == null) {
            return;
        }
        Object t = extensionMap.get("t");
        if (t == null) {
            return;
        }
        int fromUserType = Integer.valueOf(t.toString());

        NIMClient.getService(MsgService.class)
                .queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> result, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS) {
                            return;
                        }
                        if (result == null || result.isEmpty()) {
                            return;
                        }
                        for (RecentContact recentContact : result) {

                            if (fromUserType == 4) {    // 医院解绑医生/咨询师

                                NimUtils.sendUnBindMessage(recentContact.getContactId());   //  给每个和该医生/咨询师聊天的对象发一个解绑的消息
                                NIMClient.getService(MsgService.class)
                                        .deleteRecentContact2(recentContact.getContactId(), recentContact.getSessionType());
                                NIMClient.getService(MsgService.class)
                                        .clearChattingHistory(recentContact.getContactId(),recentContact.getSessionType());
                                NIMClient.getService(MsgService.class)
                                        .clearServerHistory(recentContact.getContactId(),recentContact.getSessionType(),true);

                            } else if (fromUserType == 2 || fromUserType == 3) {  //  医生/咨询师自己解绑
                                if (TextUtils.equals(recentContact.getContactId(),fromAccount)) {
                                    NIMClient.getService(MsgService.class)
                                            .deleteRecentContact2(recentContact.getContactId(), recentContact.getSessionType());
                                    NIMClient.getService(MsgService.class)
                                            .clearChattingHistory(recentContact.getContactId(),recentContact.getSessionType());
                                    NIMClient.getService(MsgService.class)
                                            .clearServerHistory(recentContact.getContactId(),recentContact.getSessionType(),true);
                                }
                            }

                        }
                    }
                });
    }
}

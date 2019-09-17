package com.chengzi.app.utils;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.chengzi.app.BuildConfig;
import com.chengzi.app.im.DemoCache;
import com.chengzi.app.im.config.preference.UserPreferences;
import com.chengzi.app.im.session.extension.UnBindAttachment;

import java.util.List;
import java.util.Map;

public class NimUtils {

    public static void login(String account, String token, RequestCallbackWrapper<LoginInfo> callbackWrapper) {
        NimUIKit.login(new LoginInfo(account, token), new RequestCallbackWrapper<LoginInfo>() {
            @Override
            public void onResult(int code, LoginInfo result, Throwable exception) {
                initNotificationConfig();
                NimUIKit.loginSuccess(account);
                DemoCache.setAccount(account);

                if (callbackWrapper != null) {
                    callbackWrapper.onResult(code, result, exception);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callbackWrapper != null) {
                    callbackWrapper.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callbackWrapper != null) {
                    callbackWrapper.onException(exception);
                }
            }
        });
    }

    public static void logout() {
        NimUIKit.logout();
    }

    private static void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    public static void updateUserInfo(Map<UserInfoFieldEnum, Object> map) {
        NIMClient.getService(UserService.class)
                .updateUserInfo(map)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {

                    }
                })
        ;
    }

    // 发起普通会话
    public static void startP2PSession(Context context, String yunxinAccid) {
        NimUIKit.startP2PSession(context, yunxinAccid);
    }

    // 发起普通会话 需要计时

    /**
     * @param context
     * @param yunxinAccid  云信id
     * @param keepTime     0 不需要计时，1 商品 需要计时，2 医院 需要计时
     * @param queryId      商品或医院id
     * @param consultantId 咨询师id
     */
    public static void startP2PSession(Context context, String yunxinAccid, int keepTime, String queryId, String consultantId) {
        NimUIKit.startP2PSession(context, yunxinAccid, keepTime, queryId, consultantId);
    }

    //  发起视频通话
    public static void startVideoCall(Context context, String yunxinAccid) {
        AVChatKit.outgoingCall(context, yunxinAccid, UserInfoHelper.getUserDisplayName(yunxinAccid),
                AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    public static void sendUnBindMessage(String accid) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        config.enablePush = false;
        config.enableRoaming = true;
        config.enableUnreadCount = false;

        UnBindAttachment unBindAttachment = new UnBindAttachment();

        IMMessage message = MessageBuilder.createCustomMessage(accid, SessionTypeEnum.P2P,
                null, unBindAttachment, config);
        NIMClient.getService(MsgService.class).sendMessage(message, true)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
                        ToastUtils.showShort("发送成功");
                    }
                });
    }

    public static void doctorUnBind() {
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

                            NimUtils.sendUnBindMessage(recentContact.getContactId());   //  给每个和该医生/咨询师聊天的对象发一个解绑的消息
                            NIMClient.getService(MsgService.class)
                                    .deleteRecentContact2(recentContact.getContactId(), recentContact.getSessionType());
                            NIMClient.getService(MsgService.class)
                                    .clearChattingHistory(recentContact.getContactId(),recentContact.getSessionType());
                            NIMClient.getService(MsgService.class)
                                    .clearServerHistory(recentContact.getContactId(),recentContact.getSessionType(),true);

                        }
                    }
                });
    }

}

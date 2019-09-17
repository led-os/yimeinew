package com.chengzi.app.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.chengzi.app.receiver.UmCustomMsgBroadcastReceiver;

import java.io.Serializable;
import java.util.Map;

public class UmcustomMsgParser {

    private String jump_type;
    private String text;
    private String title;
    private String ticker;
    private JumpParam jumpParam;


    public static UmcustomMsgParser parse(Map<String, String> params) {
        UmcustomMsgParser parser = new UmcustomMsgParser();
        parser.jump_type = params.get("jump_type");
        parser.ticker = params.get("ticker");
        parser.title = params.get("title");
        parser.text = params.get("text");
        parser.jumpParam = GsonHelper.getDefault().fromJson(params.get("jump_param"), JumpParam.class);
        return parser;
    }

    public String getJump_type() {
        return jump_type;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getTicker() {
        return ticker;
    }

    public JumpParam getJumpParam() {
        return jumpParam;
    }

    public static class JumpParam implements Serializable {

        private static final long serialVersionUID = 868345258L;

        public static final int BIND_MESSAGE               = 0;  // 解绑和绑定
        public static final int ORDER_MESSAGE              = 1;  // 订单成交
        public static final int COMPLAINT_MESSAGE          = 2;  // 投诉
        public static final int GOODS_MESSAGE              = 3;  // 商品信息
        public static final int AUDIT_MESSAGE              = 4;  // 审核
        public static final int EVALUATION_MESSAGE         = 5;  // 评价
        public static final int PLAN_MESSAGE               = 6;  // 美人愁
        public static final int FIND_REMIND_MESSAGE        = 7;  // 发表@通知
        public static final int MAIL_MESSAGE               = 8;  // 站内信
        public static final int PLAN_COMMENT_MESSAGE       = 9;  // 美人愁评论
        public static final int PLAN_COMMENT_REPLY_MESSAGE = 10; // 美人愁回复评论
        public static final int FIND_COMMENT_MESSAGE       = 11; // 发表评论
        public static final int FIND_COMMENT_REPLY_MESSAGE = 12; // 发表回复评论
        public static final int FIND_LIKE_MESSAGE          = 13; // 发表点赞
        public static final int QUESITION_COMMENT_MESSAGE  = 14; // 在线问答回复
        public static final int ASKOTHERS_COMMENT_MESSAGE  = 15; // 大家说回答
        public static final int FANS_MESSAGE               = 16; // 粉丝关注

        private String jump_id;
        private String notification_id;
        private int type;

        public String getJump_id() {
            return jump_id;
        }

        public void setJump_id(String jump_id) {
            this.jump_id = jump_id;
        }

        public String getNotification_id() {
            return notification_id;
        }

        public void setNotification_id(String notification_id) {
            this.notification_id = notification_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public PendingIntent getClickPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UmCustomMsgBroadcastReceiver.class);
        intent.putExtra("jump_param", jumpParam);
        intent.putExtra("action", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    public PendingIntent getDismissPendingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UmCustomMsgBroadcastReceiver.class);
        intent.putExtra("jump_param", jumpParam);
        intent.putExtra("action", 2);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                (int) (System.currentTimeMillis() + 1L), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private static MessageClickHandler messageClickHandler;

    public static void setMessageClickHandler(MessageClickHandler messageClickHandler) {
        UmcustomMsgParser.messageClickHandler = messageClickHandler;
    }

    public static MessageClickHandler getMessageClickHandler() {
        return messageClickHandler;
    }

    public interface MessageClickHandler {
        void onCustomNotificationClick(Context context, JumpParam jumpParam);

        void onCustomNotificationDismess(Context context, JumpParam jumpParam);
    }

}

package com.chengzi.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chengzi.app.utils.UmcustomMsgParser;

public class UmCustomMsgBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_JUMP_PARAM = "jump_param";
    private static final String EXTRA_ACTION = "action";

    @Override
    public void onReceive(Context context, Intent intent) {

        UmcustomMsgParser.JumpParam jumpParam = (UmcustomMsgParser.JumpParam) intent.getSerializableExtra(EXTRA_JUMP_PARAM);
        int action = intent.getIntExtra(EXTRA_ACTION, -1);
        switch (action) {
            case 1:
                if (UmcustomMsgParser.getMessageClickHandler() != null) {
                    UmcustomMsgParser.getMessageClickHandler().onCustomNotificationClick(context, jumpParam);
                }
                break;
            case 2:
                if (UmcustomMsgParser.getMessageClickHandler() != null) {
                    UmcustomMsgParser.getMessageClickHandler().onCustomNotificationDismess(context, jumpParam);
                }
                break;
        }
    }
}

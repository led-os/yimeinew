package com.chengzi.app.third;

import android.content.Context;
import android.util.Log;

import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class UmHelper {

    private static final String TAG = UmHelper.class.getSimpleName();

    public static void addUmengAlias(String uid, Context context) {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        String alias = "yimei" + uid;
        String alias_type = "yimei_type";
        mPushAgent.addAlias(alias, alias_type, (isSuccess, s) -> {
            if (isSuccess) {
                // 设置alias成功
                Log.d(TAG, "addUmengAlias: " + isSuccess);
            }
        });
    }

    public static void removeAlias(String uid, Context context) {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        String alias = "yimei" + uid;
        String alias_type = "yimei_type";
        mPushAgent.deleteAlias(alias, alias_type, (isSuccess, message) -> {
            if (isSuccess) {
                Log.d(TAG, "removeAlias: " + isSuccess + " message");
            }
        });
    }

    //  添加友盟标签
    public static void addTag(Context context, String... tag) {
        PushAgent pushAgent = PushAgent.getInstance(context);
        pushAgent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean b, ITagManager.Result result) {
                Log.d(TAG, "onMessage: addTag  " + result.msg +"  "+ Arrays.toString(tag));
            }
        }, tag);

    }

    public static void deleteTags(Context context,String... tag){
        PushAgent pushAgent = PushAgent.getInstance(context);
        pushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean b, ITagManager.Result result) {
                Log.d(TAG, "onMessage: deleteTags " + result.msg+ "  "+Arrays.toString(tag));
            }
        }, tag);
    }
}
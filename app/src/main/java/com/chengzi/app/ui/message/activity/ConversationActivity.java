package com.chengzi.app.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.q.a;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityConversationBinding;
import com.chengzi.app.im.session.extension.OnlineCaseAttachment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

/**
 * 聊天回话 消息
 *
 * @ClassName:ConversationActivity
 * @PackageName:com.yimei.app.ui.message.activity
 * @Create On 2019/4/11 0011   17:18
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class ConversationActivity extends BaseActivity<ActivityConversationBinding, BaseViewModel> {

    public static void start(Context context, int userType) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(Sys.EXTRA_USER_TYPE, userType);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_conversation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        int userType = getIntent().getIntExtra(Sys.EXTRA_USER_TYPE, 1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                Field mMap = BaseBundle.class.getDeclaredField("mMap");
                mMap.setAccessible(true);
                ArrayMap<String, Object> o = (ArrayMap<String, Object>) mMap.get(extras);
                Set<String> strings = o.keySet();
                for (String string : strings) {
                    Object o1 = o.get(string);
                    if (o1 != null && (o1 instanceof ArrayList) && ((ArrayList) o1).size() > 0 && ((ArrayList) o1).get(0) != null) {
                        Object o2 = ((ArrayList) o1).get(0);
                        com.netease.nimlib.q.a object = (a) o2;
                        String fromAccount = object.getFromAccount();
                        NimUserInfo userInfo = NimUserInfoCache.getInstance().getUserInfo(fromAccount);
                        if (userInfo != null && userInfo.getExtensionMap() != null && userInfo.getExtensionMap().get("t") != null) {
                            userType = Integer.valueOf(userInfo.getExtensionMap().get("t").toString());
                            if (userType == 3) {
                                Object isWorker = userInfo.getExtensionMap().get("isWorker");
                                if (isWorker != null && Integer.valueOf(isWorker.toString()) == 1) {
                                    userType = 5;
                                }
                            }
                        }
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String[] title = {"用户消息", "医生消息", "咨询师消息", "机构消息","客服消息"};
        mBinding.topBar.setCenterText(title[userType - 1]);

        RecentContactsFragment recentContactsFragment = new RecentContactsFragment();
        recentContactsFragment.getArguments().putInt("userType", userType);
        recentContactsFragment.setCustomCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {

            }

            @Override
            public String getDigestOfAttachment(RecentContact recent, MsgAttachment attachment) {
                if (attachment instanceof OnlineCaseAttachment) {
                    OnlineCaseAttachment onlineCaseAttachment = (OnlineCaseAttachment) attachment;
                    String caseUserName = onlineCaseAttachment.getCaseUserName();
                    if (TextUtils.equals(recent.getFromAccount(), NimUIKit.getAccount())) {
                        return "[我的病例]";
                    } else {
                        return "[" + caseUserName + "的病例]";
                    }
                }
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, recentContactsFragment, "RecentContactsFragment")
                .commitAllowingStateLoss();

    }
}

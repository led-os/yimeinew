package com.chengzi.app.im.session.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.handong.framework.account.AccountHelper;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.chengzi.app.R;
import com.chengzi.app.im.session.extension.OnlineCaseAttachment;
import com.chengzi.app.ui.mine.activity.onlinecases.OnlineCasesActivity;

public class MsgViewHolderOnlineCase extends MsgViewHolderBase {

    private HeadImageView headImageView;
    private TextView tvName;
    private View root;

    public MsgViewHolderOnlineCase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.msg_item_online_case;
    }

    @Override
    protected void inflateContentView() {
        root = findViewById(R.id.root);
        headImageView = findViewById(R.id.avatar);
        tvName = findViewById(R.id.tv_nick_name);
    }

    @Override
    protected void bindContentView() {
        MsgAttachment attachment = message.getAttachment();
        if (attachment instanceof OnlineCaseAttachment) {
            String avatar = ((OnlineCaseAttachment) attachment).getCaseUserAvatar();
            headImageView.loadAvatar(avatar);
            tvName.setText(((OnlineCaseAttachment) attachment).getCaseUserName());
        }
    }

    @Override
    protected void onItemClick() {
        MsgAttachment attachment = message.getAttachment();
        if (attachment instanceof OnlineCaseAttachment) {
            String userId = ((OnlineCaseAttachment) attachment).getCaseUserId();

            if (TextUtils.equals(userId, AccountHelper.getUserId())) {
                OnlineCasesActivity.start(context, userId);
            } else {
                OnlineCasesActivity.startForView(context,userId);
            }
        }
    }
}

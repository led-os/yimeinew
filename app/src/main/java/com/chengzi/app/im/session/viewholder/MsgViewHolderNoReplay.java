package com.chengzi.app.im.session.viewholder;

import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.chengzi.app.R;
import com.chengzi.app.im.session.extension.NoReplayAttachment;
import com.chengzi.app.utils.NimUtils;

public class MsgViewHolderNoReplay extends MsgViewHolderBase {

    private TextView textView;

    public MsgViewHolderNoReplay(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.msg_holder_no_replay_layout;
    }

    @Override
    protected void inflateContentView() {
        textView = findViewById(R.id.textView);
    }

    @Override
    protected void bindContentView() {
        textView.setText("对方长时间未回复，我们给您推荐了在线咨询师");
    }

    @Override
    protected void onItemClick() {
        MsgAttachment attachment = message.getAttachment();
        if (attachment == null) {
            return;
        }
        if (!(attachment instanceof NoReplayAttachment)) {
            return;
        }
        NoReplayAttachment noReplayAttachment = ((NoReplayAttachment) attachment);
        NimUtils.startP2PSession(context,noReplayAttachment.getYunxinAccid(),noReplayAttachment.getKeepTime(),
                noReplayAttachment.getQueryId(),noReplayAttachment.getConsultantId());
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }

}

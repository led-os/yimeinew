package com.netease.nim.uikit.business.session;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

public interface NoReplayAttachmentFetcher {

    MsgAttachment fetchAttachment(String yunxinAccid, int keepTime, String queryId, String consultantId);

}

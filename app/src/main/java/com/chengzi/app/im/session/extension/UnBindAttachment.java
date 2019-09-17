package com.chengzi.app.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class UnBindAttachment extends CustomAttachment {

    public UnBindAttachment() {
        super(CustomAttachmentType.UnBind);
    }

    @Override
    protected void parseData(JSONObject data) {

    }

    @Override
    protected JSONObject packData() {
        return null;
    }
}

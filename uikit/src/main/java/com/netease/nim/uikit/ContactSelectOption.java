package com.netease.nim.uikit;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

public class ContactSelectOption {

    private MsgAttachment attachment;
    private Class targetClass;

    public ContactSelectOption() {
    }

    public ContactSelectOption setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public MsgAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(MsgAttachment attachment) {
        this.attachment = attachment;
    }
}

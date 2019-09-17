package com.chengzi.app.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class OnlineCaseAttachment extends CustomAttachment {

    private static final String CASE_USER_ID = "case_uid";
    private static final String NAME = "name";
    private static final String AVATAR = "headerImageUrl";


    private String caseUserId;    //  病例所有者的id
    private String caseUserName;
    private String caseUserAvatar;

    public OnlineCaseAttachment() {
        super(CustomAttachmentType.OnlineCase);
    }

    @Override
    protected void parseData(JSONObject data) {
        if (data != null) {
            caseUserId = data.getString(CASE_USER_ID);
            caseUserName = data.getString(NAME);
            caseUserAvatar = data.getString(AVATAR);
        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CASE_USER_ID, caseUserId);
        jsonObject.put(NAME, caseUserName);
        jsonObject.put(AVATAR, caseUserAvatar);

        return jsonObject;
    }

    public String getCaseUserId() {
        return caseUserId;
    }

    public void setCaseUserId(String caseUserId) {
        this.caseUserId = caseUserId;
    }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
    }

    public String getCaseUserAvatar() {
        return caseUserAvatar;
    }

    public void setCaseUserAvatar(String caseUserAvatar) {
        this.caseUserAvatar = caseUserAvatar;
    }
}

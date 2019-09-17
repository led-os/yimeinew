package com.chengzi.app.im.session.extension;

import com.alibaba.fastjson.JSONObject;

public class NoReplayAttachment extends CustomAttachment {

    private String yunxinAccid;
    private int keepTime;
    private String queryId;
    private String consultantId;

    public NoReplayAttachment() {
        super(CustomAttachmentType.NoReplay);
    }



    @Override
    protected void parseData(JSONObject data) {
        yunxinAccid = data.getString("yunxinAccid");
        keepTime = data.getInteger("keepTime");
        queryId = data.getString("queryId");
        consultantId = data.getString("consultantId");
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("yunxinAccid",yunxinAccid);
        object.put("keepTime",keepTime);
        object.put("queryId",queryId);
        object.put("consultantId",consultantId);
        return object;
    }

    public String getYunxinAccid() {
        return yunxinAccid;
    }

    public int getKeepTime() {
        return keepTime;
    }

    public String getQueryId() {
        return queryId;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setYunxinAccid(String yunxinAccid) {
        this.yunxinAccid = yunxinAccid;
    }

    public void setKeepTime(int keepTime) {
        this.keepTime = keepTime;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }
}

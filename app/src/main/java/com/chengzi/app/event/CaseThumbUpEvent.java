package com.chengzi.app.event;

public class CaseThumbUpEvent {

    private String caseId;
    private boolean thumbUp;

    public CaseThumbUpEvent(String caseId, boolean thumbUp) {
        this.caseId = caseId;
        this.thumbUp = thumbUp;
    }

    public String getCaseId() {
        return caseId;
    }

    public boolean isThumbUp() {
        return thumbUp;
    }
}

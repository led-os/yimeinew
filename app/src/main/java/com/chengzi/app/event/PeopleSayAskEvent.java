package com.chengzi.app.event;

public class PeopleSayAskEvent {
    private String targetId;
    private int targetType;

    public PeopleSayAskEvent(String targetId, int targetType) {
        this.targetId = targetId;
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public int getTargetType() {
        return targetType;
    }
}

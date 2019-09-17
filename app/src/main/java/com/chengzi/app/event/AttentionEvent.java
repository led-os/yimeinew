package com.chengzi.app.event;

public class AttentionEvent {

    private String userId;  //  被关注者id
    private boolean attention;  //  true 已关注，false 未关注

    public AttentionEvent(String userId, boolean attention) {
        this.userId = userId;
        this.attention = attention;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isAttention() {
        return attention;
    }
}

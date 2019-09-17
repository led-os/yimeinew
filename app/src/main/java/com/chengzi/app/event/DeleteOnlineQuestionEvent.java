package com.chengzi.app.event;

public class DeleteOnlineQuestionEvent {

    private String questionId;

    public DeleteOnlineQuestionEvent(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }
}

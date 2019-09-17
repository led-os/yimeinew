package com.chengzi.app.event;

public class AnswerOnlineQuestionEvent {

    private String questionId;
    private int answerNum;

    public AnswerOnlineQuestionEvent(String questionId, int answerNum) {
        this.questionId = questionId;
        this.answerNum = answerNum;
    }

    public String getQuestionId() {
        return questionId;
    }

    public int getAnswerNum() {
        return answerNum;
    }
}

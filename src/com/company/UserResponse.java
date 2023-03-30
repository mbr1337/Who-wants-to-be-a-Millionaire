package com.company;

/**
 * Ta klasa reprezentuje odpowiedź użytkownika na pytanie,
 * która może być wyborem koła ratunkowego lub jedną z odpowiedzi.
 */
public class UserResponse {
    private LifeLine lifeline;
    private int answerToken;

    public UserResponse() {
    }

    public UserResponse(int token, LifeLine lifeLineValue) {
        lifeline = lifeLineValue;
        answerToken = token;
    }

    public LifeLine getLifeline() {
        return lifeline;
    }

    public void setLifeline(LifeLine lifeline) {
        this.lifeline = lifeline;
    }

    public int getAnswerToken() {
        return answerToken;
    }

    public void setAnswerToken(int answerToken) {
        this.answerToken = answerToken;
    }
}
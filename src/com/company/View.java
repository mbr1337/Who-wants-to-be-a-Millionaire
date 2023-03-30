package com.company;

import java.util.List;

/**
 * Ten interfejs określa funkcjonalność, którą
 * muszą zaimplementować wszystkie widoki. Umożliwi to
 * w przyszłości łatwiej tworzyć dodatkowe widoki
 */
public interface View {
    public void displayErrorMessage(String message);

    public void displayIntro();

    public void displayQuestion(Question question);

    public void displayAnswer(String answer);

    public void displayLifeLines(List<LifeLine> lifeLines);

    public void displayCallFriendResults(Question question, int roundIndex);

    public void displayAudienceResults(Question question);

    public void displayOneLineMessage(String promptText);

    public UserResponse getUserResponseToQuestion(List<LifeLine> lifelines);

    public boolean getIsFinalAnswer();

//    public String getUserName();

    public void closeOpenResources();
}
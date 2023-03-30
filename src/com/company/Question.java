package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ta klasa reprezentuje jedno pytanie, które można zadać graczowi.
 */
public class Question {
    private String text;
    private int correctAnswer;
    private List<String> answers;

    public Question() {
    }

    public Question(String txt, int rightAnswer, List<String> possibleAnswers) {
        this.text = txt;
        this.correctAnswer = rightAnswer;
        this.answers = new ArrayList<>();

        for (int i = 0; i < possibleAnswers.size(); i++) {
            this.answers.add(digitToLetterOfQuestion(i) + ": " + possibleAnswers.get(i));
        }
    }

    public static Character digitToLetterOfQuestion(int digit) {
        return switch (digit) {
            case 0 -> 'A';
            case 1 -> 'B';
            case 2 -> 'C';
            case 3 -> 'D';
            case 4 -> 'E';
            case 5 -> 'F';
            case 6 -> 'G';
            case 7 -> 'H';
            default -> '!';
        };
    }

    public void removeAnswer(int index) {
        answers.remove(index);
    }

    public List<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrectAnswer(int ans) {
        return correctAnswer == ans ? true : false;
    }

    public int getNumberOfAnswers() {
        return answers.size();
    }
}



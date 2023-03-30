package com.company;

import java.util.*;

/**
 * To jest tekstowa implementacja interfejsu View. Uzyskuje dane wejściowe
 * użytkownika z System.in i wyświetla informacje, pisząc do System.out
 */
public class MainView implements View {
    private static final Scanner scanner = new Scanner(System.in);


    public void displayQuestion(Question question) {
        displayOneLineMessage(question.getText());
        for (String answer : question.getAnswers()) {
            displayAnswer(answer);
        }
    }

    public void displayAnswer(String answer) {
        displayOneLineMessage(answer);
    }

    public void displayLifeLines(List<LifeLine> lifeLines) {
        if (lifeLines.isEmpty()) {
            displayOneLineMessage("Nie masz żadnych pozostałych kół ratunkowych. ");
        } else {
            displayOneLineMessage("\nDostępne koła ratunkowe:");
        }
        for (int i = 0; i < lifeLines.size(); i++) {
            if (lifeLines.get(i) == LifeLine.FIFTY_FIFTY)
                displayOneLineMessage((i + 1) + ". " + "50 na 50");
            else if (lifeLines.get(i) == LifeLine.POLL_AUDIENCE)
                displayOneLineMessage((i + 1) + ". " + "Pytanie do publiczności");
            else if (lifeLines.get(i) == LifeLine.CALL_A_FRIEND)
                displayOneLineMessage((i + 1) + ". " + "Telefon do przyjaciela");
        }
    }

    public void displayCallFriendResults(Question question, int roundIndex) {
        if (roundIndex <= 4) {
            displayOneLineMessage(
                    "Cześć, tu Zbyszek. Jestem raczej pewny, że odpowiedź proprawna odpowiedź to "
                            + Question.digitToLetterOfQuestion(question.getCorrectAnswer()-1)
                            + ". Powodzenia w dalszej grze"
            );
        } else {
            displayOneLineMessage("Cześć, tu Zbyszek. Prawdę mówiąc nie mam pojęcia jaka jest odpowiedź. Ale statystycznie najczęściej prawidłowa jest odpowiedź C.");
        }
    }

    public void displayAudienceResults(Question question) {
        int rightAnswer = question.getCorrectAnswer() - 1;
        int rightAnswerVotes = new Random().nextInt(51) + 50;
        int votesPool = 100 - rightAnswerVotes;
        int numberOfAnswers = question.getNumberOfAnswers();

        List<Integer> votesList = new ArrayList<>();

        for (int i = 0; i < numberOfAnswers - 1; i++) {
            if (i == numberOfAnswers - 2) {
                votesList.add(votesPool);
            } else {// any answer except right one and last one
                votesList.add(new Random().nextInt(votesPool - 2));
                votesPool -= votesList.get(i);
            }
        }

        Collections.shuffle(votesList);

        displayOneLineMessage("Głosujący wybrali następujące odpowiedzi:");
        int j = 0;

        for (int i = 0; i < votesList.size(); i++) {
            if (i == rightAnswer) {
                displayOneLineMessage("Odpowiedź " + Character.toString(Question.digitToLetterOfQuestion(j)) + ": " + rightAnswerVotes + "%");
                j++;
            }
            displayOneLineMessage("Odpowiedź " + Character.toString(Question.digitToLetterOfQuestion(j)) + ": " + votesList.get(i) + "%");
            j++;
        }

    }

    public void displayOneLineMessage(String promptText) {
        System.out.println(promptText);
    }

    /**
     * Próbuje uzyskać AnswerToken z danych wejściowych użytkownika
     *
     * @param input tekst wprowadzony przez użytkownika (zakładając, że jest już przycięty)
     * @return AnswerToken pasujący do danych wejściowych lub null, jeśli nie ma pasującego tokenu.
     */
    private int getAnswerTokenFromInput(String input) {
        input = input.toUpperCase();

        return switch (input) {
            case "A" -> 1;
            case "B" -> 2;
            case "C" -> 3;
            case "D" -> 4;
            case "E" -> 5;
            case "F" -> 6;
            case "G" -> 7;
            case "H" -> 8;
            default -> 999;
        };
    }

    /**
     * Próbuje uzyskać koło ratunkowe z danych wejściowych użytkownika
     *
     * @param lifelines lista lin ratunkowych dostępnych po dokonaniu przez użytkownika wyboru
     * @param input     tekst wprowadzony przez użytkownika (zakładając, że jest już przycięty)
     * @return Koło ratunkowe wybrane przez użytkownika
     */
    private LifeLine getLifeLineFromInput(List<LifeLine> lifelines, String input) {
        try {
            // Uzyskaj wybraną linię życia, dostosowując do faktu, że dane wejściowe użytkownika
            //  wynoszą od 1 do 3, podczas gdy tablica jest indeksowana od 0
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < lifelines.size())
                return lifelines.get(index);
        } catch (NumberFormatException e) {
            //użytkownik wprowadził wartość niebędącą liczbą całkowitą.
        }
        return null;
    }

    public UserResponse getUserResponseToQuestion(List<LifeLine> lifelines) {
        String userInput = scanner.nextLine().trim();

        //Spróbuj uzyskać token odpowiedzi od użytkownika
        int token = getAnswerTokenFromInput(userInput);
        //Spróbuj uzyskać wybór koła ratunkowego od użytkownika
        LifeLine lifeLine = getLifeLineFromInput(lifelines, userInput);

        return new UserResponse(token, lifeLine);
    }

    /**
     * Pyta się czy odpowiedź jest ostateczna
     *
     * @return true jeśli jest, false jeśli nie.
     */
    public boolean getIsFinalAnswer() {
        String finalAnswer = null;
        do {
            displayOneLineMessage("Czy to twoja ostateczna odpowiedź? ? (Tak lub Nie[t/n])");
            finalAnswer = scanner.nextLine().trim();
        } while (!isValidYesNoResponse(finalAnswer));

        finalAnswer = finalAnswer.trim();
        return "Tak".equalsIgnoreCase(finalAnswer) || "T".equalsIgnoreCase(finalAnswer);
    }

    /**
     * Sprawdza, czy określone dane wejściowe reprezentują prawidłową odpowiedź tak lub nie.
     *
     * @param input String do analizy
     * @return true jeśli string = tak, t, nie, n (wielkość liter nie jest rozróżniana ); inaczej false.
     */
    private boolean isValidYesNoResponse(String input) {
        input = input.trim();
        return input != null && ("Tak".equalsIgnoreCase(input) ||
                "t".equalsIgnoreCase(input) || "Nie".equalsIgnoreCase(input)
                || "n".equalsIgnoreCase(input));
    }

    public void closeOpenResources() {
        scanner.close();
    }

    private static final String INTRO_PREAMBLE = "" +
            "\n\n\n Witamy w Milionerach! Jak na pewno wiesz, będziesz musiał odpowiedzieć poprawnie na 13 pytań pod rząd. \n" +
            " Ale masz 3 koła ratunkowe do pracy: 50/50, ankieta publiczności lub zapytanie znajomego \n" +
            " A więc Zaczynamy! \n";

    public void displayIntro() {
        displayOneLineMessage(INTRO_PREAMBLE);
    }

    public void displayErrorMessage(String msg) {
        displayOneLineMessage(msg);
    }
}
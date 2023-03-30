package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Główna klasa kontrolująca gre
 * koordynuje model danych i widok (View).
 */
public class Main {

    private static final Random random = new Random();
    private static final List<LifeLine> lifeLines = new ArrayList<>();
    private static final View view = new MainView();

    /**
     * Określa, czy dane wprowadzone przez użytkownika są prawidłowe na podstawie tego, czy
     * dane wejściowe zostały przeanalizowane do prawidłowego wyboru odpowiedzi lub koła ratunkowego
     * odpowiedniego do kontekstu sytuacji.
     *
     * @param question Obecnie wyświetlane pytanie. Użyte do zweryfikowania czy
     *                 użytkownik podał poprawną odpowiedź.
     * @param token    reprezentacja danych wejściowych użytkownika jako jedna z odpowiedzi na pytanie
     * @param lifeline reprezentacja danych wejściowych użytkownika jako jedna z opcji koła ratunkowego
     * @return true, jeśli dane wejściowe użytkownika są prawidłowe, w przeciwnym razie false.
     */
    private static boolean isInputValid(Question question, int token, LifeLine lifeline) {
        if (token >= 0 && token <= 7) {
            return true;
        }

        return lifeline != null;
    }

    /**
     * Logika potrzebna do użycia koła ratunkowego
     *
     * @param question     obecne pytanie
     * @param lifeLineUsed użycie koła ratunkowego
     */
    private static void useLifeLine(Question question, LifeLine lifeLineUsed, int roundIndex) {
        lifeLines.remove(lifeLineUsed);

        switch (lifeLineUsed) {

            case CALL_A_FRIEND:
                view.displayCallFriendResults(question, roundIndex);
                break;
            case FIFTY_FIFTY:
                useFiftyFiftyLifeLine(question);
                break;
            case POLL_AUDIENCE:
                view.displayAudienceResults(question);
                break;
            default:
                System.err.println("ERROR Nieznane koło ratunkowe! coś pan nabroił?");
        }
    } //useLifeLine

    /**
     * logika użycia koła ratunkowego 50/50 odpowiedzialna za usunięcie
     * 2 losowych niepoprawnych odpowiedzi z pytania.
     *
     * @param question pytania, które będą zmodyfikowane.
     */
    private static void useFiftyFiftyLifeLine(Question question) {
        for (int i = 0; i < 2; i++) {
            removeRandomIncorrectAnswer(question);
        }
    } //useFiftyFiftyLifeLine

    /**
     * Usuwa jedną niepoprawną odpowiedź
     *
     * @param question pytanie, z którego odpowiedź ma być usunięta.
     */
    private static void removeRandomIncorrectAnswer(Question question) {
        Random rnd = new Random();
        int randomIndex;
        while (true) {
            randomIndex = rnd.nextInt(question.getNumberOfAnswers());
            if (question.getCorrectAnswer() - 1 != randomIndex)
                break;
        }
        question.removeAnswer(randomIndex);
    } //removeRandomIncorrectAnswer

    private static String displayRoundIndexAndAward(int roundIndex) {
        String a = "\n Przed tobą runda ";
        return switch (roundIndex) {
            case 1 -> a + "pierwsza, a nagrodą jest aż 500 zł!";
            case 2 -> a + "druga, a nagrodą jest aż 1000 zł!";
            case 3 -> a + "trzecia, a nagrodą jest aż 2000 zł!";
            case 4 -> a + "czwarta, a nagrodą jest aż 5000 zł!";
            case 5 -> a + "piąta, a nagrodą jest aż 10 000 zł!";
            case 6 -> a + "szósta, a nagrodą jest aż 20 000 zł!";
            case 7 -> a + "siódma, a nagrodą jest aż 40 000 zł!";
            case 8 -> a + "ósma , a nagrodą jest aż 75 000 zł!";
            case 9 -> a + "dziewiąta, a nagrodą jest aż 125 000 zł!";
            case 10 -> a + "dziesiąta, a nagrodą jest aż 250 000 zł!";
            case 11 -> a + "jedenasta, a nagrodą jest aż 500 000 zł!";
            case 12 -> a + "finałowa - dwunasta, a nagrodą jest aż 1 000 000 zł!";
            default -> "displayRoundIndexAndAward() error";
        };
    }


    /**
     * Laduje pytania do odpowiedniego modelu danych
     *
     * @return lista list wszystkich załadowanych pytań.
     */
    private static List<List<Question>> loadSetsOfQuestions(int numberOfSets) throws Exception {
        List<List<Question>> setsOfQuestions = new ArrayList<>();

        for (int i = 1; i <= numberOfSets; i++) {//po plikach
            Scanner sc = new Scanner(new File("questions/" + Integer.toString(i) + ".csv"));
            sc.useDelimiter("\n");
            List<Question> singleList = new ArrayList<>();
            while (sc.hasNext()) {//po linijkach
                String[] line = sc.next().split(",");

                String questionItself = line[1];
                int rightAnswer = Character.getNumericValue(line[line.length - 1].charAt(0));

                List<String> answers = new ArrayList<>();
                for (int j = 2; j <= line.length - 2; j++) {
                    answers.add(line[j]);
                }
                singleList.add(new Question(questionItself, rightAnswer, answers));
            }
            setsOfQuestions.add(singleList);
            sc.close();
        }

        return setsOfQuestions;
    }

    public static void main(String[] args) {
        lifeLines.addAll(Arrays.asList(LifeLine.values()));

        final int numberOfRounds = 12;

        List<List<Question>> setsOfQuestions = new ArrayList<>();
        try {
            setsOfQuestions = loadSetsOfQuestions(numberOfRounds);
        } catch (Exception e) {
            System.out.println(e);
        }

        view.displayIntro();


        for (int i = 0; i < numberOfRounds; i++) {
            int randomIndex = random.nextInt(setsOfQuestions.get(i).size());
            Question randQuestion = setsOfQuestions.get(i).get(randomIndex);

            System.out.println(displayRoundIndexAndAward(i + 1) + "\n");

            boolean isFinalAnswer = false;

            while (!isFinalAnswer) {
                view.displayQuestion(randQuestion);
                view.displayLifeLines(lifeLines);
                UserResponse questionResponse = view.getUserResponseToQuestion(lifeLines);

                //Sprawdź poprawność danych wejściowych — użytkownik powinien wprowadzić odpowiedź lub linię ratunkową.
                //  Jeśli tak nie jest, wyświetl błąd i poproś, o więcej danych wejściowych
                //  aż odpowiedź będzie prawidłowa.
                while (!isInputValid(
                        randQuestion,
                        questionResponse.getAnswerToken(),
                        questionResponse.getLifeline())) {
                    view.displayErrorMessage("Nieprawidłowe dane wejściowe. Spróbuj ponownie. \n\n");
                    questionResponse = view.getUserResponseToQuestion(lifeLines);
                }

                // Jak użytkownik użyje koła ratunkowego, użyj go
                if (questionResponse.getLifeline() != null) {
                    useLifeLine(randQuestion, questionResponse.getLifeline(), i);
                } else {
                    //  Użytkownik podał odpowiedź, spytaj czy jest ona ostateczna
                    isFinalAnswer = view.getIsFinalAnswer();
                    if (isFinalAnswer) {
                        //Jeśli tak, sprawdź czy jest poprawna
                        boolean rightAnswer = randQuestion.isCorrectAnswer(questionResponse.getAnswerToken());
                        if (rightAnswer) {
                            //Jeśli jest poprawna, daj znać
                            if (i + 1 == numberOfRounds) {
                                view.displayOneLineMessage("Poprawna odpowiedź! Wygrałeś 1 000 000 zł! Gratulacje!!!");
                            } else {
                                view.displayOneLineMessage("Poprawna odpowiedź!");
                            }
                        } else {
                            //Jesli odpowiedź jest bledna, ZAKONCZ ISTNIENIE GRACZA
                            view.displayOneLineMessage("Niestety, przegrałeś! Może następnym razem pójdzie ci lepiej.");
                            return;
                        } //else
                    } //if
                } //else
            } //while
        } //for
        view.closeOpenResources();
    } //main

} //Main
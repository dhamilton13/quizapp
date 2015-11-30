package com.example.quizletclone;

/**
 * Created by sunnysarow on 11/22/15.
 */

public class TestGrader {
    private static String[] correctAnswers, userAnswers;
    private static boolean[] answersBeingGraded;
    private static boolean areAnswersBeingRecorded = false, isTestGraded = false;
    private static int numberCorrect = 0;

    TestGrader(){
    }

    public static void gradeTest() {
        isTestGraded = true;
    }

    public static void setTestSize(int size) {
        userAnswers = new String[size];
        correctAnswers = new String[size];
        answersBeingGraded = new boolean[size];
    }

    public static void addCorrectAnswer(String correctAnswer, int index) {
        areAnswersBeingRecorded = true;
        correctAnswers[index] = correctAnswer;
    }

    public static void addUserAnswer(String userAnswer, int index) {
        userAnswers[index] = userAnswer;
        answersBeingGraded[index] = true;
    }

    public static void clearGrader() {
        areAnswersBeingRecorded = false;
        isTestGraded = false;
        correctAnswers = null;
        userAnswers = null;
        answersBeingGraded = null;
        numberCorrect = 0;
    }

    public static boolean checkAnswer(String userAnswer, String correctAnswer) {
        boolean answerIsCorrect= false;
         if (userAnswer != null) {
             if (userAnswer.toLowerCase().equals(correctAnswer.toLowerCase()))
                 answerIsCorrect = true;
         }
        return answerIsCorrect;
    }

    public static boolean checkArrayAnswer(String[] userAnswers, String[] correctAnswers) {
        boolean answerIsCorrect= false;

        if (userAnswers != null || userAnswers.length != 0) {
            if (userAnswers.length == correctAnswers.length) {
                answerIsCorrect = true;
                for (int i = 0; i < correctAnswers.length; ++i) {
                    if (!correctAnswers[i].toLowerCase().equals
                            (userAnswers[i].toLowerCase())) {
                        answerIsCorrect = false;
                        break;
                    }
                }
            }
        }
        return answerIsCorrect;
    }
    public static void increaseScore() {
        numberCorrect++;
    }

    public static double calculateScore() {
        return numberCorrect/correctAnswers.length * 100;
    }

    public static int getNumberCorrect() {
        return numberCorrect;
    }
    public static boolean isTestInProgress() {
        return areAnswersBeingRecorded;
    }

    public static boolean isTestGraded() {
        return isTestGraded;
    }

    public static String[] getUserAnswers() {
        return userAnswers;
    }

    public static String[] getCorrectAnswers() {
        return correctAnswers;
    }

    public static boolean[] getAnswersBeingGraded() { return answersBeingGraded; }
}

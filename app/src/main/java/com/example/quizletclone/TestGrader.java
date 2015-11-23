package com.example.quizletclone;

/**
 * Created by sunnysarow on 11/22/15.
 */

public class TestGrader {
    private static String[] correctAnswers, userAnswers;
    private static boolean[] answersBeingGraded;
    private static boolean areAnswersBeingRecorded = false, isTestGraded = false;

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

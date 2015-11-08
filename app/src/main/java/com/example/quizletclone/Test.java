package com.example.quizletclone;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by sunnysarow on 11/4/15.
 */
public class Test {
    private boolean isTestDynamic, multipleChoice, shortAnswer, checkAllThatApply, trueFalse;
    private List<Flashcard> flashcards;
    private String name;


    Test(String name, boolean isTestDynamic) {
        this.name = name;
        this.isTestDynamic = isTestDynamic;

        //setFlags(isTestDynamic);
    }

    /* Build a test with proper formatting. The test structure depends on whether the user
        specifies his/her own question types (mc, tf, etc.) or randomized. Possible future
         implementation of choosing individual cards. This method currently only displays
         a title and does not build the test .
     */
    public ArrayList<String> generateTest(ArrayList<Flashcard> fcs){
        ArrayList<String> questions = new ArrayList<String>();
        this.flashcards = fcs;

        for (int i = 0; i < flashcards.size(); ++i) {
            questions.add(flashcards.get(i).getQuestion());
        }

        return questions;
    }

    public void addFlashcards(ArrayList<Flashcard> cards) {}

    public void addFlashcard(Flashcard card) {
        this.flashcards.add(card);
    }

    public int getNumberOfQuestions() {
        return flashcards.size();
    }

    public String getName() { return this.name; }

    /* Set certain flags as true depending on which tags the user specifies to be added to the test
    * */
    private void setFlags(boolean flag) {
        if (flag) {
            multipleChoice = true;
            shortAnswer = true;
            checkAllThatApply = true;
            trueFalse = true;
        } else {
            multipleChoice = false;
            shortAnswer = false;
            checkAllThatApply = false;
            trueFalse = false;
        }
    }
}

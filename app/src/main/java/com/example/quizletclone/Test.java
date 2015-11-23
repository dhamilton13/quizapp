package com.example.quizletclone;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import android.util.Log;

public class Test {
    private boolean isTestDynamic, isMultipleChoice, isShortAnswer, isCheckAllThatApply,
            isTrueFalse, isRandom;
    private String name;
    private ModelViewController mvc;
    private ArrayList<Flashcard> setOfFlashcards;


    Test(String name, boolean isTestDynamic, boolean isShortAnswer,
         boolean isMultipleChoice, boolean isTrueFalse, boolean isCheckAll, boolean isRandom) {
        this.name = name;
        this.isTestDynamic = isTestDynamic;
        this.isShortAnswer = isShortAnswer;
        this.isMultipleChoice = isMultipleChoice;
        this.isTrueFalse = isTrueFalse;
        this.isCheckAllThatApply = isCheckAll;
        this.isRandom = isRandom;
    }

    /* Build a test with proper formatting. The test structure depends on whether the user
        specifies his/her own question types (mc, tf, etc.) or randomized. Possible future
         implementation of choosing individual cards. This method currently only displays
         a title and does not build the test .
     */
    public ArrayList<Flashcard> generateQuestions (ArrayList<Flashcard> fcs){
        setOfFlashcards = new ArrayList<Flashcard>();

        //if (isTestDynamic) {
            //do something, this will be implemented later
       // } else {
            for (int i = 0; i < fcs.size(); ++i) {
                if(isRandom)
                {
                    Random myRandom = new Random();
                    int addOrNot = myRandom.nextInt() % 2;
                    if(addOrNot == 1)
                    {
                        setOfFlashcards.add(fcs.get(i));
                        Log.w("dylans test", "should have added a card");
                    }
                }
                else {
                    String category = fcs.get(i).getCategory();
                    if (isShortAnswer && category.equals(ShortAnswer.CATEGORY)) {
                        setOfFlashcards.add(fcs.get(i));
                    } else if (isMultipleChoice && category.equals(MultipleChoice.CATEGORY)) {
                        setOfFlashcards.add(fcs.get(i));
                    } else if (isTrueFalse && category.equals(TrueFalse.CATEGORY)) {
                        setOfFlashcards.add(fcs.get(i));
                    } else if (isCheckAllThatApply && category.equals(CheckAllThatApply.CATEGORY)) {
                        setOfFlashcards.add(fcs.get(i));
                    }
                }
            }
        //}
    return setOfFlashcards;
    }


    public String getName() { return this.name; }

    public ArrayList<Flashcard> getSetOfFlashcards(){ return this.setOfFlashcards; }

    public void setFlashcards(ArrayList<Flashcard> fcs) {
        setOfFlashcards = fcs;
    }

}

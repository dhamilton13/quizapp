package com.example.quizletclone;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;

public class Test {
    private boolean isTestDynamic, isMultipleChoice, isShortAnswer, isCheckAllThatApply,
            isTrueFalse;
    private String name;
    private ModelViewController mvc;
    private ArrayList<Flashcard> setOfFlashcards;


    Test(String name, boolean isTestDynamic, boolean isShortAnswer,
         boolean isMultipleChoice, boolean isTrueFalse, boolean isCheckAll) {
        this.name = name;
        this.isTestDynamic = isTestDynamic;
        this.isShortAnswer = isShortAnswer;
        this.isMultipleChoice = isMultipleChoice;
        this.isTrueFalse = isTrueFalse;
        this.isCheckAllThatApply = isCheckAll;
    }

    /* Build a test with proper formatting. The test structure depends on whether the user
        specifies his/her own question types (mc, tf, etc.) or randomized. Possible future
         implementation of choosing individual cards. This method currently only displays
         a title and does not build the test .
     */
    public ArrayList<Flashcard> generateQuestions (ArrayList<Flashcard> fcs){
        setOfFlashcards = new ArrayList<Flashcard>();

        if (isTestDynamic) {
            //do something
        } else {
            for (int i = 0; i < fcs.size(); ++i) {
                String category = fcs.get(i).getCategory();
                if (isShortAnswer && category.equals(ShortAnswer.CATEGORY)) {
                    setOfFlashcards.add(fcs.get(i));
                } else if (isMultipleChoice && category.equals(MultipleChoice.CATEGORY)) {
                    setOfFlashcards.add(fcs.get(i));
                } else if (isTrueFalse && category.equals(TrueFalse.CATEGORY)) {
                    setOfFlashcards.add(fcs.get(i));
                } else if (isCheckAllThatApply && category.equals(CheckAllThatApply.CATEGORY)){
                    setOfFlashcards.add(fcs.get(i));
                }
            }
        } return setOfFlashcards;
    }


    public String getName() { return this.name; }

}

package com.example.quizletclone;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import android.util.Log;
import java.util.Collections;

public class Test {
    private boolean isTestDynamic, isMultipleChoice, isShortAnswer, isCheckAllThatApply,
            isTrueFalse, isRandom;
    private String name;
    private ModelViewController mvc;
    private ArrayList<Flashcard> setOfFlashcards;
    private ArrayList<Flashcard> saCards, mcCards, tfCards, caCards, raCards;
    private int numSA, numMC, numTF, numCA, numRA;

    Test(String name, boolean isTestDynamic, boolean isShortAnswer,
         boolean isMultipleChoice, boolean isTrueFalse, boolean isCheckAll, boolean isRandom,
         int numSA, int numMC, int numTF, int numCA, int numRA) {
        this.name = name;
        this.isTestDynamic = isTestDynamic;
        this.isShortAnswer = isShortAnswer;
        this.isMultipleChoice = isMultipleChoice;
        this.isTrueFalse = isTrueFalse;
        this.isCheckAllThatApply = isCheckAll;
        this.isRandom = isRandom;
        this.numSA = numSA;
        this.numMC = numMC;
        this.numTF = numTF;
        this.numCA = numCA;
        this.numRA = numRA;
    }

    /* Build a test with proper formatting. The test structure depends on whether the user
        specifies his/her own question types (mc, tf, etc.) or randomized. Possible future
         implementation of choosing individual cards. This method currently only displays
         a title and does not build the test .
     */
    public ArrayList<Flashcard> generateQuestions (ArrayList<Flashcard> fcs){
        setOfFlashcards = new ArrayList<Flashcard>();
        saCards = new ArrayList<Flashcard>();
        mcCards = new ArrayList<Flashcard>();
        tfCards = new ArrayList<Flashcard>();
        caCards = new ArrayList<Flashcard>();
        raCards = new ArrayList<Flashcard>();

        //if (isTestDynamic) {
            //do something, this will be implemented later
       // } else {



        //intialize arraylists of all types to be included
        for (int i = 0; i < fcs.size(); ++i) {
            String category = fcs.get(i).getCategory();
            if (isShortAnswer && category.equals(ShortAnswer.CATEGORY)) {
                saCards.add(fcs.get(i));
            } else if (isMultipleChoice && category.equals(MultipleChoice.CATEGORY)) {
                mcCards.add(fcs.get(i));
            } else if (isTrueFalse && category.equals(TrueFalse.CATEGORY)) {
                tfCards.add(fcs.get(i));
            } else if (isCheckAllThatApply && category.equals(CheckAllThatApply.CATEGORY)) {
                caCards.add(fcs.get(i));
            }
        }

        //if randomized is selected with an ammount
        if(isRandom && numRA != 0)
        {
            //add all to the raCards
            for(int i = 0; i< saCards.size(); i++)
            {
                raCards.add(saCards.get(i));
            }
            for(int i = 0; i< mcCards.size(); i++)
            {
                raCards.add(mcCards.get(i));
            }
            for(int i = 0; i< tfCards.size(); i++)
            {
                raCards.add(tfCards.get(i));
            }
            for(int i = 0; i< caCards.size(); i++)
            {
                raCards.add(caCards.get(i));
            }

            //shuffle the raCards
            raCards = shuffleCards(raCards);

            //add the cards
            addCards(setOfFlashcards, raCards, numRA);
        }
        //if randomized is selected without an ammount, so ammount is in each of the sections
        else if(isRandom)
        {
            //shuffle each type of cards
            if(isShortAnswer)
                saCards = shuffleCards(saCards);
            if(isMultipleChoice)
                mcCards = shuffleCards(mcCards);
            if(isTrueFalse)
                tfCards = shuffleCards(tfCards);
            if(isCheckAllThatApply)
                caCards = shuffleCards(caCards);

            //and then add ammount of each type to raCards shuffle again and then add all of them
            if(isShortAnswer)
                addCards(raCards, saCards, numSA);
            if(isMultipleChoice)
                addCards(raCards, mcCards, numMC);
            if(isTrueFalse)
                addCards(raCards, tfCards, numTF);
            if(isCheckAllThatApply)
                addCards(raCards, caCards, numCA);

            //shuffle raCards
            raCards = shuffleCards(raCards);

            //add them all
            addCards(setOfFlashcards, raCards, raCards.size());

        }
        //randomized is not selected
        else
        {
            //add ammount of each type without shuffling
            if(isShortAnswer)
                if(numSA == 0)
                    numSA = saCards.size();
                addCards(setOfFlashcards, saCards, numSA);
            if(isMultipleChoice)
                if(numMC == 0)
                    numMC = mcCards.size();
                addCards(setOfFlashcards, mcCards, numMC);
            if(isTrueFalse)
                if(numTF == 0)
                    numTF = tfCards.size();
                addCards(setOfFlashcards, tfCards, numTF);
            if(isCheckAllThatApply)
                if(numCA == 0)
                    numCA = caCards.size();
                addCards(setOfFlashcards, caCards, numCA);

        }

        return setOfFlashcards;
    }

    public void addCards(ArrayList<Flashcard> setOfFlashcards, ArrayList<Flashcard> fcs, int numToAdd)
    {
        int counter = 0;
        while(counter < numToAdd && counter < fcs.size())
        {
            setOfFlashcards.add(fcs.get(counter));
            counter++;
        }
    }

    public void populateSetOfFlashcardsManually(ArrayList<Flashcard> fcs) {
        this.setOfFlashcards = new ArrayList<Flashcard>();

        for (int i = 0; i < fcs.size(); ++i) {
            this.setOfFlashcards.add(fcs.get(i));
        }
    }
    public ArrayList<Flashcard> shuffleCards(ArrayList<Flashcard> fcs)
    {
        Collections.shuffle(fcs);
        return fcs;
    }


    public String getName() { return this.name; }

    public ArrayList<Flashcard> getSetOfFlashcards(){ return this.setOfFlashcards; }

    public void setFlashcards(ArrayList<Flashcard> fcs) {
        setOfFlashcards = fcs;
    }

}

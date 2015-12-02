package com.example.quizletclone;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import android.database.Cursor;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ModelViewController {
	private ArrayList<Flashcard> setOfFlashcards;
	private ArrayList<Test> setOfTests;
	private ArrayList<String> setOfTag;
	static ApplicationDatabase database;
	private static ModelViewController mvc = new ModelViewController();
	private boolean fcsAreLoaded = false, testsAreLoaded = false, tagAreLoaded = false;

	//when you click a card in a list, the recyclerView only record and pass the position on itself
	//we want to call a postion on a sorted list of card instead of all the card from database
	//to store a list of card that sorted by the tag
	public ArrayList<Flashcard> sortedCard;
	//notify the FlashcardActivity whether it should adopt a sortedCard or all the card from database
	public boolean isTag=false;

	//these two is for viewing flash card by tag, I store them outside of activity so that a refresh won't change them
	public boolean sortByTag = false;
	public String tag_option;

	//these two variables is for viewing flash card by categories, same purpose to store them here as above
	public boolean sortByCategory = false;
	public String category_option;


	private ModelViewController() {
		setOfFlashcards = new ArrayList<Flashcard>();
		setOfTests = new ArrayList<Test>();
		setOfTag = new ArrayList<String>();
		sortedCard = new ArrayList<Flashcard>();
	}

	public static ModelViewController getInstance(Context context) {
		database = new ApplicationDatabase(context);
		return mvc;
	}

	//TODO: Thi is a note, Kevin switch the position of category and tag in this function
	/** Create a flashcard and store into SQLite database */
	public boolean createFlashcard(String question, String answer, ArrayList<String> listOfAnswers,
								   String category, String tag) {
		Flashcard fc = new Flashcard(question, answer, listOfAnswers, category, tag);
		setOfFlashcards.add(fc);

		Gson gson = new Gson();
		String answerList = gson.toJson(fc.getListOfAnswers());
		return database.insertFlashcardData(question, answer, answerList, category, tag);
	}


	/** Create a new Test object and store into SQLite database */
	public boolean createTest(String nameOfTest, boolean isDynamic, boolean isShortAnswer,
						   boolean isMultipleChoice, boolean isTrueFalse, boolean isCheckAll, boolean isRandom,
							int numSA, int numMC, int numTF, int numCA, int numRA) {

		Test test = new Test(nameOfTest, isDynamic, isShortAnswer, isMultipleChoice, isTrueFalse, isCheckAll, isRandom,
						numSA, numMC, numTF, numCA, numRA);

		ArrayList<Flashcard> fcs = test.generateQuestions(setOfFlashcards);
		setOfTests.add(test);

		/* Create a gson object to hold the set of flashcards for each test */
		Gson gson = new Gson();
		String flashcards = gson.toJson(fcs);

		return database.insertTestData(nameOfTest, isDynamic, isShortAnswer, isMultipleChoice,
				isTrueFalse, isCheckAll, isRandom, flashcards,
				numSA, numMC, numTF, numCA, numRA);
	}

	/** Create a new Test object and store into SQLite database */
	public boolean createTag(String nameOfTag) {

		boolean success = database.insertTagData(nameOfTag);
		if(success)
			setOfTag.add(nameOfTag);
		return success;
	}

	/** Loads the flashcard data from SQLite database. */
	public void loadFlashcards(Context context) {
		Cursor res = database.getFlashcardData();

		if(fcsAreLoaded)
			setOfFlashcards.clear();

		while(res.moveToNext()) {
			//add arraylist parameter
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>() {}.getType();
			ArrayList<String> answerList = gson.fromJson(res.getString(4), type);

			setOfFlashcards.add(new Flashcard(res.getString(0), res.getString(1), answerList, res.getString(2),
											res.getString(3)));
		}
		fcsAreLoaded = true;
	}

    /** Loads the Test data from SQLite database. */
	public void loadTests(Context context) {
		Cursor res = database.getTestData();

		if(testsAreLoaded)
			setOfTests.clear();

		while(res.moveToNext()) {
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<Flashcard>>() {}.getType();
			ArrayList<Flashcard> cards = gson.fromJson(res.getString(7), type);

			Test test = new Test(res.getString(0), res.getInt(1)!=0, res.getInt(2)!=0,
					res.getInt(3)!=0, res.getInt(4)!=0, res.getInt(5)!=0, res.getInt(6) != 0,
					res.getInt(8), res.getInt(9), res.getInt(10), res.getInt(11), res.getInt(12));

			test.setFlashcards(cards);
			setOfTests.add(test);
		}
		testsAreLoaded = true;
	}

	/** Loads the Tag data from SQLite database. */
	public void loadTag(Context context) {
		Cursor res = database.getTagData();

		if(tagAreLoaded)
			setOfTag.clear();

		while(res.moveToNext()) {
			setOfTag.add(res.getString(0));
		}
		tagAreLoaded = true;
	}

	/* White TestGrader is static and doesn't  need to be in MVC, this allows a central
		point for debugging for nowm - instead of looking around in the code. I might change this
		depending on if I run into any issues. TestGrader's getters are not called in this class.
	 */
	public void prepareGrader(Test test) {
		TestGrader.setTestSize(test.getSetOfFlashcards().size());
		for (int i = 0; i < test.getSetOfFlashcards().size(); i++) {
			TestGrader.addCorrectAnswer(test.getSetOfFlashcards().get(i).getAnswer(), i);
		}
	}

	public void addUserAnswerToGrader(String answer, int index) {
		TestGrader.addUserAnswer(answer, index);
	}


	/* clearGrader() and gradeTest() do not need to be in MVC but are here for consistency  */
	public void clearGrader() {
		TestGrader.clearGrader();
	}
	public void gradeTest() { TestGrader.gradeTest(); }

	// Will be moved in the next iteration, getters for cards and tests
	public List<Flashcard> getFlashcards() {
		return this.setOfFlashcards;
	}

	public List<Test> getTests() { return this.setOfTests; }

	public List<String> getTags() { return this.setOfTag; }

	/**
	 * This function delete the tag specified by str and all the flashCard associated
	 * @param str
	 */
	public void deleteTag(String str)
	{
		database.deleteTag(str);
	}
	public void deleteTest(String str) {database.deleteTest(str); }
	public void deleteFlashCard(String str) {database.deleteFlashCard(str); }


}
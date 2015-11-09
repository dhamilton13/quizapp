package com.example.quizletclone;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import android.database.Cursor;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class ModelViewController {
	private ArrayList<Flashcard> setOfFlashcards;
	private ArrayList<Test> setOfTests;
	static ApplicationDatabase database;
	private static ModelViewController mvc = new ModelViewController();
	private boolean fcsAreLoaded = false, testsAreLoaded = false;


	private ModelViewController() {
		setOfFlashcards = new ArrayList<Flashcard>();
		setOfTests = new ArrayList<Test>();
	}

	public static ModelViewController getInstance(Context context) {
		database = new ApplicationDatabase(context);
		return mvc;
	}


	/** Create a flashcard and store into SQLite database */
	public boolean createFlashcard(String question, String answer, String tag, String category) {
		setOfFlashcards.add(new Flashcard(question, answer, tag, category));
		return database.insertFlashcardData(question, answer, tag, category);
	}

	/** Create a new Test object and add it to the Test ArrayList */
	public boolean createTest(String nameOfTest, boolean isDynamic, boolean isShortAnswer,
						   boolean isMultipleChoice, boolean isTrueFalse, boolean isCheckAll) {
		Test test = new Test(nameOfTest, isDynamic, isShortAnswer, isMultipleChoice, isTrueFalse, isCheckAll);
		ArrayList<Flashcard> fcs = test.generateQuestions(setOfFlashcards);
		setOfTests.add(test);

		Gson gson = new Gson();
		String flashcards = gson.toJson(fcs);

		return database.insertTestData(nameOfTest, isDynamic, isShortAnswer, isMultipleChoice,
				isTrueFalse, isCheckAll, flashcards);
	}

	/** Loads the flashcard data from SQLite database. */
	public void loadFlashcards(Context context) {
		Cursor res = database.getFlashcardData();

		if(fcsAreLoaded)
			setOfFlashcards.clear();

		while(res.moveToNext()) {
			setOfFlashcards.add(new Flashcard(res.getString(0), res.getString(1), res.getString(2),
											res.getString(3)));

		}

		fcsAreLoaded = true;
	}

	public void loadTests(Context context) {
		Cursor res = database.getTestData();

		if(testsAreLoaded)
			setOfTests.clear();

		while(res.moveToNext()) {
			Gson gson = new Gson();

			Type type = new TypeToken<ArrayList<Flashcard>>() {
			}.getType();
			ArrayList<Flashcard> cards = gson.fromJson(res.getString(6), type);

			Log.v("ARRAYLIST SIZE", String.valueOf(cards.size()));

			Test test = new Test(res.getString(0), res.getInt(1)!=0, res.getInt(2)!=0,
					res.getInt(3)!=0, res.getInt(4)!=0, res.getInt(5)!=0);
			test.setFlashcards(cards);
			setOfTests.add(test);
		}

		testsAreLoaded = true;
	}



	// Will be moved in the next iteration, getters for cards and tests
	public List<Flashcard> getFlashcards() {
		return this.setOfFlashcards;
	}

	public List<Test> getTests() { return this.setOfTests; }


}
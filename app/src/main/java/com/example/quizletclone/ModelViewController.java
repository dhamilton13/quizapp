package com.example.quizletclone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.content.Context;


public class ModelViewController {
	//private List<Room> rooms;
	private ArrayList<Flashcard> setOfFlashcards; //temp
	private ArrayList<Test> setOfTests;
	private String FILENAME = "flashcard_data";
	static ApplicationDatabase database;
	private static ModelViewController mvc = new ModelViewController();


	private ModelViewController() {
		setOfFlashcards = new ArrayList<Flashcard>();
		setOfTests = new ArrayList<Test>();
	}

	public static ModelViewController getInstance(Context context) {
		database = new ApplicationDatabase(context);
		return mvc;
	}



	public void createRoom() {
		/* bypass for now, directly create flashcard instead
		see createFlashcard(); */
	}

	/* createFlashcard() will be moved to the Room class */

	/* For the first iteration, since we are not implementing
	 * rooms, flashcards will be created directly
	 */
	public boolean createFlashcard(String question, String answer, String tag, String category) {
		//This method will be removed during the second iteration
		return database.insertData(question, answer, tag, category);
		//setOfFlashcards.add(fc);
	}

	/* Create a new Test object and add it to the Test ArrayList */
	public void createTest(String nameOfTest, boolean testType) {
		Test test = new Test(nameOfTest, testType);
		test.generateTest(setOfFlashcards);
		setOfTests.add(test);
	}

	// Ignore for now, for testing purposes
	public void deleteFlashcard(int position) {
		setOfFlashcards.remove(position);
	}

	/**
	 * Loads the flashcard data from internal phone memory.
	 */
	public void loadFlashcards(Context context) {
		Cursor res = database.getData();

		while(res.moveToNext()) {
			setOfFlashcards.add(new Flashcard(res.getString(0), res.getString(1), res.getString(2),
											res.getString(3)));
		}
	}

	/**
	 * Stores the flashcard data to internal phone memory by writing the
	 * data to a text file.
	 */
	public void storeFlashcards(Context context) {
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			//TODO:Log error message
			e.printStackTrace();
		}


		try {
			for (int i = 0; i < setOfFlashcards.size(); ++i) {
				fos.write(setOfFlashcards.get(i).getQuestion().getBytes());
				fos.write(System.getProperty("line.separator").getBytes());
				fos.write(setOfFlashcards.get(i).getAnswer().getBytes());
				fos.write(System.getProperty("line.separator").getBytes());
				fos.flush();
			} fos.close();

		} catch (IOException e) {
			//TODO:Log error message
			e.printStackTrace();
		}
	}

	// Will be moved in the next iteration, getters for cards and tests
	public List<Flashcard> getFlashcards() {
		return this.setOfFlashcards;
	}

	public List<Test> getTests() { return this.setOfTests; }


}
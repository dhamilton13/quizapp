package com.example.quizletclone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ModelViewController implements Parcelable {
	//private List<Room> rooms;
	private ArrayList<Flashcard> setOfFlashcards; //temp
	private ArrayList<Test> setOfTests;
	private String FILENAME = "flashcard_data";

	ModelViewController() {
		//this.rooms = new ArrayList<QuizletRoom>();
		this.setOfFlashcards = new ArrayList<Flashcard>();
		this.setOfTests = new ArrayList<Test>();
	}

	public void createRoom() {
		/* bypass for now, directly create flashcard instead
		see createFlashcard(); */
	}

	/* createFlashcard() will be moved to the Room class */

	/* For the first iteration, since we are not implementing
	 * rooms, flashcards will be created directly
	 */
	public void createFlashcard(String question, String answer,String category) {
		//This method will be removed during the second iteration
		Flashcard fc = new Flashcard(question, answer, category);
		if (question.contains("E.")) {
			fc.setTag("CheckAllThatApply");
		} else if (question.contains("A. True")) {
			fc.setTag("TrueFalse");
		} else if (question.contains("D.")) {
			fc.setTag("MultipleChoice");
		} else {
			fc.setTag("ShortAnswer");
		}

		setOfFlashcards.add(fc);
	}

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
		File file = context.getFileStreamPath(FILENAME);

		if (!file.exists()) {
			return;
		}


		File fileDirectory = context.getFilesDir();
		try {
			Scanner scan = new Scanner(new File(fileDirectory, FILENAME));
			scan.useDelimiter("\r\n");

			int i = 0;
			String question = null;
			String answer = null;
			while(scan.hasNextLine()) {
				if (i%2 == 0) {
					question = scan.nextLine();
				} else {
					answer = scan.nextLine();
				}

				if (question != null & answer != null) {
					createFlashcard(question, answer, "UCSD");
					Log.v("Questions:", question + " " + answer);

					question = null;
					answer = null;
				}
				i++;
			}
			scan.close();
		} catch (FileNotFoundException e) {
			//TODO:Log error message
			e.printStackTrace();
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

	// Will be moved in the second iteration
	public List<Flashcard> getFlashcards() {
		return this.setOfFlashcards;
	}

	public List<Test> getTests() { return this.setOfTests; }

	protected ModelViewController(Parcel in) {
		if (in.readByte() == 0x01) {
			setOfFlashcards = new ArrayList<Flashcard>();
			in.readList(setOfFlashcards, Flashcard.class.getClassLoader());
		} else {
			setOfFlashcards = null;
		}
		if (in.readByte() == 0x01) {
			setOfTests = new ArrayList<Test>();
			in.readList(setOfTests, Test.class.getClassLoader());
		} else {
			setOfTests = null;
		}
		FILENAME = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (setOfFlashcards == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(setOfFlashcards);
		}
		if (setOfTests == null) {
			dest.writeByte((byte) (0x00));
		} else {
			dest.writeByte((byte) (0x01));
			dest.writeList(setOfTests);
		}
		dest.writeString(FILENAME);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<ModelViewController> CREATOR = new Parcelable.Creator<ModelViewController>() {
		@Override
		public ModelViewController createFromParcel(Parcel in) {
			return new ModelViewController(in);
		}

		@Override
		public ModelViewController[] newArray(int size) {
			return new ModelViewController[size];
		}
	};
}
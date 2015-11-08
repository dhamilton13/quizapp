package com.example.quizletclone;

import android.os.Parcel;
import android.os.Parcelable;

public class Flashcard {
	private String question, answer, category, tag;
	
	Flashcard(String question, String answer, String category, String tag) {
		this.question = question;
		this.answer = answer;
		this.category = category;
        this.tag = tag;
	}

    /* Getters for retrieving flashcard data */
	public String getQuestion() {
		return this.question;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public String getCategory() { return this.category; }

    public String getTag() { return this.tag; }

}

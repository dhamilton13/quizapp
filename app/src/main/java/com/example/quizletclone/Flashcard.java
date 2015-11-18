package com.example.quizletclone;

import java.util.ArrayList;

public class Flashcard {
	private String question, answer, category, tag;
	private ArrayList<String> listOfAnswers;
	
	Flashcard(String question, String answer, ArrayList<String> listOfAnswers, String category, String tag) {
		this.question = question;
		this.answer = answer;
		this.listOfAnswers = listOfAnswers;
		this.category = category;
        this.tag = tag;
	}

    /* Getters for retrieving flashcard data */
	public String getQuestion() {
		return this.question;
	}

	public String getAnswer() { return this.answer; }
	public ArrayList<String> getListOfAnswers() { return this.listOfAnswers; }
	
	public String getCategory() { return this.category; }

    public String getTag() { return this.tag; }

}

package com.example.quizletclone;
import java.util.List;
import java.util.ArrayList;

public class Room {
	private String roomName;
	private List<Flashcard> setOfFlashcards = new ArrayList<Flashcard>();
	
	Room(String roomName){
		this.roomName = roomName;
	}
	
	public void createFlashcard(String question, String answer, String category) {
		Flashcard fc = new Flashcard(question, answer, category);
		setOfFlashcards.add(fc);
	}
	
	public void deleteFlashcard(String question) {
		setOfFlashcards.remove(question);
	}
	
	public void deleteAllFlashcards() {
		setOfFlashcards.clear();
	}	
}

package com.example.quizletclone;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	private ModelViewController mvc;
	private Button FAB;
	public final static String TAG = "tag name";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getWindow().getDecorView().setBackgroundColor(Color.BLUE);
		setTitle("Quiz App"); //Can be modified to match APP name
		mvc = ModelViewController.getInstance(this);
		mvc.loadTests(this);
		mvc.loadFlashcards(this);
		mvc.createTag("root");
		mvc.loadTag(this);
	}

	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	/**
	 * Transfers ownership to the ListActivity class when the
	 * user presses "View Flashcards".
	 */
	public void goToViewFlashcards(View view) {
		mvc.sortByTag = false;
		mvc.sortByCategory = false;
		Intent intent = new Intent(this, ListActivity.class);
		intent.putExtra("isSorted", false);
		startActivity(intent);
	}

	public void goToCreateTest(View view) {
		Intent intent = new Intent(this, TestListActivity.class);
		startActivity(intent);
	}

	/**
	 * Go to Tag List when user press "Create Tag" button
	 */
	public void createTag(View view) {
		Intent intent = new Intent(this, CreateTag.class);
		startActivity(intent);
	}

	
}

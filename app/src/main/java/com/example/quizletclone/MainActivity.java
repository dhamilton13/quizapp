package com.example.quizletclone;

import android.support.v7.app.ActionBarActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	private ModelViewController mvc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Quiz App"); //Can be modified to match APP name
		mvc = ModelViewController.getInstance(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		/* Store flashcard data anytime app is temporarily 
		 exited */
		//mvc.storeFlashcards(getApplicationContext());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		/* Store flashcard data when app is exited */
		//mvc.storeFlashcards(getApplicationContext());
	}
	
	/**
	 * Transfers ownership to the ListActivity class when the
	 * user presses "View Flashcards".
	 */
	public void goToViewFlashcards(View view) {
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
	}

	public void goToCreateTest(View view) {
		Intent intent = new Intent(this, ListTest.class);
		startActivity(intent);
	}


	

	
	
}

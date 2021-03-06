package com.example.quizletclone;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

public class ShortAnswer extends ActionBarActivity {
	private Button createQuestion;
	private EditText questionField, answerField;
	private ModelViewController mvc;
	private Spinner spinner;
	public static final String CATEGORY = "Short Answer";
	private RelativeLayout layout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		mvc = ModelViewController.getInstance(this);

		setTitle("Short answer");
		initializeGUIComponents();
		createFlashcardObject();
	}

	protected void hideKeyboard(View view) {
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/* The below methods save flashcard data whenever the activity is paused, or terminated */
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	private void initializeGUIComponents() {
		/* Initialize all the graphical user interface elements. */
		layout = (RelativeLayout)findViewById(R.id.layout);
		createQuestion = (Button)findViewById(R.id.createFlashcard);
		questionField = (EditText)findViewById(R.id.questionField);
		answerField = (EditText)findViewById(R.id.answerTextField);

		/* Initialize the spinner, create an array adapter that stores array data from
            res/strings.xml. All array modifications are performed in that file. After
            creating the adapter to represent the array objects, specify the layout type
            for the spinner.
         */
		spinner = (Spinner)findViewById(R.id.spinner);

		//make spinner consist of the available tag
		List<String> spinnerItem = mvc.getTags();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				hideKeyboard(view);
				return false;
			}
		});
	}

	/* Creates a question when the user presses the 'Create
 		Question button */
	private void createFlashcardObject() {
		createQuestion.setOnClickListener(
		    new View.OnClickListener() {
			    public void onClick(View view) {
				    //The flashcard creation
					ArrayList<String> listOfAnswers = new ArrayList<String>();
					listOfAnswers.add(answerField.getText().toString());

					if (answerField.getText().toString().isEmpty() || questionField.getText().toString().isEmpty()
							|| spinner.getSelectedItem().toString().isEmpty())
						return;

				    boolean successfulCreation = mvc.createFlashcard(questionField.getText()
										.toString(), answerField.getText().toString(), listOfAnswers,
										CATEGORY, spinner.getSelectedItem().toString());
					questionField.setText("");
					answerField.setText("");

					/* Acknowledge the card was created by using a Toast object to display a
                            message.
                        */
					if (successfulCreation ) {
						Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created",
								Toast.LENGTH_SHORT);
							toast.show();

					/* After a 1500 ms delay, return to the list of flashcards */
					new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
                        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
						startActivity(intent);
						finish();
						}
							}, 1500);
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
                                "Error creating flashcard", Toast.LENGTH_SHORT);
						toast.show();
					}

				}
			});
	}
}

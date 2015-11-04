package com.example.quizletclone;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class ShortAnswer extends ActionBarActivity {
	private Button createQuestion;
	private EditText questionField, answerField;
	private ModelViewController mvc;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		Intent intent = getIntent();

		/* Retrieve the ModelViewController object from the calling class */
		mvc = intent.getExtras().getParcelable("MVCObj");
		setTitle("Short answer");

		/* Initialize all the graphical user interface elements. */
		createQuestion = (Button)findViewById(R.id.createFlashcard);
		questionField = (EditText)findViewById(R.id.questionField);
		answerField = (EditText)findViewById(R.id.answerTextField);

		/* Initialize the spinner, create an array adapter that stores array data from
            res/strings.xml. All array modifications are performed in that file. After
            creating the adapter to represent the array objects, specify the layout type
            for the spinner.
         */
		spinner = (Spinner)findViewById(R.id.spinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		/* Creates a question when the user presses the 'Create
		 Question button */
		createQuestion.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View view) {
						//The flashcard creation
						mvc.createFlashcard(questionField.getText().toString(),
								answerField.getText().toString(), "UCSD");
						questionField.setText("");
						answerField.setText("");

					/* Acknowledge the card was created by using a Toast object to display a
                            message.
                        */
						Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created", Toast.LENGTH_SHORT);
						//TODO: need a better way of calling toast (instead of creating an object everytime).
						toast.show();

					/* After a 2000 ms delay, return to the list of flashcards */
						new Handler().postDelayed(new Runnable() {
							@Override
						public void run() {
								Intent intent = new Intent(getApplicationContext(),ListActivity.class);
								intent.putExtra("MVCObj", mvc);
								startActivity(intent);
								finish();
							}
						}, 2000);

					}
				});		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create, menu);
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


	/* The below methods save flashcard data whenever the activity is paused, or terminated */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		mvc.storeFlashcards(getApplicationContext());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mvc.storeFlashcards(getApplicationContext());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mvc.storeFlashcards(getApplicationContext());
	}

}

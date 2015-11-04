package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MultipleChoice extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private RadioButton answerFieldA, answerFieldB, answerFieldC, answerFieldD, answerFieldE;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        /* Retrieve the ModelViewController object from the calling class */
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("Multiple choice");

        /* Initialize all the graphical user interface elements. */
        createQuestion = (Button)findViewById(R.id.createFlashcard);
        questionField = (EditText)findViewById(R.id.questionField);
        answerFieldA = (RadioButton)findViewById(R.id.answerTrue);
        answerFieldB = (RadioButton)findViewById(R.id.answerFalse);
        answerFieldC = (RadioButton)findViewById(R.id.answerC);
        answerFieldD = (RadioButton)findViewById(R.id.answerD);
        answerFieldE = (RadioButton)findViewById(R.id.answerE);

        fieldA = (EditText)findViewById(R.id.fieldA);
        fieldB = (EditText)findViewById(R.id.fieldB);
        fieldC = (EditText)findViewById(R.id.fieldC);
        fieldD = (EditText)findViewById(R.id.fieldD);
        fieldE = (EditText)findViewById(R.id.fieldE);

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


        /* When the user clicks create question, create a flashcard using the ModelViewController
            object.
         */
        createQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                                if (answerFieldA.isChecked()) {
                                    //TODO: Find a better way of creating these cards
                                    mvc.createFlashcard(questionField.getText().toString() + " A. "
                                                    + fieldA.getText().toString() + " B. "
                                                    + fieldB.getText().toString() + " C. "
                                                    + fieldC.getText().toString() + " D. "
                                                    + fieldD.getText().toString() + " E. "
                                                    + fieldE.getText().toString(),
                                            fieldA.getText().toString(), "UCSD");
                                    answerFieldA.setChecked(false);
                                } else if (answerFieldB.isChecked()) {
                                    mvc.createFlashcard(questionField.getText().toString() + " A. "
                                                    + fieldA.getText().toString() + " B. "
                                                    + fieldB.getText().toString() + " C. "
                                                    + fieldC.getText().toString() + " D. "
                                                    + fieldD.getText().toString() + " E. "
                                                    + fieldE.getText().toString(),
                                            fieldB.getText().toString(), "UCSD");
                                    answerFieldB.setChecked(false);
                                } else if (answerFieldC.isChecked()) {
                                    mvc.createFlashcard(questionField.getText().toString() + " A. "
                                                    + fieldA.getText().toString() + " B. "
                                                    + fieldB.getText().toString() + " C. "
                                                    + fieldC.getText().toString() + " D. "
                                                    + fieldD.getText().toString() + " E. "
                                                    + fieldE.getText().toString(),
                                            fieldC.getText().toString(), "UCSD");
                                    answerFieldC.setChecked(false);
                                } else {
                                    mvc.createFlashcard(questionField.getText().toString() + " A. "
                                                    + fieldA.getText().toString() + " B. "
                                                    + fieldB.getText().toString() + " C. "
                                                    + fieldC.getText().toString() + " D. "
                                                    + fieldD.getText().toString() + " E. "
                                                    + fieldE.getText().toString(),
                                            fieldD.getText().toString(), "UCSD");
                                    answerFieldD.setChecked(false);
                                }
                                questionField.setText("");
                                fieldA.setText("");
                                fieldB.setText("");
                                fieldC.setText("");
                                fieldD.setText("");

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

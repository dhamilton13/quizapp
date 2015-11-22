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
import java.util.ArrayList;
import java.util.List;

public class MultipleChoice extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private RadioButton answerFieldA, answerFieldB, answerFieldC, answerFieldD, answerFieldE;
    private Spinner spinner;
    public static final String CATEGORY = "Multiple Choice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        setTitle("Multiple Choice");
        initializeGUIComponents();
        createFlashcardObject();
    }

    /* The below methods save flashcard data whenever the activity is paused, or terminated */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mvc.storeFlashcards(getApplicationContext());
    }

    private void initializeGUIComponents() {
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

        //make spinner consist of the available tag
        List<String> spinnerItem = mvc.getTags();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    /* When the user clicks create question, create a flashcard using the ModelViewController
        object.
    */
    private void createFlashcardObject() {
        createQuestion.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    boolean successfulCreation;
                    ArrayList<String> listOfAnswers = new ArrayList<String>();
                    listOfAnswers.add(fieldA.getText().toString());
                    listOfAnswers.add(fieldB.getText().toString());
                    listOfAnswers.add(fieldC.getText().toString());
                    listOfAnswers.add(fieldD.getText().toString());
                    listOfAnswers.add(fieldE.getText().toString());

                    if (answerFieldA.isChecked()) {
                            //TODO: Find a better way of creating these cards
                        successfulCreation = mvc.createFlashcard(questionField.getText().toString(),
                                fieldA.getText().toString(), listOfAnswers,
                                        CATEGORY, spinner.getSelectedItem().toString());
                            answerFieldA.setChecked(false);
                        } else if (answerFieldB.isChecked()) {
                            successfulCreation = mvc.createFlashcard(questionField.getText().
                                        toString(), fieldB.getText().toString(), listOfAnswers,
                                        CATEGORY, spinner.getSelectedItem().toString());
                            answerFieldB.setChecked(false);
                        } else if (answerFieldC.isChecked()) {
                            successfulCreation = mvc.createFlashcard(questionField.getText().
                                        toString(), fieldC.getText().toString(), listOfAnswers,
                                        CATEGORY, spinner.getSelectedItem().toString());
                            answerFieldC.setChecked(false);
                        } else if (answerFieldD.isChecked()){
                            successfulCreation = mvc.createFlashcard(questionField.getText().
                                        toString(), fieldD.getText().toString(), listOfAnswers,
                                    CATEGORY, spinner.getSelectedItem().toString());
                            answerFieldD.setChecked(false);
                        } else {
                        successfulCreation = mvc.createFlashcard(questionField.getText().
                                        toString(), fieldE.getText().toString(), listOfAnswers,
                                CATEGORY, spinner.getSelectedItem().toString());
                            answerFieldE.setChecked(false);
                        }
                        questionField.setText("");
                        fieldA.setText("");
                        fieldB.setText("");
                        fieldC.setText("");
                        fieldD.setText("");
                        fieldE.setText("");

                        /* Acknowledge the card was created by using a Toast object to display a
                            message.
                        */

                    if (successfulCreation) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created", Toast.LENGTH_SHORT);
                        //TODO: need a better way of calling toast (instead of creating an object everytime).
                            toast.show();

                        /* After a 2000 ms delay, return to the list of flashcards */
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 2000);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Error creating flashcard", Toast.LENGTH_SHORT);
                            //TODO: need a better way of calling toast (instead of creating an object everytime).
                            toast.show();
                        }
                    }
                });
    }

}

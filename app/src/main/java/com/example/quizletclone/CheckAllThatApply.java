package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CheckAllThatApply extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private CheckBox checkBoxA, checkBoxB, checkBoxC, checkBoxD, checkBoxE;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_that_apply);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        setTitle("Check all that apply");

        initializeGUIComponents();
        createFlashcardObject();
    }

    /* The below methods save flashcard data whenever the activity is paused, or terminated */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mvc.storeFlashcards(getApplicationContext());
    }

    /* Initialize all the graphical user interface elements. */
    private void initializeGUIComponents() {
        createQuestion = (Button)findViewById(R.id.createFlashcard);
        questionField = (EditText)findViewById(R.id.questionField);
        checkBoxA = (CheckBox)findViewById(R.id.checkboxA);
        checkBoxB = (CheckBox)findViewById(R.id.checkboxB);
        checkBoxC = (CheckBox)findViewById(R.id.checkboxC);
        checkBoxD = (CheckBox)findViewById(R.id.checkboxD);
        checkBoxE = (CheckBox)findViewById(R.id.checkboxE);

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
    }

    /* When the user clicks create question, create a flashcard using the ModelViewController
        object.
    */
    private void createFlashcardObject() {
        createQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String answer = "";

                        if (checkBoxA.isChecked()) {
                            answer += "A. " + fieldA + " ";
                        }

                        if (checkBoxB.isChecked()) {
                            answer += "B. " + fieldB + " ";
                        }
                        if (checkBoxC.isChecked()) {
                            answer += "C. " + fieldC + " ";
                        }
                        if (checkBoxD.isChecked()) {
                            answer += "D. " + fieldD + " ";
                        }
                        if (checkBoxE.isChecked()) {
                            answer += "E. " + fieldE + " ";
                        }

                        //The flashcard creation
                        boolean successfulCreation = mvc.createFlashcard(
                                questionField.getText().toString() + " "
                                        + fieldA.getText().toString() + " "
                                        + fieldB.getText().toString() + " "
                                        + fieldC.getText().toString() + " "
                                        + fieldD.getText().toString() + " "
                                        + fieldE.getText().toString(), answer, "Test", "UCSD");

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
                            Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created",
                                    Toast.LENGTH_SHORT);
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
                            Toast toast = Toast.makeText(getApplicationContext(), "Error creating flashcard",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        //TODO: need a better way of calling toast (instead of creating an object everytime)
                    }
                });

    }
}

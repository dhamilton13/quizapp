package com.example.quizletclone;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class TrueFalse extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField;
    private TextView trueField, falseField;
    private RadioButton answerFieldT, answerFieldF;
    private RadioGroup group;
    private Spinner spinner;
    private RelativeLayout layout;
    public static final String CATEGORY = "True Or False";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);

        /* Retrieve the ModelViewController instance */
        mvc = ModelViewController.getInstance(this);
        mvc.loadTag(this);

        setTitle("True or false");
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
        //mvc.updateFlashcards(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mvc.updateFlashcards(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mvc.updateFlashcards(this);
    }

    private void initializeGUIComponents() {
        /* Initialize all the graphical user interface elements. */
        layout = (RelativeLayout)findViewById(R.id.layout);
        createQuestion = (Button)findViewById(R.id.createFlashcard);
        questionField = (EditText)findViewById(R.id.questionField);
        trueField = (TextView)findViewById(R.id.trueField);
        falseField = (TextView)findViewById(R.id.falseField);
        answerFieldT = (RadioButton)findViewById(R.id.answerTrue);
        answerFieldF = (RadioButton)findViewById(R.id.answerFalse);

        /* Initialize the spinner, create an array adapter that stores array data from
            res/strings.xml. All array modifications are performed in that file. After
            creating the adapter to represent the array objects, specify the layout type
            for the spinner.
         */
        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> spinnerItem = mvc.getTags();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        createListeners();
    }

    private void createListeners() {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                hideKeyboard(view);
                return false;
            }
        });

        answerFieldT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (answerFieldT.isChecked())
                    answerFieldF.setChecked(false);
            }
        });

        answerFieldF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (answerFieldF.isChecked())
                    answerFieldT.setChecked(false);
            }
        });
    }

    private void createFlashcardObject() {
        /* When the user clicks create question, create a flashcard using the ModelViewController
            object.
         */
        createQuestion.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                        //The flashcard creation
                    boolean successfulInsertion;
                    ArrayList<String> listOfAnswers = new ArrayList<String>();
                    listOfAnswers.add("True");
                    listOfAnswers.add("False");

                    if (questionField.getText().toString().isEmpty() || spinner.getSelectedItem()
                            .toString().isEmpty())
                        return;

                    if (answerFieldT.isChecked()) {
                        successfulInsertion = mvc.createFlashcard(questionField.getText().
                                            toString(),"True", listOfAnswers, CATEGORY,
                                            spinner.getSelectedItem().toString());
                        answerFieldF.setChecked(false);
                    } else {
                        successfulInsertion = mvc.createFlashcard(questionField.getText().
                                            toString(), "False", listOfAnswers, CATEGORY,
                                            spinner.getSelectedItem().toString());
                        answerFieldF.setChecked(false);
                    }

                    questionField.setText("");

                    /* Acknowledge the card was created by using a Toast object to display a
                       message.
                    */
                    if (successfulInsertion) {
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

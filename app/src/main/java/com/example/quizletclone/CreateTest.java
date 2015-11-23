package com.example.quizletclone;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.CheckBox;

public class CreateTest extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText nameField;
    private CheckBox shortAnswer, multipleChoice, trueFalse, checkAll, random;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        setTitle("Create a test");

        initializeGUIComponents();
        createTestObject();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initializeGUIComponents() {
        layout = (RelativeLayout)findViewById(R.id.layout);
        createQuestion = (Button)findViewById(R.id.createTest);
        nameField = (EditText)findViewById(R.id.nameField);
        shortAnswer = (CheckBox)findViewById(R.id.shortAnswerCB);
        multipleChoice = (CheckBox)findViewById(R.id.multipleChoiceCB);
        trueFalse = (CheckBox)findViewById(R.id.trueFalseCB);
        checkAll = (CheckBox)findViewById(R.id.checkAllCB);
        random = (CheckBox)findViewById(R.id.randomCB);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                hideKeyboard(view);
                return false;
            }
        });
    }

    private void createTestObject() {
        createQuestion.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {

                    if (nameField.getText().toString().isEmpty() || (
                            !shortAnswer.isChecked() && !multipleChoice.isChecked()
                                    && !trueFalse.isChecked() && !checkAll.isChecked() &&
                                    !random.isChecked())) {
                        return;
                    }

                    boolean successfulCreation;
                    if (random.isChecked()) {
                        successfulCreation = mvc.createTest(nameField.getText().toString(),
                                true, false, false, false, false, true);
                    } else {
                        successfulCreation = mvc.createTest(nameField.getText().toString(), false,
                                shortAnswer.isChecked(), multipleChoice.isChecked(),
                                trueFalse.isChecked(), checkAll.isChecked(), random.isChecked());
                    }

                    nameField.setText("");

                        /* Acknowledge the test was created by displaying a small message */
                    if (successfulCreation) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Test created",
                                Toast.LENGTH_SHORT);
                        toast.show();


                        /* After a 2000ms delay, return to the list of tests */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), TestListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Failed to create test", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                });
    }
}

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
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CheckBox;
import android.text.TextWatcher;
import android.text.Editable;

public class CreateTest extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText nameField;
    private CheckBox shortAnswer, multipleChoice, trueFalse, checkAll, random;
    private EditText etnumSA, etnumMC, etnumTF, etnumCA, etnumRA;
    private int numSA, numMC, numTF, numCA, numRA;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        setTitle("Create a Quiz");

        initializeGUIComponents();
        createTestObject();
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initializeGUIComponents() {
        layout = (LinearLayout)findViewById(R.id.layout);
        createQuestion = (Button)findViewById(R.id.createTest);
        nameField = (EditText)findViewById(R.id.nameField);
        shortAnswer = (CheckBox)findViewById(R.id.shortAnswerCB);
        multipleChoice = (CheckBox)findViewById(R.id.multipleChoiceCB);
        trueFalse = (CheckBox)findViewById(R.id.trueFalseCB);
        checkAll = (CheckBox)findViewById(R.id.checkAllCB);
        random = (CheckBox)findViewById(R.id.randomCB);
        etnumSA = (EditText)findViewById(R.id.etAmmountSA);
        etnumMC = (EditText)findViewById(R.id.etAmmountMC);
        etnumTF = (EditText)findViewById(R.id.etAmmountTF);
        etnumCA = (EditText)findViewById(R.id.etAmmountCA);
        etnumRA = (EditText)findViewById(R.id.etAmmountRA);



        //these handle enabling and disabling the edit texts to only allow number of types or just number of random
        etnumSA.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etnumSA.getText().toString().matches("") &&
                    etnumMC.getText().toString().matches("")&&
                    etnumTF.getText().toString().matches("")&&
                    etnumRA.getText().toString().matches(""))
                {
                    enableNumRA();
                }
                else
                {
                    disableNumRA();
                    shortAnswer.setChecked(true);
                }
            }

        });

        etnumMC.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etnumSA.getText().toString().matches("") &&
                        etnumMC.getText().toString().matches("")&&
                        etnumTF.getText().toString().matches("")&&
                        etnumCA.getText().toString().matches(""))
                {
                    enableNumRA();
                }
                else
                {
                    disableNumRA();
                    multipleChoice.setChecked(true);
                }
            }

        });

        etnumTF.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etnumSA.getText().toString().matches("") &&
                        etnumMC.getText().toString().matches("")&&
                        etnumTF.getText().toString().matches("")&&
                        etnumCA.getText().toString().matches(""))
                {
                    enableNumRA();
                }
                else
                {
                    disableNumRA();
                    trueFalse.setChecked(true);
                }
            }

        });

        etnumCA.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etnumSA.getText().toString().matches("") &&
                        etnumMC.getText().toString().matches("")&&
                        etnumTF.getText().toString().matches("")&&
                        etnumCA.getText().toString().matches(""))
                {
                    enableNumRA();
                }
                else
                {
                    disableNumRA();
                    checkAll.setChecked(true);
                }
            }

        });

        etnumRA.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etnumRA.getText().toString().matches(""))
                {
                    enableOtherET();
                }
                else {
                    disableOtherET();
                    random.setChecked(true);
                }
            }

        });



        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                hideKeyboard(view);
                return false;
            }
        });
    }

    private void disableNumRA()
    {
        etnumRA.setEnabled(false);
    }

    private void enableNumRA()
    {
        etnumRA.setEnabled(true);
    }

    private void enableOtherET()
    {
        etnumSA.setEnabled(true);
        etnumMC.setEnabled(true);
        etnumTF.setEnabled(true);
        etnumCA.setEnabled(true);
    }

    private void disableOtherET()
    {
        etnumSA.setEnabled(false);
        etnumMC.setEnabled(false);
        etnumTF.setEnabled(false);
        etnumCA.setEnabled(false);
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

                    if(etnumSA.getText().toString().matches(""))
                    {
                        numSA = 0;
                    }
                    else {
                        numSA = Integer.parseInt(etnumSA.getText().toString());
                    }


                    if(etnumMC.getText().toString().matches(""))
                    {
                        numMC = 0;
                    }
                    else {
                        numMC = Integer.parseInt(etnumMC.getText().toString());
                    }


                    if(etnumTF.getText().toString().matches(""))
                    {
                        numTF = 0;
                    }
                    else {
                        numTF = Integer.parseInt(etnumTF.getText().toString());
                    }


                    if(etnumCA.getText().toString().matches(""))
                    {
                        numCA = 0;
                    }
                    else {
                        numCA = Integer.parseInt(etnumCA.getText().toString());
                    }


                    if(etnumRA.getText().toString().matches(""))
                    {
                        numRA = 0;
                    }
                    else {
                        numRA = Integer.parseInt(etnumRA.getText().toString());
                    }

                    successfulCreation = mvc.createTest(nameField.getText().toString(), false,
                            shortAnswer.isChecked(), multipleChoice.isChecked(),
                            trueFalse.isChecked(), checkAll.isChecked(), random.isChecked(),
                            numSA, numMC, numTF, numCA, numRA);


                    nameField.setText("");

                        /* Acknowledge the test was created by displaying a small message */
                    if (successfulCreation) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Quiz created",
                                Toast.LENGTH_SHORT);
                        toast.show();


                        /* After a 1500 delay, return to the list of tests */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), TestListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 1500);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Failed to create quiz", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                });
    }
}

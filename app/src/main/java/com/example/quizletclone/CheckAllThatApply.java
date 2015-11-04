package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CheckAllThatApply extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private CheckBox checkBoxA, checkBoxB, checkBoxC, checkBoxD, checkBoxE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_that_apply);
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("Check all that apply");

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

                        mvc.createFlashcard(questionField.getText().toString() + " "
                                        + fieldA.getText().toString() + " "
                                        + fieldB.getText().toString() + " "
                                        + fieldC.getText().toString() + " "
                                        + fieldD.getText().toString() + " "
                                        + fieldE.getText().toString(), answer, "UCSD");

                        questionField.setText("");
                        fieldA.setText("");
                        fieldB.setText("");
                        fieldC.setText("");
                        fieldD.setText("");
                        fieldE.setText("");

                        Toast toast = Toast.makeText(getApplicationContext(), "Flashcard created", Toast.LENGTH_SHORT);
                        //TODO: need a better way of calling toast (instead of creating an object everytime).
                        toast.show();
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

package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MultipleChoice extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField, fieldA, fieldB, fieldC, fieldD, fieldE;
    private RadioButton answerFieldA, answerFieldB, answerFieldC, answerFieldD, answerFieldE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("Multiple choice");

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

        createQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                                if (answerFieldA.isChecked()) {
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

package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class TrueFalse extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField;
    private TextView trueField, falseField;
    private RadioButton answerFieldT, answerFieldF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);

        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("True or false");

        createQuestion = (Button)findViewById(R.id.createFlashcard);
        questionField = (EditText)findViewById(R.id.questionField);
        trueField = (TextView)findViewById(R.id.trueField);
        falseField = (TextView)findViewById(R.id.falseField);
        answerFieldT = (RadioButton)findViewById(R.id.answerTrue);
        answerFieldF = (RadioButton)findViewById(R.id.answerFalse);

        createQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (answerFieldT.isChecked()) {
                            mvc.createFlashcard(questionField.getText().toString() + " A. True "
                                            + "B. False ",
                                    "True", "UCSD");
                            answerFieldF.setChecked(false);
                        } else {
                            mvc.createFlashcard(questionField.getText().toString() + " A. True "
                                        + " B. False ",
                                "False", "UCSD");
                            answerFieldF.setChecked(false);
                        }

                        questionField.setText("");

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

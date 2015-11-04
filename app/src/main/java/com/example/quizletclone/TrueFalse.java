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
import android.widget.TextView;
import android.widget.Toast;

public class TrueFalse extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText questionField;
    private TextView trueField, falseField;
    private RadioButton answerFieldT, answerFieldF;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);

        /* Retrieve the ModelViewController object from the calling class */
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("True or false");

        /* Initialize all the graphical user interface elements. */
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
                        //The flashcard creation
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

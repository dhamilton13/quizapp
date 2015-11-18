package com.example.quizletclone;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position, testPosition;
    private boolean tapped, componentAdded = false;
    private String callingClass;

    // what is being displayed on flashcard
    private TextView testTextView;
    // header, either "QUESTION" or "ANSWER"
    private TextView header;
    private LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        // when tapped == true, display question string, otherwise it will display answer
        tapped = true;

        // gets intent to obtain position of card clicked
        Intent intent = getIntent();
        mvc = ModelViewController.getInstance(this);

        // the position of the cardview clicked
        position = intent.getIntExtra("POS", 0);
        callingClass = intent.getStringExtra("callingClass");


        // set the question onto flashcard
        testTextView = (TextView) findViewById(R.id.testTextView);
        testTextView.setText(mvc.getFlashcards().get(position).getQuestion());
        testTextView.setMovementMethod(new ScrollingMovementMethod()); // make scrollable

        // set the header onto flashcard
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(null, Typeface.BOLD); // make bold
        header.setText("QUESTION");

        layout = (LinearLayout) findViewById(R.id.layout);

        if (callingClass.contains("FlashcardListForTestsActivity")) {
            testPosition = intent.getIntExtra("TestPOS", 0);
            testTextView.setText(mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getQuestion());
            String category = mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getCategory();

            createLayoutBasedOnCategory(category);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createLayoutBasedOnCategory(String category) {
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
            ArrayList<TextView> textView = new ArrayList<TextView>();

            for (int i = 0; i < 5; ++i) {
                checkBoxes.add(new CheckBox(this));
                textView.add(new TextView(this));
                textView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(checkBoxes.get(i));
                layout.addView(textView.get(i));
            }

        } else if (category.equals(MultipleChoice.CATEGORY)) {
            ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
            ArrayList<TextView> textView = new ArrayList<TextView>();

            for (int i = 0; i < 5; ++i) {
                radioButtons.add(new RadioButton(this));
                textView.add(new TextView(this));
                textView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(radioButtons.get(i));
                layout.addView(textView.get(i));
            }
        } else if (category.equals(TrueFalse.CATEGORY)) {
            ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
            ArrayList<TextView> textView = new ArrayList<TextView>();

            for (int i = 0; i < 2; ++i) {
                radioButtons.add(new RadioButton(this));
                textView.add(new TextView(this));
                textView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(radioButtons.get(i));
                layout.addView(textView.get(i));
            }
        } else {
            EditText textField = new EditText(this);
            layout.addView(textField);
        }
    }

    public void showAnswer(View v) {
        if(tapped == true && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(mvc.getFlashcards().get(position).getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else if (tapped == false && !callingClass.contains("FlashcardListForTestsActivity")){
            testTextView.setText(mvc.getFlashcards().get(position).getQuestion());
            header.setText("QUESTION");
            tapped = true;
        }
    }
}
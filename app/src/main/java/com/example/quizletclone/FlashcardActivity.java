package com.example.quizletclone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position, testPosition;
    private boolean tapped, componentAdded = false, basicLayoutExists = false;
    private String callingClass, userAnswer;
    private TextView testTextView, header;
    private LinearLayout layout;
    private Flashcard card;


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
        layout = (LinearLayout) findViewById(R.id.layout);

        // set the question onto flashcard
        testTextView = (TextView) findViewById(R.id.testTextView);

        if(!mvc.isTag)
            card = mvc.getFlashcards().get(position);       //get all cards from database
        else
            card = mvc.sortedCard.get(position);            //only get the card sorted by tag

        testTextView.setMovementMethod(new ScrollingMovementMethod()); // make scrollable

        // set the header onto flashcard
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(null, Typeface.BOLD); // make bold
        header.setText("QUESTION");

        //mvc.isTag decide whether this flashCard activity is open from a test list or normal
        //list (i.e. flash card list or tag list,
        if (!mvc.isTag) {
            /* Flashcard activity layout will depend on whether it is a normal flashcard or a test
                flashcard. Normal flashcards show answer and question. Test flashcards allow the
                user to input questions. The below if else statements will build the layout
                depending on the type of flashcard (Test vs normal).

                Calls either createBasicLayout (normal flashcard) or createLayoutBasedOnCagetory
                (test flashcard)
             */
            if (!callingClass.contains("FlashcardListForTestsActivity")) {
                testTextView.setText(card.getQuestion());
                createBasicFlashcardLayout(mvc.getFlashcards().get(position).getCategory());
            } else if (callingClass.contains("FlashcardListForTestsActivity")) {
                testPosition = intent.getIntExtra("TestPOS", 0);
                testTextView.setText(mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getQuestion());
                String category = mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getCategory();

                createTestFlashcardLayout(category);
            }
        }
        else {
            mvc.isTag = false;      //toggle the bool back for other activity
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

    /* Create a test flashcard layout depending on category type */
    private void createTestFlashcardLayout(String category) {
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

    /* Create a normal flashcard depending on layout */
    private void createBasicFlashcardLayout(String category) {
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));
            t3.setText("C: " + card.getListOfAnswers().get(2));
            t4.setText("D: " + card.getListOfAnswers().get(3));
            t5.setText("E: " + card.getListOfAnswers().get(4));

            layout.addView(t1);
            layout.addView(t2);
            layout.addView(t3);
            layout.addView(t4);
            layout.addView(t5);

        } else if (category.equals(MultipleChoice.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));
            t3.setText("C: " + card.getListOfAnswers().get(2));
            t4.setText("D: " + card.getListOfAnswers().get(3));
            t5.setText("E: " + card.getListOfAnswers().get(4));

            layout.addView(t1);
            layout.addView(t2);
            layout.addView(t3);
            layout.addView(t4);
            layout.addView(t5);
        } else if (category.equals(TrueFalse.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));

            layout.addView(t1);
            layout.addView(t2);
        } else {
            //ignore for now
        }
    }

    public void showAnswer(View v) {
        if(tapped == true && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(card.getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else if (tapped == false && !callingClass.contains("FlashcardListForTestsActivity")){
            testTextView.setText(card.getQuestion());
            header.setText("QUESTION");
            tapped = true;

        }
    }
}

package com.example.quizletclone;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position;
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

        if (callingClass.contains("FlashcardListForTestsActivity"))
           addGuiElementsForTest();
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

    private void addGuiElementsForTest() {
        Button button = new Button(this);
        button.setText("Test");
        layout.addView(button);
    }

    public void showAnswer(View v) {
        Log.v("Calling Class", callingClass);
        if(tapped == true && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(mvc.getFlashcards().get(position).getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else {
            testTextView.setText(mvc.getFlashcards().get(position).getQuestion());
            header.setText("QUESTION");
            tapped = true;
        }
    }



}
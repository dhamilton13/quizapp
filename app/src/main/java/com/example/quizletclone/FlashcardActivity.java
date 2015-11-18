package com.example.quizletclone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position;
    private boolean tapped;

    // what is being displayed on flashcard
    private TextView testTextView;
    // header, either "QUESTION" or "ANSWER"
    private TextView header;
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


        // set the question onto flashcard
        testTextView = (TextView) findViewById(R.id.testTextView);

        if(!mvc.isTag)
            card = mvc.getFlashcards().get(position);       //get all cards from database
        else
            card = mvc.sortedCard.get(position);            //only get the card sorted by tag

            testTextView.setText(card.getQuestion());

        mvc.isTag = false;      //toggle the bool back for other activity


        testTextView.setMovementMethod(new ScrollingMovementMethod()); // make scrollable
        // set the header onto flashcard
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(null, Typeface.BOLD); // make bold
        header.setText("QUESTION");
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


    public void showAnswer(View v) {
        if(tapped == true) {
            testTextView.setText(card.getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else {
            testTextView.setText(card.getQuestion());
            header.setText("QUESTION");
            tapped = true;
        }
    }



}
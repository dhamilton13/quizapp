package com.example.quizletclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jefflau on 11/11/15.
 */
public class TestListActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Test Flashcards");
        setContentView(R.layout.activity_list_test_flashcards);

        Intent intent = getIntent();
        // the position of the cardview clicked
        position = intent.getIntExtra("POS", 0);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);
        // Retrieves Tests
        List<Test> cards = mvc.getTests();
        // Retrieve Flashcards
        ArrayList<Flashcard> testFlashcards = cards.get(position).getSetOfFlashcards();

        // recycler view
        myRecyclerView = (RecyclerView)findViewById(R.id.testrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        populateCards(testFlashcards); // Populate the recyclerView with flashcard data

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    /* If 'back' is pressed on the device, return home. */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return;
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

    /* The next three methods save flashcard data whenever the current activity is paused or
        terminated.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        //mvc.storeFlashcards(getApplicationContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        //mvc.storeFlashcards(getApplicationContext());
    }

    /**
     * Build an ArrayList of flashcard questions from a  flashcard ArrayList.
     * @param fc 	The flashcard ArrayList.
     * @return 		An ArrayList of flashcard questions.
     */
    //TODO: Move to another class, violates SRP
    private List<String> buildFlashcardsToStrings(List<Flashcard> fc) {
        List<String> questions = new ArrayList<String>();
        for (int i = 0; i < fc.size(); ++i) {
            questions.add(fc.get(i).getQuestion());
        }
        return questions;
    }


    /* Populate the RecyclerView with flashcard questions */
    private void populateCards(ArrayList<Flashcard> testFlashcards){
        //Retrieve flashcard questions and convert to Strings
        List<String> cards = buildFlashcardsToStrings(testFlashcards);
//        List<String> cards = Arrays.asList("sup1", "sup2", "sup3");
        //TODO: Currently, the test's is empty. Can't view flashcards

        // populates the questions to the recyclerview to be displayed on cardview
        int i = 0;
        while(i < cards.size()){
            String propName = cards.get(i);
            i++;
            myRecyclerViewAdapter.add(
                    myRecyclerViewAdapter.getItemCount(), propName, System.getProperty(propName));
        }
    }
}

package com.example.quizletclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


/*  float button not used, comment out for latter development

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        //get the tag type on creat
        Intent intent = getIntent();
        String tag_type = intent.getStringExtra(MainActivity.TAG);
        setTitle(tag_type);



        mvc = ModelViewController.getInstance(this);
        mvc.loadFlashcards(this);

        myRecyclerView = (RecyclerView)findViewById(R.id.tagRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        populateCards(tag_type);
    }
    /* If 'back' is pressed on the device, return home. */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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


    private List<String> buildFlashcardsToStrings(List<Flashcard> fc, String str) {
        List<String> questions = new ArrayList<String>();
        for (int i = 0; i < fc.size(); ++i) {
            if(fc.get(i).getTag().equals(str))
                questions.add(fc.get(i).getQuestion());
        }
        return questions;
    }

    private void populateCards(String str){
        //Retrieve flashcard questions and convert to Strings
        List<String> cards = buildFlashcardsToStrings(mvc.getFlashcards(), str);

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



package com.example.quizletclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FlashcardListForTestsActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list_for_tests);
        mvc = ModelViewController.getInstance(this);

        Intent intent = getIntent();
        position = intent.getIntExtra("POS", 0);

        ArrayList<Flashcard> cards = mvc.getTests().get(position).getSetOfFlashcards();
        ArrayList<String> cardNames = buildFlashcardsToStrings(cards);

        setTitle("View Flashcards in Test");
        initializeComponents();
        populateCards(cardNames);
    }

    private void initializeComponents() {
        myRecyclerView = (RecyclerView)findViewById(R.id.myrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /* If 'back' is pressed on the device, return home. */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, TestListActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    private void populateCards(List<String> cards){

        // populates the questions to the recyclerview to be displayed on cardview
        int i = 0;
        while(i < cards.size()){
            String propName = cards.get(i);
            i++;
            myRecyclerViewAdapter.add(
                    myRecyclerViewAdapter.getItemCount(), propName, System.getProperty(propName));
        }
    }

    private ArrayList<String> buildFlashcardsToStrings(List<Flashcard> fc) {
        ArrayList<String> questions = new ArrayList<String>();
        for (int i = 0; i < fc.size(); ++i) {
            questions.add(fc.get(i).getQuestion());
        }
        return questions;
    }


}

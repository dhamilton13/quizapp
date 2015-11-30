package com.example.quizletclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FlashcardListForTestsActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private ImageButton fab;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private int position;
    public static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list_for_tests);
        mvc = ModelViewController.getInstance(this);

        Intent intent = getIntent();
        position = intent.getIntExtra("TestPOS", 0);
        index = intent.getIntExtra("TestPOS", 0);
        mvc.prepareGrader(mvc.getTests().get(index));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {fab = (ImageButton) findViewById(R.id.imageButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dialog asking the user what kind of question they want to click
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Test options");
                // The flashcard creation options
                builder.setItems(new CharSequence[]
                                {"Grade Test", "Clear Test"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        gradeTest();
                                        break;
                                    case 1:
                                        clearTest();
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    /* If 'back' is pressed on the device, return home. */
    @Override
    public void onBackPressed() {
        mvc.clearGrader();
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

    private void gradeTest() {
        mvc.gradeTest();
        Log.v("NUMBER CORRECT", String.valueOf(TestGrader.getNumberCorrect()));
    }

    private void clearTest() {
        mvc.clearGrader();
        mvc.prepareGrader(mvc.getTests().get(index));
    }
}

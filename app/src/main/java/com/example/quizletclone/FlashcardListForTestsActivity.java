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
import android.widget.Toast;
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

        setTitle("View Flashcards in Quiz");
        initializeComponents();
        populateCards(cardNames);
    }

    private void initializeComponents() {
        myRecyclerView = (RecyclerView)findViewById(R.id.myrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, null);
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
                builder.setTitle("Quiz Options");
                // The flashcard creation options
                builder.setItems(new CharSequence[]
                                {"Grade Quiz", "Clear Results"},
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
        final AlertDialog dialog = new AlertDialog.Builder(FlashcardListForTestsActivity.this).create();
        dialog.setTitle("Score");
        dialog.setMessage("You got " + String.valueOf(TestGrader.getNumberCorrect()) +
                " out of " + String.valueOf(TestGrader.getCorrectAnswers().length) + " correct.\n" +
                "Percent correct: " + String.format("%.2f", TestGrader.calculateScore()) + "%");

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterace, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void clearTest() {

        final AlertDialog dialog = new AlertDialog.Builder(FlashcardListForTestsActivity.this).create();
        dialog.setTitle("Clear quiz");
        dialog.setMessage("Are you sure you wish to clear the quiz?");

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterace, int id) {
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterace, int id) {
                dialog.dismiss();
                mvc.clearGrader();
                mvc.prepareGrader(mvc.getTests().get(index));
            }
        });
        dialog.show();
    }
}

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jefflau on 11/11/15.
 */
public class TestListActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    private ImageButton fab; //floating action button
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Quiz Flashcards");
        setContentView(R.layout.activity_list_test_flashcards);

        Intent intent = getIntent();
        // the position of the cardview clicked
        position = intent.getIntExtra("POS", 0);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);

        // Retrieves Tests
        List<Test> cards = mvc.getTests();

        // recycler view
        myRecyclerView = (RecyclerView)findViewById(R.id.testrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this, null);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        populateTests(); // Populate the recyclerView with flashcard data

        fab = (ImageButton) findViewById(R.id.imageButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Create quiz?");
                // The flashcard creation options
                builder.setItems(new CharSequence[]
                                {"Create quiz"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                     createTest();
                                     break;
                                }
                            }
                        });
                builder.create().show();
            }
        });

    }

    @Override   //inflate the action menu bar
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.tag, menu);
        return true;
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

        id = item.getItemId();

        //reaction when click the delete tag button
        if (id == R.id.tag_action_delete) {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

            int size = mvc.getTests().size();
            String arr[] = new String[size];
            for(int i=0;i<size;i++)
                arr[i] = mvc.getTests().get(i).getName();

            builder.setTitle("Delete Test")
                    .setItems(arr, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //gets the tag you want to delete
                                confirmDelete(mvc.getTests().get(which).getName());

                        }
                    });

            android.app.AlertDialog a = builder.create();
            a.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void confirmDelete(final String whichTest)
    {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);

        alert.setTitle("Delete Confirmation");
        alert.setMessage("Are you sure you would like to delete this test?");

        alert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                mvc.deleteTest(whichTest);
                startActivity(getIntent());
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
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

    private void populateTests() {
        List<Test> cards = mvc.getTests();


        // populates the tests to the recyclerview to be displayed on cardview
        int i = 0;
        while(i < cards.size()){
            String propName = cards.get(i).getName();
            i++;
            myRecyclerViewAdapter.add(
                    myRecyclerViewAdapter.getItemCount(), propName, System.getProperty(propName));
        }
    }

    private void createTest() {
        final Intent intent = new Intent(this, CreateTest.class);
        final Intent intentForList = new Intent(this, ListActivity.class);
        final AlertDialog dialog = new AlertDialog.Builder(TestListActivity.this).create();
        dialog.setTitle("Quiz Options");
        dialog.setMessage("How would you like to create the quiz?");

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Choose cards", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterace, int id) {
                intentForList.putExtra("callingClass", findViewById(android.R.id.content).getContext().toString());
                startActivity(intentForList);
                finish();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Automatically", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterace, int id) {
                intent.putExtra("callingClass", findViewById(android.R.id.content).getContext().toString());
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }
}

package com.example.quizletclone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class CreateTag extends AppCompatActivity {

    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);
        setTitle("Tag List");

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);
        mvc.loadTag(this);

        myRecyclerView = (RecyclerView)findViewById(R.id.tag_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        populateCards();

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //mvc.storeFlashcards(getApplicationContext());
    }
    
    public void addTag(View view)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Creat a new tag");
        alert.setMessage("Enter the tag name:");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Do something with value!
               String message = input.getText().toString();
                boolean createSuccess = mvc.createTag(message);
                input.setText("");

                if (createSuccess) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Tag Created",
                            Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Tag Already Exists", Toast.LENGTH_SHORT);
                    toast.show();
                }

                finish();
                startActivity(getIntent());

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    private void populateCards(){
        //Retrieve flashcard questions and convert to Strings
        List<String> cards = mvc.getTags();

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

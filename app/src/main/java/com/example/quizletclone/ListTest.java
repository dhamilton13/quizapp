package com.example.quizletclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

public class ListTest extends AppCompatActivity {
    private ModelViewController mvc;
    private ImageButton Fab;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test);

        /* Retrieve the ModelViewController object from the calling class */
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("View tests");


        // Recycler view for tests.
        /* TODO: This currently calls flashcard's recyclerView, need to create another recyclerView
            for test
         */
        myRecyclerView = (RecyclerView)findViewById(R.id.myrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        populateTests();

        /* Construction of the floating action button which presents the user with the option
            to create a test.
		 */
        Fab = (ImageButton) findViewById(R.id.imageButton);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dialog asking the user what kind of question they want to click
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Create a test");
                builder.setItems(new CharSequence[]
                                {"Create a test"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        goToTest();
                                        break;

                                }
                            }
                        });
                builder.create().show();

            }
        });
    }

    /* If the back button is pressed on the device, return home */
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);

        // Pass the model view controller object to ShortAnswer
        intent.putExtra("MVCObj", mvc);
        startActivity(intent);
        return;
    }

    /* Populate the RecycleView with tests */
    private void populateTests() {
        List<Test> cards = mvc.getTests();

        // populates the questions to the recyclerview to be displayed on cardview
        int i = 0;
        while(i < cards.size()){
            String propName = cards.get(i).getName();
            i++;
            myRecyclerViewAdapter.add(
                    myRecyclerViewAdapter.getItemCount(),
                    propName,
                    System.getProperty(propName));
        }
    }

    /* Passes control to the CreateTest class when the user selects create test. Also passes the
        ModelViewController object */

    private void goToTest(){
        Intent intent = new Intent(this, CreateTest.class);

        // Pass the model view controller object to ShortAnswer
        intent.putExtra("MVCObj", mvc);
        startActivity(intent);
    }
}

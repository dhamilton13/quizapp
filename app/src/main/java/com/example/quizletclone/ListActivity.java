package com.example.quizletclone;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ListActivity extends AppCompatActivity {
	private ModelViewController mvc;
	private ImageButton FAB; //floating action button
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private List<Flashcard> fc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

        /* Retrieve the ModelViewController object from the calling class */
        mvc = ModelViewController.getInstance(this);
        mvc.loadFlashcards(this);
        mvc.loadTag(this);

        //fc = mvc.getFlashcards();

        setTitle("View Flashcards");

        // recycler view
        myRecyclerView = (RecyclerView)findViewById(R.id.myrecyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);


        if(!mvc.isSorted)
            populateCards(); // Populate the recyclerView with all flashcard data
        else
        {
            setTitle(mvc.sort_option);
            populateCards(mvc.sort_option);    //populate the recyclerView with only the sorted card
            //mvc.isSorted = false;  TODO: it will never go back to true!

        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert dialog asking the user what kind of question they want to click
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Question Type");
                // The flashcard creation options
                builder.setItems(new CharSequence[]
                                {"Multiple Choice", "Short Answer", "True/False",
                                        "Check All That Apply"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        multipleChoice();
                                        break;
                                    case 1:
                                        shortAnswer();
                                        break;
                                    case 2:
                                        trueFalse();
                                        break;
                                    case 3:
                                        checkAllThatApply();
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });
		// Inflate the menu; this adds items to the action bar if it is present.

        //menu = (Menu) findViewById(R.id.sort_menu);

        //menu.add("option 1");

		getMenuInflater().inflate(R.menu.list_activity, menu);
		return true;
	}

    /* If 'back' is pressed on the device, return home. */
    @Override
    public void onBackPressed() {

        //press 'back' from a sorted list will go back to unsorted list
        //press 'back' from an unsorted list will go back to MainActivity
        if(!mvc.isSorted) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        else
        {
            mvc.isSorted = false;
            finish();
            startActivity(getIntent());
        }
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

        /* reaction when you click the top right search button */
		int id = item.getItemId();
		if (id == R.id.action_sort) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            int size = mvc.getTags().size();
            String arr[] = new String[size];
            for(int i=0;i<size;i++)
                arr[i] = mvc.getTags().get(i);

            builder.setTitle("Sort Flash Card by Tags")
                    .setItems(arr, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            mvc.sort_option = mvc.getTags().get(which);
                            mvc.isSorted = true;

                            //decide what tag content need to be show and refresh the current activity
                            finish();
                            startActivity(getIntent());
                        }
                    });



            AlertDialog a = builder.create();
            a.show();

/* test if the sort_option is returned correctly

            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle(sort_option);
            AlertDialog b = builder2.create();
            b.show();
*/
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
    private void populateCards(){
        //Retrieve flashcard questions and convert to Strings
        List<String> cards = buildFlashcardsToStrings(mvc.getFlashcards());

        // populates the questions to the recyclerview to be displayed on cardview
        int i = 0;
        while(i < cards.size()){
            String propName = cards.get(i);
            i++;
            myRecyclerViewAdapter.add(
                myRecyclerViewAdapter.getItemCount(), propName, System.getProperty(propName));
        }
    }

    /*overload populateCards() and buildFlashCardToStrings(fc) to serve the sorting purpose */
    private List<String> buildFlashcardsToStrings(List<Flashcard> fc, String str) {
        List<String> questions = new ArrayList<String>();

        mvc.sortedCard.clear();
        for (int i = 0; i < fc.size(); i++) {
            if(fc.get(i).getTag().equals(str)) {
                questions.add(fc.get(i).getQuestion());
                mvc.sortedCard.add(fc.get(i));          //record that card for recyclerView in next activity
            }
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


    /* The following private methods are responsible for passing control to the respective
        activites that will create specific types of questions. Each method passes the
        modelViewController in order to call createFlashcard() in each activity.
     */
    // starts new activity leading to multiple choice creation
    private void multipleChoice(){
        Intent intent = new Intent(this, MultipleChoice.class);
        startActivity(intent);
    }

    // starts new activity leading to short answer creation
    private void shortAnswer(){
        Intent intent = new Intent(this, ShortAnswer.class);

        startActivity(intent);
    }

    private void trueFalse(){
        Intent intent = new Intent(this, TrueFalse.class);
        startActivity(intent);
    }

    private void checkAllThatApply() {
        Intent intent = new Intent(this, CheckAllThatApply.class);
        startActivity(intent);
    }
}

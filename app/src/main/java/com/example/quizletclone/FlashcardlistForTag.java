package com.example.quizletclone;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;

        import java.util.ArrayList;
        import java.util.List;

public class FlashcardlistForTag extends AppCompatActivity {

    private ModelViewController mvc;
    private RecyclerView myRecyclerView;
    LinearLayoutManager linearLayoutManager;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcardlist_for_tag);
        mvc = ModelViewController.getInstance(this);
        mvc.loadFlashcards(this);

        Intent intent = getIntent();
        position = intent.getIntExtra("POS", 0);

        String selected_Tag = mvc.getTags().get(position);

        setTitle(selected_Tag);

        /* initialize recyclerView component */
        myRecyclerView = (RecyclerView)findViewById(R.id.tag_to_card_recyclerview);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        populateCards(selected_Tag);



    }
    /* If 'back' is pressed on the device, return to previous. */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CreateTag.class);
        startActivity(intent);
        finish();
        return;
    }

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


}

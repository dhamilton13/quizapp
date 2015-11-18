package com.example.quizletclone;

/**
 * Created by jefflau on 11/2/15.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;



public class MyRecyclerViewAdapter extends
        RecyclerView.Adapter<MyRecyclerViewAdapter.ItemHolder>{

    private List<String> cardQuestion;
    private LayoutInflater layoutInflater;
    private Context context;
    private ModelViewController mvc;

    public MyRecyclerViewAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        cardQuestion = new ArrayList<String>();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView itemCardView =
                (CardView)layoutInflater.inflate(R.layout.layout_cardview, viewGroup, false);

        return new ItemHolder(itemCardView, this);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        itemHolder.setItemName(cardQuestion.get(i));

    }

    @Override
    public int getItemCount() {
        return cardQuestion.size();
    }

    public void add(int location, String iName, String iValue){
        cardQuestion.add(location, iName);
        notifyItemInserted(location);
    }

    public void remove(int location){
        if(location >= cardQuestion.size())
            return;

        cardQuestion.remove(location);
        notifyItemRemoved(location);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{

        private MyRecyclerViewAdapter parent;
        private CardView cardView;
        TextView textItemName;

        public ItemHolder(CardView cView, MyRecyclerViewAdapter parent) {
            super(cView);
            cardView = cView;
            this.parent = parent;
            textItemName = (TextView) cardView.findViewById(R.id.card_question);


            cView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    movePosition(position, v);

                }
            });
        }

        public void setItemName(CharSequence name){
            textItemName.setText(name);
        }

        public CharSequence getItemName(){
            return textItemName.getText();
        }

        /** Method moves the position of the cardview to the appropriate activity **/
        public void movePosition(int position, View v) {
            ModelViewController mvc = ModelViewController.getInstance(v.getContext());
            // See if the onclick is called in the ListActivity
            if(v.getContext() instanceof ListActivity || v.getContext() instanceof FlashcardListForTestsActivity) {
                Intent intent = new Intent(v.getContext(), FlashcardActivity.class);

                if (v.getContext() instanceof FlashcardListForTestsActivity) {
                    intent.putExtra("TestPOS", FlashcardListForTestsActivity.index);
                    Log.v("TEST INDEX", String.valueOf(FlashcardListForTestsActivity.index));
                }

                intent.putExtra("POS", position);
                intent.putExtra("callingClass", v.getContext().toString());
                v.getContext().startActivity(intent);
            } else if(v.getContext() instanceof TestListActivity){ //From Test Activity
                //System.out.println(position);
                Intent intent = new Intent(v.getContext(), FlashcardListForTestsActivity.class);
                intent.putExtra("TestPOS", position);
                v.getContext().startActivity(intent);
            }
        }
    }
}

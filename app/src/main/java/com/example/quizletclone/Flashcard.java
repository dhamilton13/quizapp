package com.example.quizletclone;

import android.os.Parcel;
import android.os.Parcelable;

public class Flashcard implements Parcelable {
	private String question, answer, category, tag;
	
	Flashcard(String question, String answer, String tag, String category) {
		this.question = question;
		this.answer = answer;
		this.category = category;
        this.tag = tag;
	}

    /* Getters for retrieving flashcard data */
	public String getQuestion() {
		return this.question;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public String getCategory() {
		return this.category;
	}

    public String getTag() { return this.tag; }

    public void setTag(String tag) {
        this.tag = tag;
    }
	
	/* BEGIN PARCELABLE FLASHCARD OBJECT CREATION */
	protected Flashcard(Parcel in) {
        question = in.readString();
        answer = in.readString();
        category = in.readString();
        tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(category);
        dest.writeString(tag);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Flashcard> CREATOR = new Parcelable.Creator<Flashcard>() {
        @Override
        public Flashcard createFromParcel(Parcel in) {
            return new Flashcard(in);
        }

        @Override
        public Flashcard[] newArray(int size) {
            return new Flashcard[size];
        }
    };
}

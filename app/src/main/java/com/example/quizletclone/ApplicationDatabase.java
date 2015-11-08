package com.example.quizletclone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunnysarow on 11/7/15.
 */
public class ApplicationDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "QuizMe.db";
    public static final String TABLE_NAME = "flashcard_table";
    public static final String COL_1 = "QUESTION";
    public static final String COL_2 = "ANSWER";
    public static final String COL_3 = "TAG";
    public static final String COL_4 = "CATEGORY";


    public ApplicationDatabase(Context context) {
        super (context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(QUESTION TEXT, ANSWER TEXT, TAG TEXT, CATEGORY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String flashcardQuestion, String flashcardAnswer, String flashcardTag,
                            String flashcardCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, flashcardQuestion);
        contentValues.put(COL_2, flashcardAnswer);
        contentValues.put(COL_3, flashcardTag);
        contentValues.put(COL_4, flashcardCategory);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}

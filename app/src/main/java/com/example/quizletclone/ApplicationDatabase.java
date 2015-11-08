package com.example.quizletclone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;

import org.json.JSONObject;

public class ApplicationDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizMe.db";
    private static final String TABLE_NAME = "flashcard_table";
    private static final String TABLE_2_NAME = "test_table";
    private static final String COL_1_T = "NAME";
    private static final String COL_2_T = "FLASHCARDS";
    private static final String COL_1 = "QUESTION";
    private static final String COL_2 = "ANSWER";
    private static final String COL_3 = "TAG";
    private static final String COL_4 = "CATEGORY";


    public ApplicationDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(QUESTION TEXT, ANSWER TEXT, TAG TEXT, CATEGORY TEXT)");
        db.execSQL("create table " + TABLE_2_NAME + "(FLASHCARDS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_NAME);
        db.execSQL(TABLE_2_NAME);
        onCreate(db);

    }

    public boolean insertFlashcardData(String flashcardQuestion, String flashcardAnswer, String flashcardTag,
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

    public boolean insertTestData(String testName, String flashcards) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T, testName);
        contentValues.put(COL_2_T, flashcards);

        long result = db.insert(TABLE_2_NAME, null, contentValues);

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

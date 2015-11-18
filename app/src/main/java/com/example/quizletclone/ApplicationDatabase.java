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
    private static final String COL_1_T2 = "NAME";
    private static final String COL_2_T2 = "DYNAMIC";
    private static final String COL_3_T2 = "SA";
    private static final String COL_4_T2 = "MC";
    private static final String COL_5_T2 = "TF";
    private static final String COL_6_T2 = "CA";
    private static final String COL_7_T2 = "FLASHCARDS";
    private static final String COL_1_T1 = "QUESTION";
    private static final String COL_2_T1 = "ANSWER";
    private static final String COL_3_T1 = "TAG";
    private static final String COL_4_T1 = "CATEGORY";
    private static final String COL_5_T1 = "ANSWERLIST";


    public ApplicationDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)  {
        db.execSQL("create table " + TABLE_2_NAME + "(NAME TEXT, DYNAMIC INT, SA INT, MC INT, TF INT, CA INT, FLASHCARDS TEXT)");
        db.execSQL("create table " + TABLE_NAME + "(QUESTION TEXT, ANSWER TEXT, TAG TEXT, CATEGORY TEXT, ANSWERLIST TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_NAME);
        db.execSQL(TABLE_2_NAME);
        onCreate(db);
    }

    public boolean insertFlashcardData(String flashcardQuestion, String flashcardAnswer, String answerList,
                                       String flashcardTag,
                            String flashcardCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T1, flashcardQuestion);
        contentValues.put(COL_2_T1, flashcardAnswer);
        contentValues.put(COL_3_T1, flashcardTag);
        contentValues.put(COL_4_T1, flashcardCategory);
        contentValues.put(COL_5_T1, answerList);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        return true;
    }

    public boolean insertTestData(String testName, boolean isDynamic, boolean isSA, boolean isMC,
                                  boolean isTF, boolean isCA, String flashcards) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T2, testName);
        contentValues.put(COL_2_T2, isDynamic);
        contentValues.put(COL_3_T2, isSA);
        contentValues.put(COL_4_T2, isMC);
        contentValues.put(COL_5_T2, isTF);
        contentValues.put(COL_6_T2, isCA);
        contentValues.put(COL_7_T2, flashcards);

        long result = db.insert(TABLE_2_NAME, null, contentValues);

        if(result == -1)
            return false;
        return true;
    }

    public Cursor getFlashcardData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor getTestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_2_NAME, null);
        return res;
    }
}

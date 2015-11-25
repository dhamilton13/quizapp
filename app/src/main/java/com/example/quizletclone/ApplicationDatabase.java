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
    private static final String TABLE_3_NAME = "tag_table";
    private static final String COL_1_T3 = "TAG";
    private static final String COL_1_T2 = "NAME";
    private static final String COL_2_T2 = "DYNAMIC";
    private static final String COL_3_T2 = "SA";
    private static final String COL_4_T2 = "MC";
    private static final String COL_5_T2 = "TF";
    private static final String COL_6_T2 = "CA";
    private static final String COL_7_T2 = "FLASHCARDS";
    private static final String COL_8_T2 = "RA";

    private static final String COL_9_T2 = "SANUM";
    private static final String COL_10_T2 = "MCNUM";
    private static final String COL_11_T2 = "TFNUM";
    private static final String COL_12_T2 = "CANUM";
    private static final String COL_13_T2 = "RANUM";

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
        db.execSQL("create table " + TABLE_3_NAME + "(TAG TEXT UNIQUE)");
        db.execSQL("create table " + TABLE_2_NAME + "(NAME TEXT, DYNAMIC INT, SA INT, MC INT, TF INT, CA INT, RA INT, FLASHCARDS TEXT, SANUM INT, MCNUM INT, TFNUM, CANUM INT, RANUM INT)");
        db.execSQL("create table " + TABLE_NAME + "(QUESTION TEXT, ANSWER TEXT, TAG TEXT, CATEGORY TEXT, ANSWERLIST TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_NAME);
        db.execSQL(TABLE_2_NAME);
        db.execSQL(TABLE_3_NAME);
        onCreate(db);
    }

    public boolean insertFlashcardData(String flashcardQuestion, String flashcardAnswer, String answerList,
                                       String flashcardTag,String flashcardCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T1, flashcardQuestion);
        contentValues.put(COL_2_T1, flashcardAnswer);
        contentValues.put(COL_3_T1, flashcardTag);
        contentValues.put(COL_4_T1, flashcardCategory);
        contentValues.put(COL_5_T1, answerList);

        //so the structure of contentValue after all puts should be
        //due to the mismatch of flash card constructor and database insert method signature
        //  question | answer | Category | tag | answerList
        //  Col_1    | Col_2  | Col_3    |Col_4| Col_5

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        return true;
    }

    public boolean insertTestData(String testName, boolean isDynamic, boolean isSA, boolean isMC,
                                  boolean isTF, boolean isCA, boolean isRA, String flashcards,
                                  int saNum, int mcNum, int tfNum, int caNum, int raNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T2, testName);
        contentValues.put(COL_2_T2, isDynamic);
        contentValues.put(COL_3_T2, isSA);
        contentValues.put(COL_4_T2, isMC);
        contentValues.put(COL_5_T2, isTF);
        contentValues.put(COL_6_T2, isCA);
        contentValues.put(COL_8_T2, isRA);
        contentValues.put(COL_7_T2, flashcards);
        contentValues.put(COL_9_T2, saNum);
        contentValues.put(COL_10_T2, mcNum);
        contentValues.put(COL_11_T2, tfNum);
        contentValues.put(COL_12_T2, caNum);
        contentValues.put(COL_13_T2, raNum);

        long result = db.insert(TABLE_2_NAME, null, contentValues);

        if(result == -1)
            return false;
        return true;
    }

    public boolean insertTagData(String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_T3, tag);

        long result = db.insert(TABLE_3_NAME, null, contentValues);

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

    public Cursor getTagData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_3_NAME, null);
        return res;
    }

    public void deleteTag(String str)
    {
        //memo:
        //  question | answer | Category | tag | answerList
        //  Col_1    | Col_2  | Col_3    |Col_4| Col_5
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_3_NAME + " WHERE " + COL_1_T3 + "='" + str + "'");
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_4_T1 + "='" + str + "'");
    }


}

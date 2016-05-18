package com.example.john.miniproj3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by John on 3/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="phrase.db";
    private static final int SCHEMA=1;
    static final String NAME="phrase_name";
    static final String DESC="phrase_desc";
    static final String LANG="phrase_lang";
    static final String TABLE="phrase";

    private static final String[] itemsEng= { "see you tomorrow.", "nice to meet you!", "have a nice day!", "today, the weather is good!"};
    private static final String[] itemsEngDesc = {"Typically said to someone whose daily schedule is the same as one's own.", "nice to meet you!", "have a nice day!", "today, the weather is good!"};
    private static final String[] itemsCn= { "食左飯未?", "今晚遲Ｄ返來食飯.", "今日天氣幾好."};
    private static final String[] itemsCnDesc = {"食左飯未?", "今晚遲Ｄ返來食飯.", "今日天氣幾好."};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE phrase (phrase_name TEXT, phrase_desc TEXT, phrase_lang TEXT);");

        ContentValues cv=new ContentValues();

        for (int i = 0 ; i < itemsEng.length ; i++) {
            cv.put(NAME, itemsEng[i]);
            cv.put(DESC, itemsEngDesc[i]);
            cv.put(LANG, "eng");
            db.insert(TABLE, NAME, cv);
        }

        for (int i = 0 ; i < itemsCn.length ; i++) {
            cv.put(NAME, itemsCn[i]);
            cv.put(DESC, itemsCnDesc[i]);
            cv.put(LANG, "cn");
            db.insert(TABLE, NAME, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("How did we get here?");
    }
}

package com.example.android.cinemate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cinemate.data.MovieContract.MovieEntry;

/**
 * Created by Gavin on 19-Aug-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movies.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                    + MovieEntry.COLUMN_NAME_ID + " INTEGER,"
                    + MovieEntry.COLUMN_NAME_TITLE + " TEXT,"
                    + MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT,"
                    + MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT,"
                    + MovieEntry.COLUMN_NAME_LANGUAGE + " TEXT,"
                    + MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT,"
                    + MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

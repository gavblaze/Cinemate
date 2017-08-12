package com.example.android.cinemate.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cinemate.data.FavouritesContract.FavouriteEntry;

/**
 * Created by Gavin on 12-Aug-17.
 */

public class FavouritesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Favourites.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavouriteEntry.TABLE_NAME + " (" +
                    FavouriteEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FavouriteEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    FavouriteEntry.COLUMN_POSTER_PATH + " TEXT," +
                    FavouriteEntry.COLUMN_NAME_LANGUAGE + " TEXT," +
                    FavouriteEntry.COLUMN_NAME_RELEASE_DATE + " TEXT," +
                    FavouriteEntry.COLUMN_NAME_VOTE_AVERAGE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavouriteEntry.TABLE_NAME;


    public FavouritesDbHelper(Context context) {
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


package com.example.android.cinemate.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.cinemate.data.MovieContract.MovieEntry;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class MovieProvider extends ContentProvider {
    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    static {
         /*
         * Sets the integer value for multiple rows in MovieTable to 100. Notice that no wildcard is used
         * in the path
         */
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME, MOVIE);
        /*
         * Sets the code for a single row to 101. In this case, the "#" wildcard is
         * used. "content://com.example.android.cinemate/movietable/3" matches, but
         * "content://com.example.android.cinemate/movietable doesn't.
         */
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME + "/#", MOVIE_ID);
    }

    private MovieDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionAgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case MOVIE:
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionAgs, null, null, sortOrder);
                break;
            case MOVIE_ID:
                selection = MovieEntry.COLUMN_NAME_ID + "=?";
                selectionAgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionAgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri uriOfInserted;
        switch (match) {
            case MOVIE:
                long idOfMovieInserted = db.insert(MovieEntry.TABLE_NAME, null, contentValues);
                uriOfInserted = ContentUris.withAppendedId(uri, idOfMovieInserted);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not succesfull for " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriOfInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
            case MOVIE_ID:
                selection = MovieEntry.COLUMN_NAME_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not succesfull for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }
}

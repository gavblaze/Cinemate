package com.example.android.cinemate.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.cinemate.data.MovieContract.MovieEntry;

/**
 * Created by Gavin on 20-Sep-17.
 */

public class FavouriteUtils {

    public static boolean isFavourite(Context context, int movieId) {
        Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        String[] projection = {MovieEntry.COLUMN_NAME_FAVOURITE};
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToNext();
        }
        int indexOfValue = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVOURITE);
        int value = cursor.getInt(indexOfValue);
        if (value == MovieEntry.IS_FAVOURITE) {
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public static void setToFavouriteInDb(Context context, int movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_NAME_FAVOURITE, MovieEntry.IS_FAVOURITE);
        Uri uriToUpdate = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uriToUpdate))};
        context.getContentResolver().update(MovieEntry.CONTENT_URI, contentValues, selection, selectionArgs);
    }

    public static void setToDefaultInDb(Context context, int movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_NAME_FAVOURITE, MovieEntry.IS_DEFAULT);
        Uri uriToUpdate = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uriToUpdate))};
        context.getContentResolver().update(MovieEntry.CONTENT_URI, contentValues, selection, selectionArgs);
    }
}

package com.example.android.cinemate.data;

import android.provider.BaseColumns;

/**
 * Created by Gavin on 12-Aug-17.
 */

public class FavouritesContract {

    private FavouritesContract() {
    }

    public static class FavouriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_NAME_LANGUAGE = "language";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteaverage";
    }
}


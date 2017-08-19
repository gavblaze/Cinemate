package com.example.android.cinemate.data;

import android.provider.BaseColumns;

/**
 * Created by Gavin on 19-Aug-17.
 */

public final class MovieContract {
    private MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "movieId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POSTER_PATH = "posterpath";
        public static final String COLUMN_NAME_LANGUAGE = "language";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteaverage";
    }
}

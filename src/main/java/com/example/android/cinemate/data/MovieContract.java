package com.example.android.cinemate.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Gavin on 12-Aug-17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.cinemate";
    //Base Uri for all querys
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //This is the path of the actual table we are interested in (the movie table) eg if we have multiple tables
    public static final String PATH = "movies";

    private MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "movieId";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_POSTER_PATH = "posterpath";
        public static final String COLUMN_NAME_RELEASE_DATE = "releasedate";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteaverage";
        public static final String COLUMN_NAME_SORT_ORDER = "sortorder";
        public static final String COLUMN_NAME_FAVOURITE = "favourite";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
    }
}


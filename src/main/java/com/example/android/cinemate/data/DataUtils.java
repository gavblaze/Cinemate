package com.example.android.cinemate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.example.android.cinemate.Movie;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import com.example.android.cinemate.data.MovieProvider;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class DataUtils extends AsyncTask<String, Void, List<Movie>> {
    private Context mContext;

    public DataUtils(Context context) {
        this.mContext = context;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String i = strings[0];
        if (i.isEmpty()) return null;
        String url = NetworkUtils.getDataFromNetwork(TmdbUrlUtils.MOVIE_URL);
        try {
            return MovieJsonUtils.parseJson(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {

        for (Movie movie : movies) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_NAME_ID, movie.getmId());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getmTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getmOverview());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getmPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getmReleaseDate());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getmRating());
            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
        }
    }
}

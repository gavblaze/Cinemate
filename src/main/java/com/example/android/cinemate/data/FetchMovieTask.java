package com.example.android.cinemate.data;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.cinemate.Movie;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
    private Context mContext;


    public FetchMovieTask(Context context) {
        this.mContext = context;
    }


    @Override
    protected List<Movie> doInBackground(String... strings) {
        String i = strings[0];
        String url = NetworkUtils.getDataFromNetwork(i);
        try {
            return MovieJsonUtils.parseJson(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {

        insertIntoDb(movies);
    }


    public void insertIntoDb(List<Movie> listMovie) {
        for (Movie movie : listMovie) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_NAME_ID, movie.getmId());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getmTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getmOverview());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getmPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getmReleaseDate());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getmRating());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_SORT_ORDER, MoviePreferences.preferenceSelected(mContext));
            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        }
    }
}

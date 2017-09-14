package com.example.android.cinemate.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.android.cinemate.MainActivity;
import com.example.android.cinemate.models.Movie;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    public View mLoadingIndicator;
    private Context mContext;
    //private AsyncTaskResponse mResponse;


    public FetchMovieTask(Context context) {
        this.mContext = context;
        //this.mResponse = response;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        Log.i(LOG_TAG, "TEST.......................FetchMovieTask doInBackground() called");


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
        //Log.i(LOG_TAG, "TEST.......................FetchMovieTask onPostExecute() called");

        if (movies != null) {
            //mResponse.asyncTaskResult(movies);
            insertIntoDb(movies);
        }
    }

    public void insertIntoDb(List<Movie> listMovie) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortOrder = sp.getString(MainActivity.SORT_KEY, MainActivity.DEFAULT);

        for (Movie movie : listMovie) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_NAME_ID, movie.getmId());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, movie.getmTitle());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getmOverview());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getmPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getmBackdropPath());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getmReleaseDate());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getmRating());
            values.put(MovieContract.MovieEntry.COLUMN_NAME_SORT_ORDER, sortOrder);
            mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        }
    }

    /*We create an interface so we can pass the list of Movie objects back to MainActivity*/
    public interface AsyncTaskResponse {
        void asyncTaskResult(List<Movie> data);
    }
}

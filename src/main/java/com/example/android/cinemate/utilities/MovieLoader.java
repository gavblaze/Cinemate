package com.example.android.cinemate.utilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.cinemate.Movie;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 02-Aug-17.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    private List<Movie> mData;

    private String mStringUrl;
    public MovieLoader(Context context, String stringUrl) {
        super(context);
        mStringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {

        //Log.i(LOG_TAG, "TEST.......MovieLoader onStartLoading() called");
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
        } else {
            //We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        //Log.i(LOG_TAG, "TEST.......MovieLoader loadInBackground() called");

        if (mStringUrl == null) {
            return null;
        }
        String jsonString = NetworkUtils.getDataFromNetwork(mStringUrl);
        try {
            return MovieJsonUtils.parseJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(List<Movie> data) {
        //Log.i(LOG_TAG, "TEST.......MovieLoader deliverResult() called");
        mData = data;
        super.deliverResult(data);
    }
}

package com.example.android.cinemate.utilities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 02-Aug-17.
 */

public class MovieLoader extends AsyncTaskLoader<List<String>> {
    private static final String LOG_TAG = MovieLoader.class.getSimpleName();

    private String mStringUrl;
    public MovieLoader(Context context, String stringUrl) {
        super(context);
        mStringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {
        Log.i(LOG_TAG, "TEST.......MovieLoader loadInBackground() called");
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
}

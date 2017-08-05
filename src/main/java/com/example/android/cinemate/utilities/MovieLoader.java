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

    private List<String> mData;

    private String mStringUrl;
    public MovieLoader(Context context, String stringUrl) {
        super(context);
        mStringUrl = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Use cached data
            deliverResult(mData);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
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

    @Override
    public void deliverResult(List<String> data) {
        mData = data;
        super.deliverResult(data);
    }
}

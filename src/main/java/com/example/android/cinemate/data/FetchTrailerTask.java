package com.example.android.cinemate.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 27-Aug-17.
 */

public class FetchTrailerTask extends AsyncTask<String, Void, List<String>> {
    private static final String LOG_TAG = FetchTrailerTask.class.getSimpleName();
    public TrailerAsyncResponse mTrailerAsyncResponse;
    private Context mContext;


    public FetchTrailerTask(TrailerAsyncResponse response, Context context) {

        this.mTrailerAsyncResponse = response;
        this.mContext = context;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        Log.i(LOG_TAG, "TEST..........FetchTrailerTask doInBackGroundCalled()");

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isCancelled()) return null;
        if (isConnected) {
            String i = strings[0];
            String json = NetworkUtils.getDataFromNetwork(i);
            try {
                return MovieJsonUtils.parseTrailerData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> data) {
        /*This if statement will prevent the app from crashing if there is no network*/
        if (data != null) {
            mTrailerAsyncResponse.trailerAsyncResult(data);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.i(LOG_TAG, "CANCELLED................................FetchTrailerTask!");
    }

    public interface TrailerAsyncResponse {
        void trailerAsyncResult(List<String> data);
    }
}

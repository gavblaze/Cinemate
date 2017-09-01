package com.example.android.cinemate.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.android.cinemate.models.MovieParticulars;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

/**
 * Created by Gavin on 31-Aug-17.
 */

public class FetchMovieParticulars extends AsyncTask<String, Void, MovieParticulars> {
    private Context mContext;
    private ParticularsAsyncResponse mParticularsResponse;

    public FetchMovieParticulars(Context context, ParticularsAsyncResponse response) {
        this.mContext = context;
        this.mParticularsResponse = response;
    }

    @Override
    protected MovieParticulars doInBackground(String... strings) {

        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            String i = strings[0];
            String json = NetworkUtils.getDataFromNetwork(i);
            try {
                return MovieJsonUtils.parseMovieParticulars(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(MovieParticulars movieParticulars) {
        super.onPostExecute(movieParticulars);
        if (movieParticulars != null) {
            mParticularsResponse.particularsAsyncResult(movieParticulars);
        }
    }

    public interface ParticularsAsyncResponse {
        void particularsAsyncResult(MovieParticulars result);
    }
}

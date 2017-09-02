package com.example.android.cinemate.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cinemate.adapters.TrailerAdapter;
import com.example.android.cinemate.models.Review;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Gavin on 29-Aug-17.
 */

public class FetchReviewTask extends AsyncTask<String, Void, List<Review>> {
    private static final String LOG_TAG = FetchReviewTask.class.getSimpleName();
    private ReviewAsyncResponse mReviewAsyncResponse;
    private Context mContext;

    public FetchReviewTask(ReviewAsyncResponse response, Context context) {
        mReviewAsyncResponse = response;
        mContext = context;
    }

    @Override
    protected List<Review> doInBackground(String... strings) {
        Log.i(LOG_TAG, "TEST..........FetchReviewTask doInBackGroundCalled()");

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
                return MovieJsonUtils.parseReviewData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Review> review) {
        super.onPostExecute(review);
        if (review != null) {
            mReviewAsyncResponse.reviewAsyncResult(review);
        }
    }

    @Override
    protected void onCancelled() {
        Log.i(LOG_TAG, "CANCELLED................................FetchReviewTask!");
        super.onCancelled();
    }

    public interface ReviewAsyncResponse {
        void reviewAsyncResult(List<Review> data);
    }
}

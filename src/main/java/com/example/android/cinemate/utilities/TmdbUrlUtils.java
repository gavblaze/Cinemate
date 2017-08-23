package com.example.android.cinemate.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.cinemate.R;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class TmdbUrlUtils {

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";

    public static final String BASE_IMAGE_SIZE = "w185";

    public static String electedUrl(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String adoptedPath = sharedPreferences.getString(context.getString(R.string.sort_order_key), context.getString(R.string.sort_order_default_value));

        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(adoptedPath);
        return builder.build().toString();
    }
}

package com.example.android.cinemate.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.cinemate.R;
import com.example.android.cinemate.data.MoviePreferences;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class TmdbUrlUtils {

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";

    public static String urlFromPreferences(Context context) {

        String adoptedPath = MoviePreferences.getValueFromPreferences(context);

        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(adoptedPath);
        return builder.build().toString();
    }
}

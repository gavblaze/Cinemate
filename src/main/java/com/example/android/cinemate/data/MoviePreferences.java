package com.example.android.cinemate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;

import com.example.android.cinemate.Movie;
import com.example.android.cinemate.R;

import java.net.URL;

/**
 * Created by Gavin on 05-Aug-17.
 */

public class MoviePreferences {
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";

    public static String stringUrlFromSharedPreferences(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String sortBy = sharedPrefs.getString(
                context.getString(R.string.sort_order_key),
                context.getString(R.string.sort_order_default_value));

        Uri baseUri = Uri.parse(MOVIE_URL);

        Uri.Builder builder = baseUri.buildUpon();

        builder.appendPath(sortBy);

        return builder.toString();
    }
}

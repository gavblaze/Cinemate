package com.example.android.cinemate.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.cinemate.data.MoviePreferences;

/**
 * Created by Gavin on 09-Aug-17.
 */

public class TmdbUrlUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE = "w185";

    public static String builtUrl(Context context, String pageNumber) {

        String sortBy = MoviePreferences.preferredSortOrder(context);

        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(sortBy);
        builder.appendQueryParameter("page", pageNumber);
        return builder.toString();
    }
}

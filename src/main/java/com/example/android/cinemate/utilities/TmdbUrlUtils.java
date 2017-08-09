package com.example.android.cinemate.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.cinemate.data.MoviePreferences;

/**
 * Created by Gavin on 09-Aug-17.
 */

public class TmdbUrlUtils {
    public static String BASE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";

    public static String stringUrlOfCurrentPage(Context context, String page) {
        String sortBy = MoviePreferences.preferredSortOrder(context);
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(sortBy);
        builder.appendQueryParameter("page", page);
        return builder.toString();
    }

    //public static String preferredStringUrl
}

package com.example.android.cinemate.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.android.cinemate.data.MoviePreferences;

/**
 * Created by Gavin on 20-Aug-17.
 */

public class TmdbUrlUtils {

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US";

    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static final String BASE_IMAGE_SIZE = "w185";


    public static String urlFromPreferences(Context context) {

        String adoptedPath = MoviePreferences.preferenceSelected(context);

        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(adoptedPath);
        return builder.build().toString();
    }

    public static String getImageUrl(String path, String imageSize) {
        Uri baseUri = Uri.parse(BASE_IMAGE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(imageSize);
        /*We use appendEncoded path because the path from JSON
        * is already encoded with a backslash*/
        builder.appendEncodedPath(path);

        String stringUrlOfImage = builder.toString();

        return stringUrlOfImage;
    }
}

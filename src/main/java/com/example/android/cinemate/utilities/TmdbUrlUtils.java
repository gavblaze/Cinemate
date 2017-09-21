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

    public static final String MEDIUM_IMAGE_SIZE = "w342";

    public static final String LARGE_IMAGE_SIZE = "w500";

    public static final String XLARGE_IMAGE_SIZE = "w780";

    public static final String BACKDROP_IMAGE_SIZE = "w780";

    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?";

    public static final String BASE_YOUTUBE_IMG_URL = "http://img.youtube.com/vi/";

    public static final String IMG_SIZE_TYPE = "0.jpg";


//    public static String urlFromPreferences(Context context, String pageNumber) {
//
//        String adoptedPath = MoviePreferences.preferenceSelected(context);
//
//        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
//        Uri.Builder builder = baseUri.buildUpon();
//        builder.appendPath(adoptedPath);
//        builder.appendQueryParameter("page", pageNumber);
//        return builder.build().toString();
//    }

    public static String getUrl(Context context, String path, String pageNumber) {
        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(path);
        builder.appendQueryParameter("page", pageNumber);
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

    public static String getBackdropUrl(String path, String imageSize) {
        Uri baseUri = Uri.parse(BASE_IMAGE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(imageSize);
        /*We use appendEncoded path because the path from JSON
        * is already encoded with a backslash*/
        builder.appendEncodedPath(path);

        String stringUrlOfImage = builder.toString();

        return stringUrlOfImage;
    }

    public static String getTrailerJsonUrl(String movieId) {
        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("videos");
        return builder.build().toString();
    }


    public static String getVideoImgUrl(String path) {
        Uri baseUri = Uri.parse(BASE_YOUTUBE_IMG_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(path);
        builder.appendPath(IMG_SIZE_TYPE);
        return builder.toString();
    }

    public static String getYouTubeUrl(String path) {
        Uri baseUri = Uri.parse(BASE_YOUTUBE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(path);
        return builder.toString();
    }

    public static String getReviewJsonUrl(String movieId) {
        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(movieId);
        builder.appendPath("reviews");
        return builder.build().toString();
    }

    public static String getMovieParticulars(String movieId) {
        Uri baseUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(movieId);
        return builder.build().toString();

    }
}

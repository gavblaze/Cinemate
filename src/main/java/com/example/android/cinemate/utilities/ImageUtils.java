package com.example.android.cinemate.utilities;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Picasso;

/**
 * Created by Gavin on 06-Aug-17.
 */

public class ImageUtils {
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";

    public static String getMovieImage(String path, String imageSize) {
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

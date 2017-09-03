package com.example.android.cinemate.utilities;

import android.widget.TextView;

import com.example.android.cinemate.R;

/**
 * Created by Gavin on 03-Sep-17.
 */

public class GenreUtils {

    public static void genreIcon(String genre, TextView tv) {
        switch (genre) {
            case "Action":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.action, 0, 0, 0);
                break;
            case "Adventure":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.adventure, 0, 0, 0);
                break;
            case "Animation":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.animation, 0, 0, 0);
                break;
            case "Comedy":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comedy, 0, 0, 0);
                break;
            case "Crime":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.crime, 0, 0, 0);
                break;
            case "Documentary":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.documentary, 0, 0, 0);
                break;
            case "Drama":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drama, 0, 0, 0);
                break;
            case "Family":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.family, 0, 0, 0);
                break;
            case "Fantasy":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fantasy, 0, 0, 0);
                break;
            case "History":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.history, 0, 0, 0);
                break;
            case "Horror":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.horror, 0, 0, 0);
                break;
            case "Music":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.music, 0, 0, 0);
                break;
            case "Mystery":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mystery, 0, 0, 0);
                break;
            case "Romance":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.romance, 0, 0, 0);
                break;
            case "Science Fiction":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scifi, 0, 0, 0);
                break;
            case "TV Movie":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tvmovie, 0, 0, 0);
                break;
            case "Thriller":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thriller, 0, 0, 0);
                break;
            case "War":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.war, 0, 0, 0);
                break;
            case "Western":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.western, 0, 0, 0);
                break;
            default:
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.defaulticon, 0, 0, 0);
        }
    }
}

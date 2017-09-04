package com.example.android.cinemate.utilities;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cinemate.R;

/**
 * Created by Gavin on 03-Sep-17.
 */

public class GenreUtils {

    public static void setGenreIcon(String genre, ImageView imageView) {
        switch (genre) {
            case "Action":
                imageView.setImageResource(R.drawable.action);
                break;
            case "Adventure":
                imageView.setImageResource(R.drawable.adventure);
                break;
            case "Animation":
                imageView.setImageResource(R.drawable.animation);
                break;
            case "Comedy":
                imageView.setImageResource(R.drawable.comedy);
                break;
            case "Crime":
                imageView.setImageResource(R.drawable.crime);
                break;
            case "Documentary":
                imageView.setImageResource(R.drawable.documentary);
                break;
            case "Drama":
                imageView.setImageResource(R.drawable.drama);
                break;
            case "Family":
                imageView.setImageResource(R.drawable.family);
                break;
            case "Fantasy":
                imageView.setImageResource(R.drawable.fantasy);
                break;
            case "History":
                imageView.setImageResource(R.drawable.history);
                break;
            case "Horror":
                imageView.setImageResource(R.drawable.horror);
                break;
            case "Music":
                imageView.setImageResource(R.drawable.music);
                break;
            case "Mystery":
                imageView.setImageResource(R.drawable.mystery);
                break;
            case "Romance":
                imageView.setImageResource(R.drawable.romance);
                break;
            case "Science Fiction":
                imageView.setImageResource(R.drawable.scifi);
                break;
            case "TV Movie":
                imageView.setImageResource(R.drawable.tvmovie);
                break;
            case "Thriller":
                imageView.setImageResource(R.drawable.thriller);
                break;
            case "War":
                imageView.setImageResource(R.drawable.war);
                break;
            case "Western":
                imageView.setImageResource(R.drawable.western);
                break;
            default:
                imageView.setImageResource(R.drawable.defaulticon);
        }
    }
}

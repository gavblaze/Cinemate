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

    public static String getValueFromPreferences(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String selectedListPref = sharedPrefs.getString(
                context.getString(R.string.sort_order_key),
                context.getString(R.string.sort_order_default_value));
        return selectedListPref;
    }
}

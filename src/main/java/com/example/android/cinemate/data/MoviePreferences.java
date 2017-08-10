package com.example.android.cinemate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.cinemate.R;

import java.net.URL;

/**
 * Created by Gavin on 05-Aug-17.
 */

public class MoviePreferences {

    public static String preferredSortOrder(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String path = sharedPreferences.getString(context.getString(R.string.sort_order_key), context.getString(R.string.sort_order_default_value));
        return path;
    }
}

package com.example.android.cinemate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.cinemate.R;

/**
 * Created by Gavin on 05-Aug-17.
 */

public class MoviePreferences {

    public static String preferenceSelected(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String selectedListPref = sharedPrefs.getString(
                context.getString(R.string.sort_order_key),
                context.getString(R.string.sort_order_default_value));
        return selectedListPref;
    }
}

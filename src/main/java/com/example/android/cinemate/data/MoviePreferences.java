package com.example.android.cinemate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.cinemate.R;

/**
 * Created by Gavin on 05-Aug-17.
 */

public class MoviePreferences {
    public static final String SORT_BY_KEY = "sort_by";
    public static final String SORT_BY_DEFAULT = "popular";

    public static final String POPULAR_PAGE_COUNT_KEY = "popularPageCount";
    public static final int PAGE_COUNT_DEFAULT = 1;

    public static final String TOPRATED_PAGE_COUNT_KEY = "topRatedPageCount";


//    public static String preferenceSelected(Context context) {
//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
//
//        String selectedListPref = sharedPrefs.getString(
//                context.getString(R.string.sort_order_key),
//                context.getString(R.string.sort_order_default_value));
//        return selectedListPref;
//    }


    public static String sortedBy(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedListPref = sharedPrefs.getString(SORT_BY_KEY, SORT_BY_DEFAULT);
        return selectedListPref;
    }

    public static void setSortBy(Context context, String sortBy) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(SORT_BY_KEY, sortBy);
        editor.apply();
    }

    public static int getPopularPageCount(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int pageCount = sharedPrefs.getInt(POPULAR_PAGE_COUNT_KEY, PAGE_COUNT_DEFAULT);
        return pageCount;
    }

    public static void setPopularPageCount(Context context, int pageCount) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(POPULAR_PAGE_COUNT_KEY, pageCount);
        editor.apply();
    }

    public static int getTopRatedPageCount(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int pageCount = sharedPrefs.getInt(TOPRATED_PAGE_COUNT_KEY, PAGE_COUNT_DEFAULT);
        return pageCount;
    }

    public static void setTopRatedPageCount(Context context, int pageCount) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(TOPRATED_PAGE_COUNT_KEY, pageCount);
        editor.apply();
    }

    synchronized public static void incrementPopularPageCount(Context context) {
        int pageCount = MoviePreferences.getPopularPageCount(context);
        setPopularPageCount(context, ++pageCount);
    }

    synchronized public static void incrementTopRatedPageCount(Context context) {
        int pagecount = MoviePreferences.getTopRatedPageCount(context);
        setTopRatedPageCount(context, ++pagecount);
    }
}

package com.example.android.cinemate.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.cinemate.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.android.cinemate.data.MovieContract.MovieEntry;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieJsonUtils {
    private static final String LOG_TAG = MovieJsonUtils.class.getName();


    public static List<Movie> parseJson(String jsonString) throws JSONException {
        Log.i(LOG_TAG, "TEST.......MovieJsonUtils parseJson() called");
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray("results");

        List<Movie> list = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject o = results.getJSONObject(i);
            String title = o.getString("title");
            String posterPath = o.getString("poster_path");
            String rating = o.getString("vote_average");
            String overView = o.getString("overview");
            String releaseDate = o.getString("release_date");
            int id = o.getInt("id");
            list.add(new Movie(id, title, posterPath, rating, overView, releaseDate));

        }
        return list;
    }
}


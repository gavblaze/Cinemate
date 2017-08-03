package com.example.android.cinemate.utilities;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieJsonUtils {
    private static final String LOG_TAG = MovieJsonUtils.class.getName();


    public static List<String> parseJson (String jsonString) throws JSONException {
        Log.i(LOG_TAG, "TEST.......MovieJsonUtils parseJson() called");
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
       List<String> list = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject o = results.getJSONObject(i);
            String title = o.getString("title");
            list.add(title);
        }
        return list;
    }
}


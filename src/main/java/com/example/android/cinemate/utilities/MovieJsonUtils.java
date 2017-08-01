package com.example.android.cinemate.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieJsonUtils {
    private static final String LOG = MovieJsonUtils.class.getName();
    //private static final String MOVIE_URL = " https://api.themoviedb.org/3/movie/popular?api_key=9bbba1ac9930bbe1a98d6ad3295520a0";
    //public static ArrayList<String> list = new ArrayList<>();


    public static ArrayList<String> parseJson (String stringUrl) throws JSONException {
       ArrayList<String> list = new ArrayList<>();

        JSONObject root = new JSONObject(stringUrl);
        JSONArray results = root.getJSONArray("results");

        for (int i = 0; i < list.size(); i++) {
            JSONObject o = results.getJSONObject(i);
            String title = o.getString("title");
            list.add(title);
        }
        return list;
    }
}


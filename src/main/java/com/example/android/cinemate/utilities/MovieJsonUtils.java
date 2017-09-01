package com.example.android.cinemate.utilities;

import android.text.TextUtils;

import com.example.android.cinemate.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.android.cinemate.models.MovieParticulars;
import com.example.android.cinemate.models.Review;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieJsonUtils {
    private static final String LOG_TAG = MovieJsonUtils.class.getName();

    public static List<Movie> parseJson(String jsonString) throws JSONException {
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

    public static List<String> parseTrailerData(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray("results");

        List<String> list = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject o = results.getJSONObject(i);
            String trailerId = o.getString("id");
            String trailerKey = o.getString("key");
            String trailerName = o.getString("name");
            String trailerSite = o.getString("site");
            String trailerSize = o.getString("size");

            list.add(trailerKey);
        }
        return list;
    }

    public static List<Review> parseReviewData(String jsonString) throws JSONException {
        List<Review> list = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray resultsArray = root.getJSONArray("results");
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject o = resultsArray.getJSONObject(i);
            String reviewAuthor = o.getString("author");
            String reviewContent = o.getString("content");
            list.add(new Review(reviewAuthor, reviewContent));
        }
        return list;
    }

    public static MovieParticulars parseMovieParticulars(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        JSONArray genreArray = root.getJSONArray("genres");
        JSONObject genreObject = genreArray.getJSONObject(0);
        String genre = genreObject.getString("name");
        int runtime = root.getInt("runtime");
        JSONArray languageArray = root.getJSONArray("spoken_languages");
        JSONObject languageObject = languageArray.getJSONObject(0);
        String language = languageObject.getString("name");
        String tagline = root.getString("tagline");
        String homepage = root.getString("homepage");

        return new MovieParticulars(genre, runtime, language, tagline, homepage);
    }
}


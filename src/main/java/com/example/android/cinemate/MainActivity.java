package com.example.android.cinemate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = MainActivity.class.getName();
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=9bbba1ac9930bbe1a98d6ad3295520a0";
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    //private ArrayList<String> mList;

    private String mString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ArrayList<String> movies = new ArrayList<>();
//        movies.add("Scarface");
//        movies.add("Casino");
//        movies.add("Pulp Fiction");
//        movies.add("Oldboy");
//        movies.add("Jackie Brown");
//        movies.add("From Dusk till Dawn");

        //mList = new ArrayList<>();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mString = "Hi";


        //mMovieAdapter = new MovieAdapter(movies);
        //mRecyclerView.setAdapter(mMovieAdapter);




        MovieAsyncTask task = new MovieAsyncTask();
        task.execute(MOVIE_URL);


    }


    public class MovieAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String i = strings[0];
            if (strings.length != 1) {
                throw new IllegalArgumentException("Not a valid Url");
        }

            String rawJsonString = NetworkUtils.getDataFromNetwork(i);
            Log.i(LOG, "TEST...rawJson = " + rawJsonString);

            try {
                JSONObject root = new JSONObject(rawJsonString);
                JSONArray resultsArray = root.getJSONArray("results");
                JSONObject first = resultsArray.getJSONObject(0);
                String title = first.getString("title");
                return title;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);


            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(MainActivity.this);

            mRecyclerView.setLayoutManager(mLayoutManager);

            mMovieAdapter = new MovieAdapter(strings);

            Log.i(LOG, "TEST........" + strings);


            mRecyclerView.setAdapter(mMovieAdapter);


        }
    }
}


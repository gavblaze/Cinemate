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
    private static final String MOVIE_URL = " https://api.themoviedb.org/3/movie/popular?api_key=9bbba1ac9930bbe1a98d6ad3295520a0";
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mList = new ArrayList<>();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieAsyncTask task = new MovieAsyncTask();
        task.execute(MOVIE_URL);

    }


    public class MovieAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String i = strings[0];

            String rawJsonString = NetworkUtils.getDataFromNetwork(i);
            Log.i(LOG, "TEST_STRING..." + rawJsonString);

            try {
                JSONObject root = new JSONObject(rawJsonString);
                JSONArray resultsArray = root.getJSONArray("results");
                for (int j = 0; j < resultsArray.length(); j++) {
                    JSONObject object = resultsArray.getJSONObject(j);
                    String title = object.getString("title");
                    mList.add(title);
                }
                return mList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            mLayoutManager = new LinearLayoutManager(MainActivity.this);

            mRecyclerView.setLayoutManager(mLayoutManager);
            Log.i(LOG, "TEST..." + strings.size());

            mMovieAdapter = new MovieAdapter(strings);

            mMovieAdapter.notifyDataSetChanged();

            mRecyclerView.setAdapter(mMovieAdapter);

        }
    }
}


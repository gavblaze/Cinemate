package com.example.android.cinemate;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.MovieLoader;
import com.example.android.cinemate.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=9bbba1ac9930bbe1a98d6ad3295520a0";
    private static final int LOADER_ID = 333;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.initLoader(LOADER_ID, null, this);

    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreateLoader() called");
        return new MovieLoader(this, MOVIE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMovieAdapter = new MovieAdapter(data);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoaderReset() called");

    }
}


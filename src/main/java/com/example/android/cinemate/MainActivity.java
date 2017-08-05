package com.example.android.cinemate;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.cinemate.utilities.MovieLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>, MovieAdapter.ListItemClickHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=9bbba1ac9930bbe1a98d6ad3295520a0";
    private static final int LOADER_ID = 333;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LoaderManager mLoaderManager;

    private View mLoadingIndicator;
    private TextView mEmptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyTextView = (TextView) findViewById(R.id.emptyStateTextView);

        mLoadingIndicator.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);


        mLoaderManager = getSupportLoaderManager();

        mLoaderManager.initLoader(LOADER_ID, null, this);
    }



    public void showMovieDataView() {
        mEmptyTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }




    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreateLoader() called");

            return new MovieLoader(this, MOVIE_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mLoadingIndicator.setVisibility(View.INVISIBLE);


        if (data == null) {
            showErrorMessage();
        } else {

            showMovieDataView();

            mMovieAdapter.setMovieData(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoaderReset() called");
    }

    @Override
    public void onItemClicked(String title) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.refresh:
                mLoadingIndicator.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(null);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


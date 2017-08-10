package com.example.android.cinemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.utilities.EndlessRecyclerViewScrollListener;
import com.example.android.cinemate.utilities.MovieLoader;
import com.example.android.cinemate.utilities.TmdbUrlUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>, MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private static final int LOADER_ID = 333;
    private final static String KEY = "page_key";
    private static boolean PREFERENCE_CHANGED = false;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;

    private TextView mEmptyTextView;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreate() called");


        //mBundle.putString(KEY, "https://api.themoviedb.org/3/movie/top_rated?api_key=9bbba1ac9930bbe1a98d6ad3295520a0&language=en-US&page=6");
        //mList = new ArrayList<>();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyTextView = (TextView) findViewById(R.id.emptyStateTextView);

        mLoadingIndicator.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);


        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);


        mLoaderManager = getSupportLoaderManager();

        mLoaderManager.initLoader(LOADER_ID, mBundle, this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);


        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    public void loadNextDataFromApi(int offset) {
        Log.i(LOG_TAG, "MainActivity loadNetDataFromApi() called");
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

        String currentPage = String.valueOf(offset);
        Bundle bundle = new Bundle();
        bundle.putString(KEY, currentPage);

        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);
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

        String currentPage;
        // If nothing passed from bundle we are on page 1
        if (args == null) {
            currentPage = "1";
        } else {
            // Else get the page value passed and append it to the query URL
            currentPage = args.getString(KEY);
        }
        return new MovieLoader(this, TmdbUrlUtils.builtUrl(this, currentPage));
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (data == null) {

            showErrorMessage();

        } else {

            showMovieDataView();

            mMovieAdapter.addItems(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoaderReset() called");
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        String item = mMovieAdapter.getPosition(position);
        intent.putExtra(Intent.EXTRA_TEXT, item);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCE_CHANGED = true;
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "TEST.......MainActivity onStart() called");
        super.onStart();
        if (PREFERENCE_CHANGED) {
            mMovieAdapter.setMovieData(null);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            PREFERENCE_CHANGED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }
}


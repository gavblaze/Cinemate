package com.example.android.cinemate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.utilities.MovieLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int LOADER_ID = 333;
    private static boolean PREFERENCE_CHANGED = false;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;
    private TextView mEmptyTextView;

    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

    private Toast mToast;


    /*This is just so we can get reference to our data
    * based on the clicked position*/

    private List<Movie> mMovieList = new ArrayList<>();

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
        mRecyclerView.setHasFixedSize(true);
        mGridLayoutMananger = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutMananger);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mGridLayoutMananger.getItemCount();
                firstVisibleItem = mGridLayoutMananger.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        pageCount++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    int page = pageCount;

                    loading = true;
                }
            }
        });


        mLoaderManager = getSupportLoaderManager();

        mLoaderManager.initLoader(LOADER_ID, null, MainActivity.this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

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
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreateLoader() called");


        return new MovieLoader(this, MoviePreferences.stringUrlFromSharedPreferences(this));
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mMovieAdapter.setMovieData(data);

         /*We declared this so we can initialise the list and
            pass the data to new activity based on the position of item clicked*/
        mMovieList = data;

        if (data == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            showErrorMessage();
        } else {
            showMovieDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoaderReset() called");
        mMovieList.clear();
    }

    private void invalidateData() {
        Log.i(LOG_TAG, "TEST.......MainActivity invalidateData() called");
        mMovieAdapter.setMovieData(null);
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Movie movie = mMovieList.get(position);
        intent.putExtra(Intent.EXTRA_TEXT, movie);
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
                showMovieDataView();
                invalidateData();
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCE_CHANGED = true;
    }

    @Override
    protected void onStart() {
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}


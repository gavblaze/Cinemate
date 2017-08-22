package com.example.android.cinemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.cinemate.data.DataUtils;
import com.example.android.cinemate.data.MovieContract;
import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.data.TmdbUrlUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final int INDEX_MOVIE_ID = 0;


/*In this less than ideal situation while trying to implement the favourites part of the project I realised that
* I will need to save favourites to a Database. In doing so now I would need to query any favourite added and display in the
* Main Activity which displays a List of objects from JSON?
* How do we do this? In this case I would need to query data from a Cursor to display the favourites in the same Main Activity
* I would need 2 x Loader.Callbacks (The link between the LoaderManager & the activity) - Even though in this case I decided
* to make both querys return List objects LoaderCallbacks<List> - to display data from diferrent datasets you need to have the LoaderCallbacks as variables
* rather than call Implement:
*
*  if (preference is favourite) {
*  use thisLoader;
*  else if (preference is other) {
*  use otherLoader
*
**/
public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_OVERVIEW = 2;
    public static final int INDEX_MOVIE_POSTERPATH = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 5;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER = 0;
    private static boolean PREFERENCE_CHANGED = false;
    private static String[] MOVIE_TABLE_PROJECTION = {
            MovieEntry.COLUMN_NAME_ID,
            MovieEntry.COLUMN_NAME_TITLE,
            MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieEntry.COLUMN_NAME_POSTER_PATH,
            MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieEntry.COLUMN_NAME_VOTE_AVERAGE,
            MovieEntry.COLUMN_NAME_SORT_ORDER
    };
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mGridLayoutMananger = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutMananger);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();

        mLoaderManager = getSupportLoaderManager();

        mLoaderManager.initLoader(LOADER, null, this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "TEST.......MainActivity onStart() called");
        super.onStart();
        DataUtils task = new DataUtils(this);
        task.execute(TmdbUrlUtils.electedUrl(this));
        if (PREFERENCE_CHANGED) {
            mMovieAdapter.swapCursor(null);
            getSupportLoaderManager().restartLoader(LOADER, null, MainActivity.this);
        }
        PREFERENCE_CHANGED = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = MovieContract.MovieEntry.COLUMN_NAME_SORT_ORDER + "=?";
        String[] selectionArgs = {MoviePreferences.getValueFromPreferences(this)};
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, MOVIE_TABLE_PROJECTION, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showMovieDataView();
        mMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(LOG_TAG, "TEST.......MainActivity onSharedPreferenceChanged() called");
        PREFERENCE_CHANGED = true;
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Movie movie = mMovieAdapter.getItemClicked(position);
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
                mMovieAdapter.swapCursor(null);
                getSupportLoaderManager().restartLoader(LOADER, null, this);
                return true;
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMovieDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}



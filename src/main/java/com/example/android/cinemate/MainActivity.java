package com.example.android.cinemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.data.FavouritesContract;
import com.example.android.cinemate.data.FavouritesDbHelper;
import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.utilities.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


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

    private static final int LOADER_JSON = 0;
    private static final int LOADER_DB = 1;

    private static boolean PREFERENCE_CHANGED = false;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;
    private TextView mEmptyTextView;

    private SQLiteDatabase mDb;
    private FavouritesDbHelper mDbHelper;

    private Cursor mCursor;


    private String mPref;

    /*This is just so we can get reference to our data
    * based on the clicked position*/

    private List<Movie> mMovieList = new ArrayList<>();


    private LoaderManager.LoaderCallbacks<List<Movie>> movieFromJson = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            Log.i(LOG_TAG, "TEST.......movieFromJson onCreateLoader() called");
            return new MovieLoader(MainActivity.this, MoviePreferences.stringUrlFromSharedPreferences(getApplicationContext()));
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Log.i(LOG_TAG, "TEST.......movieFromJson onLoadFinished() called");
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            mMovieAdapter.setMovieData(data);

         /*We declared this so we can initialise the list and
            pass the data to new activity based on the position of item clicked*/
            mMovieList = data;

            if (data == null) {
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                showErrorMessage();
            } else {
                showMovieDataView();

            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {
            Log.i(LOG_TAG, "TEST.......movieFromJson loaderReset() called");

        }
    };

    private LoaderManager.LoaderCallbacks<List<Movie>> movieFromDb = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        List<Movie> mData;

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            Log.i(LOG_TAG, "TEST.......movieFromDb onCreateLoader() called");

            return new AsyncTaskLoader<List<Movie>>(MainActivity.this) {
                @Override
                protected void onStartLoading() {
                    if (mData != null) {
                        deliverResult(mData);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public List<Movie> loadInBackground() {
                    Log.i(LOG_TAG, "TEST.......movieFromDb loadInBackGround() called");

                    mDb = mDbHelper.getReadableDatabase();
                    ArrayList<Movie> data = new ArrayList<>();
                    mCursor = mDb.query(
                            FavouritesContract.FavouriteEntry.TABLE_NAME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    while (mCursor.moveToNext()) {
                        int id = mCursor.getInt(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry._ID));
                        String title = mCursor.getString(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_NAME_TITLE));
                        String posterPath = mCursor.getString(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_POSTER_PATH));
                        String rating = mCursor.getString(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_NAME_VOTE_AVERAGE));
                        String overview = mCursor.getString(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_NAME_OVERVIEW));
                        String releasedate = mCursor.getString(mCursor.getColumnIndex(FavouritesContract.FavouriteEntry.COLUMN_NAME_RELEASE_DATE));
                        data.add(new Movie(id, title, posterPath, rating, overview, releasedate));
                    }
                    mCursor.close();
                    return data;
                }

                @Override
                public void deliverResult(List<Movie> data) {
                    //Log.i(LOG_TAG, "TEST.......MovieLoader deliverResult() called");
                    mData = data;
                    super.deliverResult(data);
                }
            };


        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            Log.i(LOG_TAG, "TEST.......movieFromDb onLoadFinished() called");
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            mMovieAdapter.setMovieData(data);

         /*We declared this so we can initialise the list and
            pass the data to new activity based on the position of item clicked*/
            mMovieList = data;

            if (data == null) {
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
                showErrorMessage();
            } else {
                showMovieDataView();
            }

        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


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

        mLoaderManager = getSupportLoaderManager();

        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("sort_order_key", "top_rated");

        mDbHelper = new FavouritesDbHelper(this);

        if (mPref.equals(getString(R.string.favourite_value))) {


            mLoaderManager.initLoader(LOADER_DB, null, movieFromDb);

        } else {

            mLoaderManager.initLoader(LOADER_JSON, null, movieFromJson);
        }

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
                getSupportLoaderManager().restartLoader(5, null, movieFromJson);
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
        Log.i(LOG_TAG, "TEST.......MainActivity onSharedPreferenceChanged() called");
        PREFERENCE_CHANGED = true;

        String p = sharedPreferences.getString(key, getString(R.string.most_populary_value));
        if (p.equals(getString(R.string.favourite_value))) {
            mLoaderManager.restartLoader(LOADER_DB, null, movieFromDb);
        } else {
            mLoaderManager.restartLoader(LOADER_JSON, null, movieFromJson);
        }
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "TEST.......MainActivity onStart() called");
        super.onStart();
        //if (PREFERENCE_CHANGED) {
        //    mMovieAdapter.setMovieData(null);
        // }
        // PREFERENCE_CHANGED = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }
}



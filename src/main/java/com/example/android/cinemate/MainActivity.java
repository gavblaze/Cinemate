package com.example.android.cinemate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
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
import android.widget.TextView;

import com.example.android.cinemate.data.DataUtils;
import com.example.android.cinemate.data.MovieContract;
import com.example.android.cinemate.data.MovieDbHelper;
import com.example.android.cinemate.data.TmdbUrlUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {
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

    private static final int LOADER = 0;

    private static boolean PREFERENCE_CHANGED = false;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;
    private TextView mEmptyTextView;

    private SQLiteDatabase mDb;
    private MovieDbHelper mDbHelper;

    private Cursor mCursor;


    private String mPref;

    /*This is just so we can get reference to our data
    * based on the clicked position*/

    //private List<Movie> mMovieList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEmptyTextView = (TextView) findViewById(R.id.emptyStateTextView);

        //mLoadingIndicator.setVisibility(View.VISIBLE);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mGridLayoutMananger = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutMananger);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();

        mLoaderManager = getSupportLoaderManager();

        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("sort_order_key", "top_rated");

        mDbHelper = new MovieDbHelper(this);

        mLoaderManager.initLoader(LOADER, null, this);

        DataUtils task = new DataUtils(this);
        task.execute(TmdbUrlUtils.MOVIE_URL);



        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

    }

//    public void showMovieDataView() {
//        mEmptyTextView.setVisibility(View.INVISIBLE);
//        mRecyclerView.setVisibility(View.VISIBLE);
//    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }


//    private void invalidateData() {
//        Log.i(LOG_TAG, "TEST.......MainActivity invalidateData() called");
//        mMovieAdapter.setMovieData(null);
//    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//        Movie movie = mMovieList.get(position);
//        intent.putExtra(Intent.EXTRA_TEXT, movie);
//        startActivity(intent);
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
                //showMovieDataView();
                //invalidateData();
                getSupportLoaderManager().restartLoader(LOADER, null, this);
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
    }

    private void showMovieDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
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
}



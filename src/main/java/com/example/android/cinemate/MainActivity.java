package com.example.android.cinemate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.cinemate.adapters.MovieAdapter;
import com.example.android.cinemate.data.FetchMovieTask;
import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.models.Movie;
import com.example.android.cinemate.utilities.TmdbUrlUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor>, FetchMovieTask.AsyncTaskResponse {

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_OVERVIEW = 2;
    public static final int INDEX_MOVIE_POSTERPATH = 3;
    public static final int INDEX_MOVIE_BACKDROP_PATH = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 6;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER = 0;
    private final static String KEY = "page_key";
    private static String[] MOVIE_TABLE_PROJECTION = {
            MovieEntry.COLUMN_NAME_ID,
            MovieEntry.COLUMN_NAME_TITLE,
            MovieEntry.COLUMN_NAME_OVERVIEW,
            MovieEntry.COLUMN_NAME_POSTER_PATH,
            MovieEntry.COLUMN_NAME_BACKDROP_PATH,
            MovieEntry.COLUMN_NAME_RELEASE_DATE,
            MovieEntry.COLUMN_NAME_VOTE_AVERAGE,
            MovieEntry.COLUMN_NAME_SORT_ORDER
    };
    private boolean PREFERENCE_CHANGED;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;
    private View mEmptyStateView;
    private Bundle mBundle;

    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int pageCount;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoaderManager = getSupportLoaderManager();


        mLoaderManager.initLoader(LOADER, null, this);


        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //mEmptyStateTextView = (TextView) findViewById(R.id.emptyStateTextView);

        mEmptyStateView = findViewById(R.id.emptyStateView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutMananger = new GridLayoutManager(this, 2);

        } else {
            mGridLayoutMananger = new GridLayoutManager(this, 4);
        }

        mRecyclerView.setLayoutManager(mGridLayoutMananger);


        mMovieAdapter = new MovieAdapter(this, this);

        mRecyclerView.setAdapter(mMovieAdapter);

        showLoading();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        FetchMovieTask task = new FetchMovieTask(this, this);
        task.execute(TmdbUrlUtils.urlFromPreferences(getApplicationContext(), String.valueOf(1)));


    }


    public void loadMore(int page) {
        Log.i(LOG_TAG, "INFO.......MainActivity loadMore() called");

        FetchMovieTask task = new FetchMovieTask(this, this);
        task.execute(TmdbUrlUtils.urlFromPreferences(this, String.valueOf(page)));


        // mMovieAdapter.notifyDataSetChanged();


    }

//    // Append the next page of data into the adapter
//    // This method probably sends out a network request and appends new data items to your adapter.
//    public void loadNextDataFromApi(int offset) {
//        // Send an API request to retrieve appropriate paginated data
//        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
//        //  --> Deserialize and construct new model objects from the API response
//        //  --> Append the new data objects to the existing set of items inside the array of items
//        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
//        Log.i(LOG_TAG, "TEST.......MainActivity loadNextDataFromApi() called");
//
//        FetchMovieTask task = new FetchMovieTask(this, this);
//        task.execute(TmdbUrlUtils.urlFromPreferences(this, String.valueOf(offset)));
//
//
//        mMovieAdapter.notifyDataSetChanged();
//
//        Toast.makeText(this, "Page = " + offset, Toast.LENGTH_SHORT).show();
//
//    }


    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "TEST.......MainActivity onResume() called");
        super.onResume();

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mGridLayoutMananger.getItemCount();
                firstVisibleItem = mGridLayoutMananger.findFirstVisibleItemPosition();


                Log.i(LOG_TAG, "INFO....visibleItemCount = " + visibleItemCount);
                Log.i(LOG_TAG, "INFO....totalItemCount = " + totalItemCount);
                Log.i(LOG_TAG, "INFO....firstVisibleItem = " + firstVisibleItem);


                pageCount = sp.getInt("page_count", 1);

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        Log.i(LOG_TAG, "INFO.....................previousTotal = " + previousTotal);
                        pageCount++;

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("page_count", pageCount);
                        editor.apply();

                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    Toast.makeText(getApplicationContext(), "page = " + pageCount, Toast.LENGTH_SHORT).show();
                    loadMore(pageCount);
                    Log.i(LOG_TAG, "INFO.....................pageCount = " + pageCount);

                    loading = true;

                }
            }
        });

//        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mGridLayoutMananger) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                loadNextDataFromApi(page);
//
//            }
//        };mRecyclerView.addOnScrollListener(scrollListener);

        if (PREFERENCE_CHANGED) {
//            visibleItemCount = mRecyclerView.getChildCount();
//            totalItemCount = mGridLayoutMananger.getItemCount();
//            firstVisibleItem = mMovieAdapter.getItemCount();

            mLoaderManager.restartLoader(LOADER, null, this);
        }
        PREFERENCE_CHANGED = false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreateLoader() called");

//        FetchMovieTask task = new FetchMovieTask(this, this);
//        task.execute(TmdbUrlUtils.urlFromPreferences(getApplicationContext(), String.valueOf(pageCount)));

        String selection;
        String[] selectionArgs;

        /*If ListPreference is an instance of "Favourite" query the favourites column of db*/
        if (MoviePreferences.preferenceSelected(this).equals(getString(R.string.favourite_value))) {
            selection = MovieEntry.COLUMN_NAME_FAVOURITE + "=?";
            selectionArgs = new String[]{String.valueOf(MovieEntry.IS_FAVOURITE)};
        } else {
            /*If ListPreference is an instance of "Popular or Top Rated" query the sort_order column of the db*/
            selection = MovieEntry.COLUMN_NAME_SORT_ORDER + "=?";
            selectionArgs = new String[]{MoviePreferences.preferenceSelected(this)};
        }
        return new CursorLoader(this, MovieEntry.CONTENT_URI, MOVIE_TABLE_PROJECTION, selection, selectionArgs, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mMovieAdapter.swapCursor(data);

        showMovieDataView();

        if (mMovieAdapter.getItemCount() == 0 && MoviePreferences.preferenceSelected(this).equals(getString(R.string.favourite_value))) {
            showErrorMessage();
        }
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
    public void onItemClicked(int position, ImageView sharedImageView) {

        Log.i(LOG_TAG, "TEST.......MainActivity onItemClicked() called");

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Movie movie = mMovieAdapter.getItemClicked(position);
        intent.putExtra(Intent.ACTION_MAIN, movie);
        intent.putExtra(Intent.EXTRA_TEXT, movie.getmTitle());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, sharedImageView, movie.getmTitle());
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_spinner, android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = (Spinner) item.getActionView();
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false); //stops onItemSelected firing on initialisation
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String j = spinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "selcted: " + j, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                return false;
        }
    }

    private void showMovieDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        //mEmptyStateTextView.setVisibility(View.INVISIBLE);
        mEmptyStateView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        //mEmptyStateTextView.setVisibility(View.INVISIBLE);
        mEmptyStateView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        //mEmptyStateTextView.setVisibility(View.VISIBLE);
        mEmptyStateView.setVisibility(View.VISIBLE);

    }

    public boolean zipInDbForPopularOrTopRatedPrefs() {
        String selection = MovieEntry.COLUMN_NAME_SORT_ORDER + "=?";
        String[] selectionArgs = new String[]{MoviePreferences.preferenceSelected(this)};
        Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI, MOVIE_TABLE_PROJECTION, selection, selectionArgs, null);
        if (cursor.getCount() <= 0) {
            return true;
        } else {
            cursor.close();
            return false;
        }
    }


    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "TEST.......MainActivity onDestroy() called");
        super.onDestroy();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    /*This method from the AsyncTask interface allows us to get the value from onPostExecute()*/
    public void asyncTaskResult(List<Movie> data) {
        //Log.i(LOG_TAG, "TEST.......MainActivity asyncTaskResult() called");
        /*We initialize our loader here - after we have received our value from FetchMovie AsyncTask
        * so we can be sure onLoadFinished() is called AFTER doInBackground and not before
        *
        * When our loader was initialised in onCreate() it's onLoadFinished was
        * being called twice (before doInBackground was complete and after)*/


        /*NOT IMPLEMENTED*/

    }
}



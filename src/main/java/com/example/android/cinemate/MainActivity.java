package com.example.android.cinemate;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.cinemate.adapters.MovieAdapter;
import com.example.android.cinemate.data.FetchMovieTask;
import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.models.Movie;
import com.example.android.cinemate.utilities.FavouriteUtils;
import com.example.android.cinemate.utilities.TmdbUrlUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickHandler, SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_OVERVIEW = 2;
    public static final int INDEX_MOVIE_POSTERPATH = 3;
    public static final int INDEX_MOVIE_BACKDROP_PATH = 4;
    public static final int INDEX_MOVIE_RELEASE_DATE = 5;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 6;
    public final static String SORT_KEY = "page_key";
    public final static String DEFAULT = "popular";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER = 0;
    private final static String INDEX_KEY = "index";
    private final static String FIRST_VISIBLE_KEY = "first_visible";
    private final static String TOTAL_ITEM_COUNT = "total_item_count";
    private final static String VISIBLE_ITEM_COUNT = "visible_item_count";
    private final static int INDEX_DEFAULT = 0;
    private static final String FAVOURITES = "favourites";
    private static final String MOST_POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
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
    //private String mSortBy = DEFAULT;
    private boolean PREFERENCE_CHANGED;
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutMananger;
    private LoaderManager mLoaderManager;
    private View mLoadingIndicator;
    private View mEmptyStateView;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int popularPageCount;
    private int topRatedPageCount;
    private Spinner mSpinner;
    private int mPage;
    private boolean userIsInteracting;
    private Context mContext;
    private Movie mMovie;
    private ImageView mLikeImageView;
    private ImageButton mShareImageButton;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = getApplicationContext();


//        ImageButton mLikeImageButton = (ImageButton) findViewById(R.id.likeImageView);
//        mShareImageButton = (ImageButton) findViewById(R.id.shareImageView);

//        if (savedInstanceState != null) {
//            firstVisibleItem = savedInstanceState.getInt(FIRST_VISIBLE_KEY);
//            previousTotal = savedInstanceState.getInt("previousTotal");
//        }

        if (dbIsEmpty()) {

            FetchMovieTask fetchPopular = new FetchMovieTask(this, MOST_POPULAR);
            fetchPopular.execute(TmdbUrlUtils.getUrl(this, MOST_POPULAR, String.valueOf(1)));

            FetchMovieTask fetchTopRated = new FetchMovieTask(this, TOP_RATED);
            fetchTopRated.execute(TmdbUrlUtils.getUrl(this, TOP_RATED, String.valueOf(1)));
        }



        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);

        mEmptyStateView = findViewById(R.id.emptyStateView);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMain);
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

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mLoaderManager = getSupportLoaderManager();


        mLoaderManager.initLoader(LOADER, null, this);

        mLikeImageView = (ImageView) findViewById(R.id.likeImageView);


        popularPageCount = MoviePreferences.getPopularPageCount(this);
        topRatedPageCount = MoviePreferences.getTopRatedPageCount(this);


        setOnScrollListener();


    }


    public void loadMore(int page) {
        Log.i(LOG_TAG, "INFO.......MainActivity loadMore() called");

        FetchMovieTask task = new FetchMovieTask(this, MoviePreferences.getSortedBy(this));
        task.execute(TmdbUrlUtils.getUrl(this, MoviePreferences.getSortedBy(this), String.valueOf(page)));

        mMovieAdapter.notifyDataSetChanged();

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        Log.i(LOG_TAG, "TEST.......MainActivity onSaveInstanceState() called");
//        super.onSaveInstanceState(outState);
//        outState.putInt(FIRST_VISIBLE_KEY, firstVisibleItem);
//        outState.putInt("previousTotal", previousTotal);
//    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "INFO.......MainActivity onResume() called");
        super.onResume();

//        if (MoviePreferences.getSortedBy(this).equals(MOST_POPULAR)) {
//            resetScrollListener(0, true, popularPageCount);
//        } else if (MoviePreferences.getSortedBy(this).equals(TOP_RATED)) {
//            resetScrollListener(0, true, topRatedPageCount);
//        } else {
//            reloadData();
//        }
        resetScrollListener(0, true, 1);
    }


    public void reloadData() {
        mMovieAdapter.swapCursor(null);
        mLoaderManager.restartLoader(LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST.......MainActivity onCreateLoader() called");

        String selection;
        String[] selectionArgs;

        if (MoviePreferences.getSortedBy(mContext).equals(FAVOURITES)) {

            selection = MovieEntry.COLUMN_NAME_FAVOURITE + "=?";
            selectionArgs = new String[]{String.valueOf(MovieEntry.IS_FAVOURITE)};

        } else {

            selection = MovieEntry.COLUMN_NAME_SORT_ORDER + "=?";

            selectionArgs = new String[]{MoviePreferences.getSortedBy(this)};
        }
        return new CursorLoader(this, MovieEntry.CONTENT_URI, MOVIE_TABLE_PROJECTION, selection, selectionArgs, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(LOG_TAG, "TEST.......MainActivity onLoadFinished() called");

        mMovieAdapter.swapCursor(data);

        showMovieDataView();

        if (mMovieAdapter.getItemCount() == 0 && MoviePreferences.getSortedBy(this).equals(FAVOURITES)) {
            showNoFavouritesMessage();
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
    public void onLikeClicked(int position) {
        mMovie = mMovieAdapter.getItemClicked(position);
        /*If movie clicked is NOT yet a favourite add it to favourites in database*/
        if (!FavouriteUtils.isFavourite(this, mMovie.getmId())) {
            FavouriteUtils.setToFavouriteInDb(this, mMovie.getmId());
            Snackbar.make(findViewById(R.id.recyclerViewMain), mMovie.getmTitle() + " added to favourites!", Snackbar.LENGTH_SHORT).show();
        } else {
            FavouriteUtils.setToDefaultInDb(this, mMovie.getmId());
            Snackbar.make(findViewById(R.id.recyclerViewMain), "Removed from favourites", Snackbar.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onShareClicked(int position) {
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_spinner, R.layout.spinner_dropdown_item);
        mSpinner = (Spinner) item.getActionView();
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(MoviePreferences.getSavedSpinnerIndex(mContext));  // declared this method to save the spinner index to shared preferences

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(LOG_TAG, "TEST....onItemSelected() called!!!! ");

                if (userIsInteracting) {


                    int index = mSpinner.getSelectedItemPosition();
                    String sortBy = getTitleOfItemSelected(index);

                    resetScrollListener(0, true, 1); // reset the scrollListener back to true if the user changes preference

//                    if (mSortBy.equals(MOST_POPULAR)) {
//                        resetScrollListener(0, true, popularPageCount);
//                    } else if (mSortBy.equals(TOP_RATED)) {
//                        resetScrollListener(0, true, topRatedPageCount);
//                    }

//                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putInt(INDEX_KEY, index);
//                    editor.putString(SORT_KEY, mSortBy);
//                    Log.i(LOG_TAG, "TEST....We are on: " + mSortBy);
//
//                    editor.apply();

                    MoviePreferences.setSpinnerIndex(mContext, index);
                    MoviePreferences.setSortBy(mContext, sortBy);


                    reloadData();
                }
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
//            case R.id.refresh:
//                mLoadingIndicator.setVisibility(View.VISIBLE);
//                mMovieAdapter.swapCursor(null);
//                getSupportLoaderManager().restartLoader(LOADER, null, this);
//                return true;
//            case R.id.settings:
//                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                startActivity(intent);
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

    private void showNoFavouritesMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        //mEmptyStateTextView.setVisibility(View.VISIBLE);
        mEmptyStateView.setVisibility(View.VISIBLE);

    }

    public boolean dbIsEmpty() {
        String selection = MovieEntry.COLUMN_NAME_SORT_ORDER + "=?";
        String[] selectionArgs = new String[]{MoviePreferences.getSortedBy(this)};
        Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI, MOVIE_TABLE_PROJECTION, selection, selectionArgs, null);
        if ((cursor != null ? cursor.getCount() : 0) <= 0) {
            return true;
        } else {
            assert cursor != null;
            cursor.close();
            return false;
        }
    }



    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "TEST.......MainActivity onDestroy() called");
        super.onDestroy();
        //mRecyclerView.clearOnScrollListeners();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }


    public String getTitleOfItemSelected(int index) {
//        int index = spinner.getSelectedItemPosition();
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt(INDEX_KEY, index);
//        editor.apply();
        //MoviePreferences.setSpinnerIndex(mContext, index);

        switch (index) {
            case 0:
                return MOST_POPULAR;
            case 1:
                return TOP_RATED;
            case 2:
                return FAVOURITES;
            default:
                return null;
        }
    }

//    public void getSavedSpinnerIndex() {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        int position = sp.getInt(INDEX_KEY, INDEX_DEFAULT);
//        mSpinner.setSelection(position);
//    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    public void setOnScrollListener() {


        Log.i(LOG_TAG, "TEST.......MainActivity setOnScrollListener() called");
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
                        Log.i(LOG_TAG, "INFO..........................previousTotal = " + previousTotal);
                        if (MoviePreferences.getSortedBy(mContext).equals(MOST_POPULAR)) {

                            MoviePreferences.setPopularPageCount(getApplicationContext(), popularPageCount);
                            popularPageCount++;

                        } else if (MoviePreferences.getSortedBy(mContext).equals(TOP_RATED)) {

                            MoviePreferences.setTopRatedPageCount(getApplicationContext(), topRatedPageCount);
                            topRatedPageCount++;
                        }
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

                    if (MoviePreferences.getSortedBy(mContext).equals(MOST_POPULAR)) {

                        loadMore(popularPageCount);
                        Log.i(LOG_TAG, "INFO.....................pop pageCount = " + popularPageCount);
                        //Toast.makeText(getApplicationContext(), "popular page count = " + popularPageCount, Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(R.id.recyclerViewMain), "Loading page " + popularPageCount, Snackbar.LENGTH_SHORT).show();
                        loading = true;


                    } else if (MoviePreferences.getSortedBy(mContext).equals(TOP_RATED)) {

                        loadMore(topRatedPageCount);
                        Log.i(LOG_TAG, "INFO.....................top pageCount = " + topRatedPageCount);
                        //Toast.makeText(getApplicationContext(), "top rated page count = " + topRatedPageCount, Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(R.id.recyclerViewMain), "Loading page " + topRatedPageCount, Snackbar.LENGTH_SHORT).show();
                        loading = true;
                    }
                }
            }
        });
    }

        /*
    Because after you pause the activity and resume again, the condition
    !loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)
    is always false, thus onLoadMore is never called.*/

    public void resetScrollListener(int previousTotal, boolean loading, int page) {
        this.previousTotal = previousTotal;
        this.loading = loading;
        this.mPage = page;
    }
}



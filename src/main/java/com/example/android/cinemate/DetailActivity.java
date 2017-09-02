package com.example.android.cinemate;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.adapters.ReviewAdapter;
import com.example.android.cinemate.adapters.TrailerAdapter;
import com.example.android.cinemate.data.FetchMovieParticulars;
import com.example.android.cinemate.data.FetchReviewTask;
import com.example.android.cinemate.data.FetchTrailerTask;
import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.models.Movie;
import com.example.android.cinemate.models.MovieParticulars;
import com.example.android.cinemate.models.Review;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickHandler, FetchTrailerTask.TrailerAsyncResponse, FetchReviewTask.ReviewAsyncResponse, FetchMovieParticulars.ParticularsAsyncResponse {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final String TRAILER_KEY = "trailer_key";
    private static final String REVIEW_KEY = "review_key";
    private TextView mDetailMovieTitle;
    private TextView mDetailMovieOverView;
    private TextView mDetailMovieRating;
    private TextView mDetailMovieReleaseDate;
    private ImageView mDetailMovieImageView;
    private FloatingActionButton mFab;
    private String mUrlPosterPath;
    private Movie mReceivedMovie;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private GridLayoutManager mGridLayoutManager;
    private TextView mTrailerLabelTextView;
    private TextView mReviewLabelTextView;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<String> mTrailerList;
    private ArrayList<Review> mReviewList;

    private TextView mTagLineTextView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST.......DetailActivity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailMovieImageView = (ImageView) findViewById(R.id.detailMovieImageView);

        supportPostponeEnterTransition();


        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);

        mTrailerAdapter = new TrailerAdapter(this, this);

        mTrailerRecyclerView.setLayoutManager(mGridLayoutManager);

        mTrailerRecyclerView.setAdapter(mTrailerAdapter);


        mReviewRecyclerView = (RecyclerView) findViewById(R.id.reviewRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mReviewAdapter = new ReviewAdapter();

        mReviewRecyclerView.setLayoutManager(mLinearLayoutManager);

        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setNestedScrollingEnabled(false);


        mTagLineTextView = (TextView) findViewById(R.id.detailTagline);

        mDetailMovieTitle = (TextView) findViewById(R.id.detailTitle);
        mDetailMovieOverView = (TextView) findViewById(R.id.detailOverview);
        mDetailMovieRating = (TextView) findViewById(R.id.detailRating);
        mDetailMovieReleaseDate = (TextView) findViewById(R.id.detailYear);
        mDetailMovieImageView = (ImageView) findViewById(R.id.detailMovieImageView);


        mTrailerLabelTextView = (TextView) findViewById(R.id.trailersLabelTextView);
        mReviewLabelTextView = (TextView) findViewById(R.id.reviewLabelTextView);




        Intent intentThatStartedActivity = getIntent();

        mReceivedMovie = intentThatStartedActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        mDetailMovieTitle.setText(mReceivedMovie.getmTitle());
        mDetailMovieOverView.setText(mReceivedMovie.getmOverview());
        mDetailMovieRating.setText(mReceivedMovie.getmRating());
        mDetailMovieReleaseDate.setText(mReceivedMovie.getmReleaseDate());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = intentThatStartedActivity.getStringExtra(Intent.ACTION_MAIN);
            mDetailMovieImageView.setTransitionName(transitionName);
        }

        mUrlPosterPath = mReceivedMovie.getmPosterPath();
        String urlForImage = TmdbUrlUtils.getImageUrl(mUrlPosterPath, TmdbUrlUtils.BASE_IMAGE_SIZE);
        Picasso.with(mDetailMovieImageView.getContext()).load(urlForImage).noFade().into(mDetailMovieImageView, new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }
        });

        String movieId = String.valueOf(mReceivedMovie.getmId());

        FetchMovieParticulars particlarsTask = new FetchMovieParticulars(this, this);
        particlarsTask.execute(TmdbUrlUtils.getMovieParticulars(movieId));


        if (savedInstanceState != null && savedInstanceState.containsKey(TRAILER_KEY)) {
            showTrailerView();
            mTrailerList = savedInstanceState.getStringArrayList(TRAILER_KEY);
            mTrailerAdapter.setData(mTrailerList);
        } else {
            FetchTrailerTask trailerTask = new FetchTrailerTask(this, this);
            trailerTask.execute(TmdbUrlUtils.getTrailerJsonUrl(movieId));
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(REVIEW_KEY)) {
            showReviewView();
            mReviewList = savedInstanceState.getParcelableArrayList(REVIEW_KEY);
            mReviewAdapter.setData(mReviewList);
        } else {
            FetchReviewTask reviewTask = new FetchReviewTask(this, this);
            reviewTask.execute(TmdbUrlUtils.getReviewJsonUrl(movieId));
        }



        mFab = (FloatingActionButton) findViewById(R.id.fab);

        if (isFavourite(mReceivedMovie.getmId())) {
            mFab.setImageResource(R.drawable.ic_favorite_black_24dp);
        }


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavourite(mReceivedMovie.getmId())) {
                    mFab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    setToFavouriteInDb();
                    Toast.makeText(getApplication(), "Added to favourites", Toast.LENGTH_SHORT).show();
                } else {
                    mFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    setToDefaultInDb();
                    Toast.makeText(getApplication(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "TEST.......DetailActivity onPause() called");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "TEST.......DetailActivity onSaveInstanceState() called");

        if (mTrailerList.size() != 0) outState.putStringArrayList(TRAILER_KEY, mTrailerList);

        if (mReviewList.size() != 0) outState.putParcelableArrayList(REVIEW_KEY, mReviewList);

        super.onSaveInstanceState(outState);

    }

    public boolean isFavourite(int movieId) {
        Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        String[] projection = {MovieEntry.COLUMN_NAME_FAVOURITE};
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
        cursor.moveToNext();
        int indexOfValue = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_FAVOURITE);
        int value = cursor.getInt(indexOfValue);
        if (value == MovieEntry.IS_FAVOURITE) {
            return true;
        } else {
            return false;
        }
    }

    public void setToFavouriteInDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_NAME_FAVOURITE, MovieEntry.IS_FAVOURITE);
        Uri uriToUpdate = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, mReceivedMovie.getmId());
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uriToUpdate))};
        getContentResolver().update(MovieEntry.CONTENT_URI, contentValues, selection, selectionArgs);
    }

    public void setToDefaultInDb() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_NAME_FAVOURITE, MovieEntry.IS_DEFAULT);
        Uri uriToUpdate = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, mReceivedMovie.getmId());
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uriToUpdate))};
        getContentResolver().update(MovieEntry.CONTENT_URI, contentValues, selection, selectionArgs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                shareMovie();
                return true;
            case R.id.detail_settings:
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareMovie() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mDetailMovieOverView.getText());
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {
        String trailerUrlPath = mTrailerAdapter.getItemClicked(position);
        Toast.makeText(this, "Item clicked " + trailerUrlPath, Toast.LENGTH_SHORT).show();
        String youTubeUrl = TmdbUrlUtils.getYouTubeUrl(trailerUrlPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youTubeUrl));
        startActivity(intent);
    }

    @Override
    /*If the result from the TrailerAsyncTask is null, hide the views related to displaying trailers*/
    public void trailerAsyncResult(List<String> data) {
        mTrailerList = (ArrayList<String>) data;
        if (data.isEmpty()) {
            hideTrailerView();
        } else {
            showTrailerView();
            mTrailerAdapter.setData(data);
        }
    }

    public void hideTrailerView() {
        mTrailerLabelTextView.setVisibility(View.GONE);
        mTrailerRecyclerView.setVisibility(View.GONE);
    }

    public void showTrailerView() {
        mTrailerLabelTextView.setVisibility(View.VISIBLE);
    }

    @Override
    /*If the result from the ReviewAsyncTask is null, hide the views related to displaying reviews*/
    public void reviewAsyncResult(List<Review> data) {
        mReviewList = (ArrayList<Review>) data;
        if (data.isEmpty()) {
            hideReviewView();
        } else {
            showReviewView();
            mReviewAdapter.setData(data);
        }
    }

    public void hideReviewView() {
        mReviewLabelTextView.setVisibility(View.GONE);
        mReviewRecyclerView.setVisibility(View.GONE);
    }

    public void showReviewView() {
        mReviewLabelTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void particularsAsyncResult(MovieParticulars result) {
        String tagline = result.getmTagLine();
        if (tagline.isEmpty()) {
            mTagLineTextView.setVisibility(View.GONE);
        } else {
            mTagLineTextView.setText(tagline);
        }
    }
}


package com.example.android.cinemate;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.android.cinemate.data.FetchTrailerTask;
import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.utilities.MovieJsonUtils;
import com.example.android.cinemate.utilities.NetworkUtils;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickHandler {
    private static String LOG_TAG = DetailActivity.class.getSimpleName();


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.trailerRecyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);


        mTrailerAdapter = new TrailerAdapter(this);

        mTrailerRecyclerView.setLayoutManager(mGridLayoutManager);

        mTrailerRecyclerView.setAdapter(mTrailerAdapter);


        mDetailMovieTitle = (TextView) findViewById(R.id.detailTitle);
        mDetailMovieOverView = (TextView) findViewById(R.id.detailOverview);
        mDetailMovieRating = (TextView) findViewById(R.id.detailRating);
        mDetailMovieReleaseDate = (TextView) findViewById(R.id.detailYear);
        mDetailMovieImageView = (ImageView) findViewById(R.id.detailMovieImageView);

        Intent intentThatStartedActivity = getIntent();

        mReceivedMovie = intentThatStartedActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        mDetailMovieTitle.setText(mReceivedMovie.getmTitle());
        mDetailMovieOverView.setText(mReceivedMovie.getmOverview());
        mDetailMovieRating.setText(mReceivedMovie.getmRating());
        mDetailMovieReleaseDate.setText(mReceivedMovie.getmReleaseDate());

        mUrlPosterPath = mReceivedMovie.getmPosterPath();
        String urlForImage = TmdbUrlUtils.getImageUrl(mUrlPosterPath, TmdbUrlUtils.BASE_IMAGE_SIZE);
        Picasso.with(mDetailMovieImageView.getContext()).load(urlForImage).into(mDetailMovieImageView);

        String movieId = String.valueOf(mReceivedMovie.getmId());

        FetchTrailerTask task = new FetchTrailerTask();
        task.execute(TmdbUrlUtils.getTrailerJsonUrl(movieId));


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

    public class FetchTrailerTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            String i = strings[0];
            String json = NetworkUtils.getDataFromNetwork(i);
            try {
                return MovieJsonUtils.parseTrailerData(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            //super.onPostExecute(strings);
            mTrailerAdapter.setData(strings);
        }
    }
}


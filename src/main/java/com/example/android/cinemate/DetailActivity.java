package com.example.android.cinemate;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;
    private TextView mLanguageTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private FloatingActionButton mFab;

    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.detailTitle);
        mOverviewTextView = (TextView) findViewById(R.id.detailOverview);
        mPosterImageView = (ImageView) findViewById(R.id.detailMoviePoster);
        mReleaseDateTextView = (TextView) findViewById(R.id.detailYear);
        mRatingTextView = (TextView) findViewById(R.id.detailRating);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity.hasExtra(Intent.ACTION_MAIN)) {
            mMovie = intentThatStartedActivity.getExtras().getParcelable(Intent.ACTION_MAIN);

            Uri baseUri = Uri.parse(TmdbUrlUtils.BASE_POSTER_URL);
            Uri.Builder builder = baseUri.buildUpon();
            builder.appendPath(TmdbUrlUtils.POSTER_SIZE);
            builder.appendEncodedPath(mMovie.getmPosterPath());
            String posterUrl = builder.toString();

            Context context = mPosterImageView.getContext();

            mTitleTextView.setText(mMovie.getmTitle());
            mOverviewTextView.setText(mMovie.getmOverView());
            Picasso.with(this)
                    .load(posterUrl)
                    .into(mPosterImageView);
            mReleaseDateTextView.setText(mMovie.getmReleaseDate());
            mRatingTextView.setText(mMovie.getmVoteAverage());

            Picasso.with(context).invalidate(posterUrl);

        }


        mFab = (FloatingActionButton) findViewById(R.id.fab);

        if (isInDatabase(mMovie.getmId())) {
            mFab.setImageResource(R.drawable.ic_star_black_24dp);
        }


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInDatabase(mMovie.getmId())) {
                    mFab.setImageResource(R.drawable.ic_star_black_24dp);
                    addToFavourites();
                    Toast.makeText(getApplicationContext(), "Items in db: " + getCount(), Toast.LENGTH_SHORT).show();
                } else {
                    mFab.setImageResource(R.drawable.ic_star_border_black_24dp);
                    deleteFromFavourites(mMovie.getmId());
                    Toast.makeText(getApplicationContext(), "Items in db: " + getCount(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isInDatabase(int id) {

        String[] projection = {MovieEntry.COLUMN_NAME_ID};
        String selection = MovieEntry.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor != null) {
            cursor.close();
        }
        return ((cursor != null ? cursor.getCount() : 0) > 0);
    }


    public void addToFavourites() {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_NAME_ID, mMovie.getmId());
        values.put(MovieEntry.COLUMN_NAME_TITLE, mMovie.getmTitle());
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, mMovie.getmOverView());
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, mMovie.getmPosterPath());
        values.put(MovieEntry.COLUMN_NAME_LANGUAGE, mMovie.getmLanguage());
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, mMovie.getmReleaseDate());
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, mMovie.getmVoteAverage());
        getContentResolver().insert(MovieEntry.CONTENT_URI, values);
    }

    public void deleteFromFavourites(int id) {
        String selection = MovieEntry.COLUMN_NAME_ID;
        String[] selectionArgs = {String.valueOf(id)};
        getContentResolver().delete(ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id), selection, selectionArgs);
    }

    public int getCount() {
        Cursor cursor = getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
        intent.putExtra(Intent.EXTRA_TEXT, mTitleTextView.getText());
        intent.setType("text/plain");
        startActivity(intent);
    }
}

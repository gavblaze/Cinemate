package com.example.android.cinemate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cinemate.data.MovieContract.MovieEntry;
import com.example.android.cinemate.data.MovieDbHelper;
import com.example.android.cinemate.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String BASE_IMAGE_SIZE = "w185";
    private TextView mDetailMovieTitle;
    private TextView mDetailMovieOverView;
    private TextView mDetailMovieRating;
    private TextView mDetailMovieReleaseDate;
    private ImageView mDetailMovieImageView;
    private FloatingActionButton mFab;
    private SQLiteDatabase mDb;
    private MovieDbHelper mDbHelper;

    private String mUrlPosterPath;

    private Movie mReceivedMovie;

//    public static List<Movie> getFavourites(Context context) {
//        ArrayList<Movie> data = new ArrayList<>();
//        //mDb = mDbHelper.getReadableDatabase();
//        MovieDbHelper dbHelper = new MovieDbHelper(context);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//
//        // Define a projection that specifies which columns from the database
//// you will actually use after this query.
//        String[] projection = {
//                FavouriteEntry.COLUMN_POSTER_PATH
//        };
//
//        Cursor cursor = db.query(
//                FavouriteEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        Movie movie = null;
//        while (cursor.moveToNext()) {
//            data.add(movie);
//        }
//        return data;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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


        // Create the DB helper (this will create the database if run for the first time)
        mDbHelper = new MovieDbHelper(this);
        // Get the repository in write mode
        mDb = mDbHelper.getWritableDatabase();


        mUrlPosterPath = mReceivedMovie.getmPosterPath();
        String urlForImage = ImageUtils.getMovieImage(mUrlPosterPath, BASE_IMAGE_SIZE);
        Picasso.with(mDetailMovieImageView.getContext()).load(urlForImage).into(mDetailMovieImageView);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

//        if (isInDatabase(mReceivedMovie.getmId())) {
//            mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
//        } else {
//            mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
//        }


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isInDatabase(mReceivedMovie.getmId())) {
//                    mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
//                    deleteFromFavouritesDb(mReceivedMovie.getmId());
//                    Toast.makeText(getApplicationContext(), "Items in db = " + getCount(), Toast.LENGTH_SHORT).show();
//                } else {
//                    addToFavouritesDb();
//                    mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
//                    Toast.makeText(getApplicationContext(), "Items in db = " + getCount(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

//    public long addToFavouritesDb() {
//        int id = mReceivedMovie.getmId();
//        String title = mDetailMovieTitle.getText().toString();
//        String overview = mDetailMovieOverView.getText().toString();
//        String posterpath = mUrlPosterPath;
//        String releasedate = mDetailMovieReleaseDate.getText().toString();
//        String voteaverage = mDetailMovieRating.getText().toString();
//
//        ContentValues values = new ContentValues();
//        values.put(FavouriteEntry._ID, id);
//        values.put(FavouriteEntry.COLUMN_NAME_TITLE, title);
//        values.put(FavouriteEntry.COLUMN_NAME_OVERVIEW, overview);
//        values.put(FavouriteEntry.COLUMN_POSTER_PATH, posterpath);
//        values.put(FavouriteEntry.COLUMN_NAME_RELEASE_DATE, releasedate);
//        values.put(FavouriteEntry.COLUMN_NAME_VOTE_AVERAGE, voteaverage);
//
//        long newRowId = mDb.insert(FavouriteEntry.TABLE_NAME, null, values);
//
//        return newRowId;
//    }
//
//    public void deleteFromFavouritesDb(int id) {
//        String selection = FavouriteEntry._ID + " = ?";
//        String[] selectionArgs = {String.valueOf(id)};
//        mDb.delete(FavouriteEntry.TABLE_NAME, selection, selectionArgs);
//    }
//
//    public boolean isInDatabase(int id) {
//        mDb = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//// you will actually use after this query.
//        String[] projection = {
//                FavouriteEntry._ID
//        };
//        String selection = FavouriteEntry._ID + " = ?";
//        String[] selectionArgs = {String.valueOf(id)};
//
//        Cursor cursor = mDb.query(
//                FavouriteEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//
//        if (cursor.getCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public int getCount() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + FavouriteEntry.TABLE_NAME, null);
//        int itemsInDb = cursor.getCount();
//        return itemsInDb;
//    }

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
}


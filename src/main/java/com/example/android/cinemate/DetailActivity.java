package com.example.android.cinemate;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.data.FavouritesContract.FavouriteEntry;
import com.example.android.cinemate.data.FavouritesDbHelper;
import com.example.android.cinemate.data.MoviePreferences;
import com.example.android.cinemate.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String BASE_IMAGE_SIZE = "w185";
    private TextView mDetailMovieTitle;
    private TextView mDetailMovieOverView;
    private TextView mDetailMovieRating;
    private TextView mDetailMovieReleaseDate;
    private ImageView mDetailMovieImageView;
    private FloatingActionButton mFab;
    private SQLiteDatabase mDb;
    private FavouritesDbHelper mDbHelper;

    private String mUrlPosterPath;


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
        final Movie receivedMovie = intentThatStartedActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        mDetailMovieTitle.setText(receivedMovie.getmTitle());
        mDetailMovieOverView.setText(receivedMovie.getmOverview());
        mDetailMovieRating.setText(receivedMovie.getmRating());
        mDetailMovieReleaseDate.setText(receivedMovie.getmReleaseDate());


        // Create the DB helper (this will create the database if run for the first time)
        mDbHelper = new FavouritesDbHelper(this);
        // Get the repository in write mode
        mDb = mDbHelper.getWritableDatabase();


        mUrlPosterPath = receivedMovie.getmPosterPath();
        String urlForImage = ImageUtils.getMovieImage(mUrlPosterPath, BASE_IMAGE_SIZE);
        Picasso.with(mDetailMovieImageView.getContext()).load(urlForImage).into(mDetailMovieImageView);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        if (isAlreadyInDataBase(receivedMovie.getmTitle())) {
            mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
        } else {
            mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        }


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the current movie is not a favourite before we click on it...

//                if (!receivedMovie.ismIsFavourite()) {
//                    mFab.setImageResource(R.drawable.ic_favorite_black_24dp);
//                    Toast.makeText(getApplicationContext(), "Added!", Toast.LENGTH_SHORT).show();
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("key", true);
//                    editor.apply();
//                    receivedMovie.setmIsFavourite(true);
//
//                } else {
//                    mFab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                    Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("key", false);
//                    editor.apply();
//                    receivedMovie.setmIsFavourite(false);
//                }

                //addFavouriteToDatabase();

                if (isAlreadyInDataBase(receivedMovie.getmTitle())) {
                    mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                    // we need to delete
                    deleteFromDataBase(receivedMovie.getmTitle());
                    Toast.makeText(getApplicationContext(), "Deleted from favourites", Toast.LENGTH_SHORT).show();
                } else {
                    addFavouriteToDatabase();
                    mFab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_24dp));
                    Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public long addFavouriteToDatabase() {
        String title = mDetailMovieTitle.getText().toString();
        String overview = mDetailMovieOverView.getText().toString();
        String posterpath = mUrlPosterPath;
        String releasedate = mDetailMovieReleaseDate.getText().toString();
        String voteaverage = mDetailMovieRating.getText().toString();

        ContentValues values = new ContentValues();
        values.put(FavouriteEntry.COLUMN_NAME_TITLE, title);
        values.put(FavouriteEntry.COLUMN_NAME_OVERVIEW, overview);
        values.put(FavouriteEntry.COLUMN_POSTER_PATH, posterpath);
        values.put(FavouriteEntry.COLUMN_NAME_RELEASE_DATE, releasedate);
        values.put(FavouriteEntry.COLUMN_NAME_VOTE_AVERAGE, voteaverage);

        long newRowId = mDb.insert(FavouriteEntry.TABLE_NAME, null, values);

        //Toast.makeText(this, "items in database: " + newRowId, Toast.LENGTH_SHORT).show();

        return newRowId;
    }

    public void deleteFromDataBase(String title) {
        // Define 'where' part of query.
        String selection = FavouriteEntry.COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {title};
// Issue SQL statement.
        mDb.delete(FavouriteEntry.TABLE_NAME, selection, selectionArgs);

    }

    public boolean isAlreadyInDataBase(String title) {
        mDb = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FavouriteEntry.COLUMN_NAME_TITLE
        };
        String selection = FavouriteEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {title};

        Cursor cursor = mDb.query(
                FavouriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(MoviePreferences.IS_FAVE)) {
        }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//        if (key.equals(MoviePreferences.IS_FAVE)) {
//            IS_FAVOURITE = MoviePreferences.getFavouriteFlag(this);
//            MoviePreferences.setFavouriteFlag(this, IS_FAVOURITE);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
//    }
    }
}


package com.example.android.cinemate;

import android.content.Intent;
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
import com.example.android.cinemate.data.MovieDbHelper;
import com.example.android.cinemate.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String BASE_IMAGE_SIZE = "w185";
    private TextView mDetailMovieTitle;
    private TextView mDetailMovieOverView;
    private TextView mDetailMovieRating;
    private TextView mDetailMovieReleaseDate;
    private ImageView mDetailMovieImageView;
    private FloatingActionButton mFab;

    private String mUrlPosterPath;

    private Movie mReceivedMovie;


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



        mUrlPosterPath = mReceivedMovie.getmPosterPath();
        String urlForImage = ImageUtils.getMovieImage(mUrlPosterPath, BASE_IMAGE_SIZE);
        Picasso.with(mDetailMovieImageView.getContext()).load(urlForImage).into(mDetailMovieImageView);

        mFab = (FloatingActionButton) findViewById(R.id.fab);



        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
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
}


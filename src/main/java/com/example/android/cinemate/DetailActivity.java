package com.example.android.cinemate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private ImageView mPosterImageView;
    private TextView mLanguageTextView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;

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
            Movie movie = intentThatStartedActivity.getExtras().getParcelable(Intent.ACTION_MAIN);

            Uri baseUri = Uri.parse(TmdbUrlUtils.BASE_POSTER_URL);
            Uri.Builder builder = baseUri.buildUpon();
            builder.appendPath(TmdbUrlUtils.POSTER_SIZE);
            builder.appendEncodedPath(movie.getmPosterPath());
            String posterUrl = builder.toString();

            Context context = mPosterImageView.getContext();

            mTitleTextView.setText(movie.getmTitle());
            mOverviewTextView.setText(movie.getmOverView());
            Picasso.with(this)
                    .load(posterUrl)
                    .into(mPosterImageView);
            mReleaseDateTextView.setText(movie.getmReleaseDate());
            mRatingTextView.setText(movie.getmVoteAverage());

            Picasso.with(context).invalidate(posterUrl);

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
        intent.putExtra(Intent.EXTRA_TEXT, mTitleTextView.getText());
        intent.setType("text/plain");
        startActivity(intent);
    }
}

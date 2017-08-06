package com.example.android.cinemate;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cinemate.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String BASE_IMAGE_SIZE = "w185";
    private TextView mDetailMovieTextView;
    private ImageView detailMovieImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailMovieTextView = (TextView) findViewById(R.id.detailMovieTextView);
        detailMovieImageView = (ImageView) findViewById(R.id.detailMovieImageView);

        Intent intentThatStartedActivity = getIntent();
        Movie recievedMovie = intentThatStartedActivity.getParcelableExtra(Intent.EXTRA_TEXT);

        mDetailMovieTextView.setText(recievedMovie.getmTitle());

        String urlPathReceived = recievedMovie.getmPosterPath();

        String urlForImage = ImageUtils.getMovieImage(urlPathReceived, BASE_IMAGE_SIZE);

        Picasso.with(getApplicationContext()).load(urlForImage).into(detailMovieImageView);
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
        intent.putExtra(Intent.EXTRA_TEXT, mDetailMovieTextView.getText());
        intent.setType("text/plain");
        startActivity(intent);
    }
}

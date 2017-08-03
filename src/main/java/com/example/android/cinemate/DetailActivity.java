package com.example.android.cinemate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView mDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailTextView = (TextView) findViewById(R.id.detailTextView);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String movie = intentThatStartedActivity.getStringExtra(Intent.EXTRA_TEXT);
            mDetailTextView.setText(movie);
        }
    }
}

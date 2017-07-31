package com.example.android.cinemate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<String> movies = new ArrayList<>();
        movies.add("Scarface");
        movies.add("Casino");
        movies.add("Pulp Fiction");
        movies.add("Oldboy");
        movies.add("Jackie Brown");
        movies.add("From Dusk till Dawn");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new MovieAdapter(movies);
        mRecyclerView.setAdapter(mMovieAdapter);
    }
}

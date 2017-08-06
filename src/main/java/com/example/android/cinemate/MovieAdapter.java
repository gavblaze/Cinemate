package com.example.android.cinemate;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p";
    private static final String BASE_SIZE = "w185";

    List<Movie> mMovies;
    private ListItemClickHandler mListItemClickHandler;

    public MovieAdapter(ListItemClickHandler listItemClicked) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter constructor called");
        //mMovies = movies;
        mListItemClickHandler = listItemClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter onCreateViewHolder() called");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter onBindViewHolder() called");
//        holder.mTextView.setText(mMovies.get(position).getmTitle().toString());
//        holder.mPosterTextView.setText(mMovies.get(position).getmPosterPath().toString());

        String path = mMovies.get(position).getmPosterPath();

        Uri baseUri = Uri.parse(BASE_IMAGE_URL);

        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(BASE_SIZE);
        builder.appendPath(path);

        String urlImageString = builder.toString();

        Log.i(LOG_TAG, "TEST...................Url = " + urlImageString);
        Context context = holder.mPosterImageView.getContext();
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185//2Fy31QB9kn3XSudA15tV7UWQ9XLuW.jpg").into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
            return mMovies.size();
    }

    public void setMovieData(List<Movie> data) {
        mMovies = data;
        notifyDataSetChanged();
    }

    /*Think about using this in future instead of passing the data in the constructor*/

    public interface ListItemClickHandler {
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView mTextView;
        public ImageView mPosterImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            //mTextView = (TextView) itemView.findViewById(R.id.moviesTextView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.posterImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
//            String title = mTextView.getText().toString();
//            String path = mPosterTextView.getText().toString();
            mListItemClickHandler.onItemClicked(position);
        }
    }
}

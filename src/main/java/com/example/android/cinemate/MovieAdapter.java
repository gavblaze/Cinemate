package com.example.android.cinemate;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    List<Movie> mMovieList = new ArrayList<>();

    private ListItemClickHandler mListItemClickHandler;

    public MovieAdapter(ListItemClickHandler listItemClicked) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter constructor called");

        mListItemClickHandler = listItemClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter onCreateViewHolder() called");


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 2;

        v.setLayoutParams(lp);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter onBindViewHolder() called");

        Context context = holder.mImageView.getContext();

        String posterSize = TmdbUrlUtils.POSTER_SIZE;
        String imagePath = mMovieList.get(position).getmPosterPath();

        Uri baseUri = Uri.parse(TmdbUrlUtils.BASE_POSTER_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(posterSize);
        builder.appendEncodedPath(imagePath);
        String imageUrl = builder.toString();
        Log.i(LOG_TAG, "TESTER...................." + imageUrl);

        Picasso.with(context)
                .load(imageUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_STORE)
                .noPlaceholder()
                .into(holder.mImageView);

        Picasso.with(context).invalidate(imageUrl);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }


    /*Think about using this in future instead of passing the data in the constructor*/
    public void setMovieData(List<Movie> data) {
        mMovieList = data;
        notifyDataSetChanged();
    }


    public void addItems(List<Movie> data) {
        // If our list is empty set the data returned on our List
        if (mMovieList == null) {

            mMovieList = data;

        } else {
            // If our list isn't empty add the new items returned to it
            mMovieList.addAll(data);
        }
        notifyDataSetChanged();
    }

    //Allows us to get the value of item at current position
    public Movie getPosition(int position) {
        return mMovieList.get(position);
    }



    public interface ListItemClickHandler {
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.imagePoster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListItemClickHandler.onItemClicked(position);
        }
    }
}

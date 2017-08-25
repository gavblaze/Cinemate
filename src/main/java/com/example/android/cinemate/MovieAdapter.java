package com.example.android.cinemate;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cinemate.data.MovieContract;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    Cursor mCursor;


    private ListItemClickHandler mListItemClickHandler;

    public MovieAdapter(ListItemClickHandler listItemClicked) {
        mListItemClickHandler = listItemClicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i(LOG_TAG, "TEST.......MovieAdapter onCreateViewHolder() called");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        lp.height = (int) (parent.getMeasuredHeight() / 2);
        v.setLayoutParams(lp);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        int posterpathIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH);
        String imgpath = mCursor.getString(posterpathIndex);

        String urlImageString = TmdbUrlUtils.getImageUrl(imgpath, TmdbUrlUtils.BASE_IMAGE_SIZE);

        Context context = holder.mPosterImageView.getContext();

        Picasso.with(context).load(urlImageString).into(holder.mPosterImageView);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newValue) {
        mCursor = newValue;
        notifyDataSetChanged();
    }

    public Movie getItemClicked(int position) {
        mCursor.moveToPosition(position);

        int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
        String title = mCursor.getString(MainActivity.INDEX_MOVIE_TITLE);
        String posterpath = mCursor.getString(MainActivity.INDEX_MOVIE_POSTERPATH);
        String rating = mCursor.getString(MainActivity.INDEX_MOVIE_VOTE_AVERAGE);
        String overview = mCursor.getString(MainActivity.INDEX_MOVIE_OVERVIEW);
        String releasedate = mCursor.getString(MainActivity.INDEX_MOVIE_RELEASE_DATE);

        return new Movie(id, title, posterpath, rating, overview, releasedate);
    }

    public interface ListItemClickHandler {
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mPosterImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            mPosterImageView = (ImageView) itemView.findViewById(R.id.posterImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListItemClickHandler.onItemClicked(position);
        }
    }
}

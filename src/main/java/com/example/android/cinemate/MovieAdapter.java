package com.example.android.cinemate;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    List<String> mMovies;
    private ListItemClickHandler mListItemClickHandler;

    public MovieAdapter(List<String> movies, ListItemClickHandler listItemClicked) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter constructor called");
        mMovies = movies;
        mListItemClickHandler = listItemClicked;
    }

    public interface ListItemClickHandler {
        void onItemClicked(String position);
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
        holder.mTextView.setText(mMovies.get(position).toString());
    }

    @Override
    public int getItemCount() {
            return mMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.moviesTextView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //int title = getAdapterPosition();
            String title = mTextView.getText().toString();
            mListItemClickHandler.onItemClicked(title);
        }
    }
}

package com.example.android.cinemate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.onClick;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    List<String> mMovies;
    private Context mContext;
    //private ListItemClickHandler mOnListItemClicked;

    public MovieAdapter(List<String> movies, Context context) {
        Log.i(LOG_TAG, "TEST.......MovieAdapter constructor called");
        mMovies = movies;
        //mOnListItemClicked = itemClicked;
        mContext = context;
    }

//    public interface ListItemClickHandler {
//        public void onClick(int position);
//    }

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
            Log.i(LOG_TAG, "TEST.......MovieAdapter onClick() called");
            int position = getAdapterPosition();
            mContext = view.getContext();
            //Toast.makeText(mContext, "Item at position " + position + " clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mTextView.getText());
            mContext.startActivity(intent);

        }
    }
}

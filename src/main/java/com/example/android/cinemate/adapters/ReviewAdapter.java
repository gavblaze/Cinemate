package com.example.android.cinemate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cinemate.R;
import com.example.android.cinemate.models.Review;

import java.util.List;

/**
 * Created by Gavin on 29-Aug-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mReviews;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mAuthorTv.setText(mReviews.get(position).getmAuthor());
        holder.mContentTv.setText(mReviews.get(position).getmContent());

    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }

    public void setData(List<Review> data) {
        mReviews = data;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTv;
        public TextView mContentTv;

        public ViewHolder(View itemView) {
            super(itemView);

            mAuthorTv = (TextView) itemView.findViewById(R.id.reviewAuthorTextView);
            mContentTv = (TextView) itemView.findViewById(R.id.reviewContentTextView);
        }
    }
}

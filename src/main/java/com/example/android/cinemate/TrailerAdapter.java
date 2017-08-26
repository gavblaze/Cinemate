package com.example.android.cinemate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gavin on 26-Aug-17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private List<String> mStringList;
    private ListItemClickHandler mListItemClickhandler;

    public TrailerAdapter(ListItemClickHandler listItemClickhandler) {
        mListItemClickhandler = listItemClickhandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trailer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public String getItemClicked(int position) {
        String path = mStringList.get(position);
        return path;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.mTrailerImageView.getContext();
        String videoImageUrl = TmdbUrlUtils.getVideoImgUrl(mStringList.get(position));

        Log.i(LOG_TAG, "TEST..........." + videoImageUrl);

        Picasso.with(context).load(videoImageUrl).into(holder.mTrailerImageView);

    }

    @Override
    public int getItemCount() {
        if (mStringList == null) return 0;
        return mStringList.size();
    }

    public void setData(List<String> data) {
        mStringList = data;
        notifyDataSetChanged();
    }

    public interface ListItemClickHandler {
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mTrailerImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTrailerImageView = (ImageView) itemView.findViewById(R.id.trailerImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListItemClickhandler.onItemClicked(position);
        }
    }
}

package com.example.android.cinemate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.android.cinemate.R;
import com.example.android.cinemate.data.FetchTrailerTask;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gavin on 26-Aug-17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();
    private static final int SLIDE_DURATION = 1000;
    private List<String> mTrailerPathList;
    private ListItemClickHandler mListItemClickhandler;
    private Context mContext;

    private int mPreviousPosition = -1;


    public TrailerAdapter(Context context, ListItemClickHandler listItemClickhandler) {
        this.mContext = context;
        mListItemClickhandler = listItemClickhandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trailer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public String getItemClicked(int position) {
        String path = mTrailerPathList.get(position);
        return path;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.mTrailerImageView.getContext();
        String videoImageUrl = TmdbUrlUtils.getVideoImgUrl(mTrailerPathList.get(position));

        Picasso.with(context).load(videoImageUrl).fit().centerInside().into(holder.mTrailerImageView);

        setAnimation(holder, position);

    }

    @Override
    public int getItemCount() {
        if (mTrailerPathList == null) return 0;
        return mTrailerPathList.size();
    }

    public void setData(List<String> data) {
        mTrailerPathList = data;
        notifyDataSetChanged();
    }

    private void setAnimation(RecyclerView.ViewHolder holder, int position) {
        //if (position > mPreviousPosition) {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
        animation.setDuration(2000);
        holder.itemView.startAnimation(animation);
        mPreviousPosition = position;
        //}
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

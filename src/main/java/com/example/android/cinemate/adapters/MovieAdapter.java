package com.example.android.cinemate.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.media.RingtoneManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinemate.MainActivity;
import com.example.android.cinemate.R;
import com.example.android.cinemate.data.MovieContract;
import com.example.android.cinemate.models.Movie;
import com.example.android.cinemate.utilities.FavouriteUtils;
import com.example.android.cinemate.utilities.TmdbUrlUtils;
import com.squareup.picasso.Picasso;

import static android.R.attr.animation;

/**
 * Created by Gavin on 31-Jul-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private final static int ODD_DURATION = 300;
    private static final int EVEN_DURATION = 400;
    private static final int FADE_DURATION = 800;
    Cursor mCursor;
    private Context mContext;
    private int mPreviousPosition = -1;


    private ListItemClickHandler mListItemClickHandler;

    public MovieAdapter(ListItemClickHandler listItemClicked, Context context) {
        mListItemClickHandler = listItemClicked;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i(LOG_TAG, "TEST.......MovieAdapter onCreateViewHolder() called");

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        int posterpathIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH);
        String imgpath = mCursor.getString(posterpathIndex);
        String urlImageString = TmdbUrlUtils.getImageUrl(imgpath, TmdbUrlUtils.LARGE_IMAGE_SIZE);
        Context context = holder.mPosterImageView.getContext();
        Picasso.with(context).load(urlImageString).noFade().fit().placeholder(R.drawable.cinema64grey).into(holder.mPosterImageView);
        ViewCompat.setTransitionName(holder.mPosterImageView, urlImageString); // set a unique transition name - in this case we are using the full img url string

        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_TITLE);
        String title = mCursor.getString(titleIndex);
        holder.mMainTitleTextView.setText(title);

        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_ID);
        int movieId = mCursor.getInt(idIndex);
        if (FavouriteUtils.isFavourite(mContext, movieId)) {
            holder.mLikeImageView.setImageResource(R.drawable.starfilled);
        } else {
            holder.mLikeImageView.setImageResource(R.drawable.starborder);
        }

        //holder.mShareImageView.setImageResource(R.drawable.share_grey);


        //setAnimation(holder, position);

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
        String backdropPath = mCursor.getString(MainActivity.INDEX_MOVIE_BACKDROP_PATH);
        String rating = mCursor.getString(MainActivity.INDEX_MOVIE_VOTE_AVERAGE);
        String overview = mCursor.getString(MainActivity.INDEX_MOVIE_OVERVIEW);
        String releasedate = mCursor.getString(MainActivity.INDEX_MOVIE_RELEASE_DATE);

        return new Movie(id, title, posterpath, backdropPath, rating, overview, releasedate);
    }


    private void setAnimation(RecyclerView.ViewHolder viewHolder, int position) {
        // If the bound view wasn't previously displayed on screen, animate it! ie if we are scrolling down
        if (position > mPreviousPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom);
            if (position % 2 == 0) {
                animation.setDuration(EVEN_DURATION);
            } else {
                animation.setDuration(ODD_DURATION);
            }
            viewHolder.itemView.startAnimation(animation);
        }
        mPreviousPosition = position;
    }

    public interface ListItemClickHandler {
        void onItemClicked(int position, ImageView imageView);

        void onLikeClicked(int position);

        //void onShareClicked(int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mPosterImageView;
        public final ImageView mLikeImageView;
        //public final ImageView mShareImageView;
        public final TextView mMainTitleTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            //mContainer = (RelativeLayout) itemView.findViewById(R.id.layoutContainer);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.posterImageView);
            mLikeImageView = (ImageView) itemView.findViewById(R.id.likeImageView);
            //mShareImageView = (ImageView) itemView.findViewById(R.id.shareImageView);
            mMainTitleTextView = (TextView) itemView.findViewById(R.id.mainTitleTextView);

            mPosterImageView.setOnClickListener(this);
            mLikeImageView.setOnClickListener(this);
            //mShareImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            int id = view.getId();
            if (id == R.id.posterImageView || id == R.id.mainTitleTextView) {
                /*We pass a view for shared element transition*/
                mListItemClickHandler.onItemClicked(position, mPosterImageView);
            } else if (id == R.id.likeImageView) {
                mListItemClickHandler.onLikeClicked(position);
            }
//            else if (id == R.id.shareImageView) {
//                mListItemClickHandler.onShareClicked(position);
//            }
        }
    }
}






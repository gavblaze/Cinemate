package com.example.android.cinemate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 06-Aug-17.
 */

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private String mTitle;
    private String mPosterPath;
    private String mRating;
    private String mOverview;
    private String mReleaseDate;

    public Movie(String title, String posterPath, String rating, String overview, String releaseDate) {
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mRating = rating;
        this.mOverview = overview;
        this.mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mPosterPath = in.readString();
        mRating = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mRating);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
    }
}

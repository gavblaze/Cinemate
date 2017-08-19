package com.example.android.cinemate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 10-Aug-17.
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
    private int mId;
    private String mTitle;
    private String mOverView;
    private String mLanguage;
    private String mPosterPath;
    private String mReleaseDate;
    private String mVoteAverage;

    public Movie(int id, String title, String overView, String posterPath, String language, String releaseDate, String voteAverage) {

        this.mId = id;
        this.mTitle = title;
        this.mOverView = overView;
        this.mPosterPath = posterPath;
        this.mLanguage = language;
        this.mReleaseDate = releaseDate;
        this.mVoteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mOverView = in.readString();
        mLanguage = in.readString();
        mPosterPath = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readString();
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOverView() {
        return mOverView;
    }

    public void setmOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mOverView);
        parcel.writeString(mLanguage);
        parcel.writeString(mPosterPath);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mVoteAverage);
    }
}

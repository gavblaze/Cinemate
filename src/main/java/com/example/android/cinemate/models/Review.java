package com.example.android.cinemate.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gavin on 29-Aug-17.
 */

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String mAuthor;
    private String mContent;

    public Review(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
    }
}

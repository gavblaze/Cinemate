package com.example.android.cinemate.models;

/**
 * Created by Gavin on 31-Aug-17.
 */

public class MovieParticulars {
    private String mGenre;
    private int mRuntime;
    private String mLanguage;
    private String mTagLine;
    private String mHomepage;

    public MovieParticulars(String genre, int runtime, String language, String tagline, String homepage) {
        this.mGenre = genre;
        this.mRuntime = runtime;
        this.mLanguage = language;
        this.mTagLine = tagline;
        this.mHomepage = homepage;
    }

    public String getmGenre() {
        return mGenre;
    }

    public void setmGenre(String mGenre) {
        this.mGenre = mGenre;
    }

    public int getmRuntime() {
        return mRuntime;
    }

    public void setmRuntime(int mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getmTagLine() {
        return mTagLine;
    }

    public void setmTagLine(String mTagLine) {
        this.mTagLine = mTagLine;
    }

    public String getmHomepage() {
        return mHomepage;
    }

    public void setmHomepage(String mHomepage) {
        this.mHomepage = mHomepage;
    }
}

package com.example.android.cinemate;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Gavin on 22-Aug-17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

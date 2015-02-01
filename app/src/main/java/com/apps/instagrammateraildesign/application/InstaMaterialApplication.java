package com.apps.instagrammateraildesign.application;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Mohamed on 29/01/15.
 */
public class InstaMaterialApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}

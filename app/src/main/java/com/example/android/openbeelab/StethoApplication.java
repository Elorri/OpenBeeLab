package com.example.android.openbeelab;

import android.app.Application;

import com.facebook.stetho.Stetho;
/**
 * Created by Elorri on 02/12/2015.
 */
public class StethoApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

package com.example.user.calculator.Stetho;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by HyeonTae on 07/11/2018..
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}

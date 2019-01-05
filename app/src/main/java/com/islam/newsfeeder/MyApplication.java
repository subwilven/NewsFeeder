package com.islam.newsfeeder;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;


public class MyApplication extends Application {

    private static MyApplication mInstance;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

//    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
//        ConnectivityReceiver.listener = listener;
//    }

}
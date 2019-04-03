package com.islam.newsfeeder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.islam.newsfeeder.data.articles.ArticleApi;
import com.islam.newsfeeder.data.pocket.PocketApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    /**
     * check if the user has a WIFI connection or Mobile data on
     *
     * @param context
     * @return true if has any network connection and false if not
     */
    public static boolean haveNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            return false;
        }
    }
}

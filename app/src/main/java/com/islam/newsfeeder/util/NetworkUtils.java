package com.islam.newsfeeder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.islam.newsfeeder.data.articles.ArticleApi;
import com.islam.newsfeeder.data.pocket.PocketApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static PocketApi pocketApi;
    private static ArticleApi articleApi;

    public static PocketApi getPocketApi() {
        if (pocketApi == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            pocketApi = retrofit.create(PocketApi.class);
        }
        return pocketApi;
    }

    public static ArticleApi getArticleApi() {
        if (articleApi == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_NEWS_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            articleApi = retrofit.create(ArticleApi.class);
        }
        return articleApi;
    }


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

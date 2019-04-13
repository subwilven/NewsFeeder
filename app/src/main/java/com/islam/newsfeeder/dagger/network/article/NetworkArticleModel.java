package com.islam.newsfeeder.dagger.network.article;

import android.util.Log;

import com.google.gson.Gson;
import com.islam.newsfeeder.data.articles.ArticleApi;
import com.islam.newsfeeder.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class NetworkArticleModel {

    @Provides
    @Singleton
    static Retrofit provideRetrofitForNewsApis(Gson gson, OkHttpClient okHttpClient) {
        Log.i("pocket","new retrofit");
        Log.i("pocket","Gson : "+gson.hashCode());
        Log.i("pocket","okHttpClient : "+okHttpClient.hashCode());
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.URL_NEWS_API)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    static ArticleApi provideArticleApi(Retrofit retrofit) {
        return retrofit.create(ArticleApi.class);
    }

}

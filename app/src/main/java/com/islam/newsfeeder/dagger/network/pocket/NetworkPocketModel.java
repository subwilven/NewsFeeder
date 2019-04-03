package com.islam.newsfeeder.dagger.network.pocket;

import com.google.gson.Gson;
import com.islam.newsfeeder.data.pocket.PocketApi;
import com.islam.newsfeeder.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
@Module
public abstract class NetworkPocketModel {
    @Provides
    @Singleton
    static Retrofit provideRetrofitForPocketApis(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constants.URL_POCKET_API)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    static PocketApi providePocketApi(Retrofit retrofit) {
        return retrofit.create(PocketApi.class);
    }
}
